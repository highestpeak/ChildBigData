import com.cloudera.sparkts.*;
import com.cloudera.sparkts.api.java.DateTimeIndexFactory;
import com.cloudera.sparkts.models.ARIMA;
import com.cloudera.sparkts.models.ARIMAModel;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.mllib.linalg.Vector;
import org.apache.spark.rdd.RDD;
import org.apache.spark.sql.*;
import org.apache.spark.sql.catalyst.encoders.RowEncoder;
import org.apache.spark.sql.catalyst.expressions.Encode;
import org.apache.spark.sql.types.DataTypes;
import org.apache.spark.sql.types.Metadata;
import org.apache.spark.sql.types.StructField;
import org.apache.spark.sql.types.StructType;
import scala.Tuple2;
import scala.reflect.ClassTag;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;
import java.util.function.Function;

/**
 * @author highestpeak
 * todo:
 *  1. 对于航班的评分（和对电影的评分类似）
 *  2. 航班的协同过滤
 *  3. 指定出发地到达地，价格的线性回归--->获取过去12天，将来12天可以获得的数据，然后预测将来x天的价格
 *  4. 指定出发地出发时间，返回价格或3的预测价格
 *  5. 可以让横轴为 各大航空公司，纵轴为每个时间点，或者每个地点，然后就可以协同过滤找出缺失价格？
 *
 *  todo:时间序列分析
 *  todo：协同过滤
 *  指定用户，航班，评分
 *  预测：用户对返回所有航班的评分
 *  todo: 基础数据分析
 *
 *  // 时间序列分析
 *  // https://www.jianshu.com/p/c14e27ac2918
 *  // http://sryza.github.io/spark-timeseries/0.3.0/index.html
 *  // https://blog.csdn.net/qq_30232405/article/details/70622400
 *  // https://www.cn.kayak.com/help/lowfares
 *  // https://www.jianshu.com/p/31e20f00c26f?spm=5176.12282029.0.0.36241491UUhnZE
 *  // https://zhuanlan.zhihu.com/p/67832773
 *
 *  https://blog.csdn.net/qq_30232405/article/details/70622400
 *  https://github.com/sryza/spark-ts-examples/blob/master/jvm/src/main/java/com/cloudera/tsexamples/JavaStocks.java
 *  todo:
 *   1. tostring会影响rdd分组
 *   2. 很多航班只有在某段时间运行，所以按照航班来训练不太妥，最好按照 每天的一个价格来训练
 */
public class SparkApplication {
    private static final String CSV_PATH = "E:\\_document\\实训\\其他软件或资料\\成都-广州-2017-2019\\成都-广州-CTU-CAN-";
    private static Function<String[],Date> strDate = (str)->{
        Date date = null;
        try {
            date = new SimpleDateFormat(str[0]).parse(str[1]);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        assert date != null;
        return date;
    };
    private static Function<String[],LocalDate> toLocalDate = (str)-> strDate.apply(str).toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    private static Function<String[], LocalTime> toLocalTime = (str)-> strDate.apply(str).toInstant().atZone(ZoneId.systemDefault()).toLocalTime();

    public static void main(String[] args){
        System.setProperty("hadoop.home.dir","E:\\_environment\\hadoop-2.9.2");
        SparkConf conf=new SparkConf().setAppName("Fly-Advice").setMaster("local[4]");
        JavaSparkContext ctx = new JavaSparkContext(conf);

        // String csvLoad = CSV_PATH + "2017-01-16.csv";
        String csvLoad = "E:\\_code\\code_github\\ChildBigData\\fly_advice_spark\\src\\main\\resources\\北京-上海半年.CSV";
        Dataset<Row> flightObs = loadObservations(ctx, csvLoad);

        LocalDate startTime = toLocalDate.apply(new String[]{"yyyyMMdd","20200101"});
        LocalDate endTime = toLocalDate.apply(new String[]{"yyyyMMdd","20200601"});
        // DateTimeIndex[2015-04-10,2015-04-11] key = A,series = [2.0,3.0],series 为时间范围内每个单位时间的key对应的值
        UniformDateTimeIndex uniformDateTimeIndex = DateTimeIndexFactory.uniformFromInterval(
                ZonedDateTime.of(startTime.atStartOfDay(), ZoneId.systemDefault()),
                ZonedDateTime.of(endTime.atStartOfDay(), ZoneId.systemDefault()),
                new DayFrequency(1)
        );
        // 必须在project structure 设定 modules 含有 scala 才能编译
        TimeSeriesRDD<String> stringTimeSeriesRDD =
                TimeSeriesRDD.timeSeriesRDDFromObservations(
                        uniformDateTimeIndex, flightObs,
                        "timestamp", "symbol", "price"
                );
        stringTimeSeriesRDD.cache();
        stringTimeSeriesRDD.fill("linear");
        int predictN = 10;
        JavaRDD<double[]> arimaModelTrain = arimaModelTrain(stringTimeSeriesRDD, predictN);
        arimaModelTrain.foreach(item->System.out.println(Arrays.toString(item)));

        ctx.close();
    }

    private static Dataset<Row> loadObservations(JavaSparkContext ctx, String csvLoad) {
        JavaRDD<String> orignRowRDD = ctx.textFile(csvLoad);
        String header = orignRowRDD.first();
        JavaRDD<Row> rowRDD = orignRowRDD
                .filter(row -> !row.equals(header))
                .filter(row->{
                    String[] tokens = row.split(",");
                    return !tokens[0].contains("noflight");
                })
                .map((String line)->{
                    String[] tokens = line.split(",");
                    ZonedDateTime dt = ZonedDateTime.of(
                            toLocalDate.apply(new String[]{"yyyy-MM-dd",tokens[7]}),
                            toLocalTime.apply(new String[]{"hh:mm",tokens[1]}),
                            ZoneId.systemDefault()
                    );
                    Flight symbolFlight=
                            new Flight()
                            .setFlightNumber(tokens[0])
                            .setSource(tokens[2])
                            .setDestination(tokens[4])
                            .setModel(tokens[5]);
                    double price = Double.parseDouble(tokens[6]);
                    return RowFactory.create(
                            Timestamp.from(dt.toInstant()),
                            "key",
                            price
                    );
                });

        List<StructField> fields = new ArrayList<>();
        fields.add(DataTypes.createStructField("timestamp", DataTypes.TimestampType, true));
//        fields.add(DataTypes.createStructField("symbol", flightDetails, true));
        fields.add(DataTypes.createStructField("symbol",  DataTypes.StringType, true));
        fields.add(DataTypes.createStructField("price", DataTypes.DoubleType, true));
        StructType schema = DataTypes.createStructType(fields);
        // 依赖错误，执行clean并再次执行install
        return new SparkSession(ctx.sc()).createDataFrame(rowRDD,schema).toDF();
    }

    private static JavaRDD<double[]> arimaModelTrain(TimeSeriesRDD<String> trainTsRDD,int predictedN){
        JavaRDD<Tuple2<ARIMAModel, Vector>> arimaAndVectorRdd =
                trainTsRDD.toJavaRDD().map(line ->
                        new Tuple2<>(ARIMA.autoFit(line._2, 5, 1, 5), line._2)
                );
        JavaRDD<Vector> forecast = arimaAndVectorRdd.map(tuple -> tuple._1.forecast(tuple._2, predictedN));
        JavaRDD<double[]> forecastValue = forecast.map(Vector::toArray);

        return forecastValue;
    }

    public static class Flight{
        private String flightNumber;
        private String source;
        private String destination;
        private String model;

        public Flight() {
        }

        public Flight(String flightNumber, String source, String destination, String model) {
            this.flightNumber = flightNumber;
            this.source = source;
            this.destination = destination;
            this.model = model;
        }

        public String getFlightNumber() {
            return flightNumber;
        }

        public Flight setFlightNumber(String flightNumber) {
            this.flightNumber = flightNumber;
            return this;
        }

        public String getSource() {
            return source;
        }

        public Flight setSource(String source) {
            this.source = source;
            return this;
        }

        public String getDestination() {
            return destination;
        }

        public Flight setDestination(String destination) {
            this.destination = destination;
            return this;
        }

        public String getModel() {
            return model;
        }

        public Flight setModel(String model) {
            this.model = model;
            return this;
        }

//        @Override
//        public String toString() {
//            return "Flight{" +
//                    "flightNumber='" + flightNumber + '\'' +
//                    ", source='" + source + '\'' +
//                    ", destination='" + destination + '\'' +
//                    ", model='" + model + '\'' +
//                    '}';
//        }

        @Override
        public String toString() {
            return "Flight{" +
                    "flightNumber='" + flightNumber + '\'' +
                    '}';
        }
    }
}
