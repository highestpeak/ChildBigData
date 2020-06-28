package com.scu.highestpeak.child.fly_advice.service.FlightSpider;

import com.scu.highestpeak.child.fly_advice.domain.BO.Airport;
import com.scu.highestpeak.child.fly_advice.domain.BO.Flight;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;

/**
 * @author highestpeak
 */
public abstract class AbstractCrawlTask implements Callable<List<Flight>> {
    Date start;
    Airport source;
    Airport destination;

    public AbstractCrawlTask() {
    }

    public AbstractCrawlTask(Airport source, Airport destination, Date startDate) {
        this.start = startDate;
        this.source = source;
        this.destination = destination;
    }

    /**
     * 爬虫调用逻辑
     * @return 爬取到的航班列表
     * todo: client 复用
     *  异常捕获
     */
    @Override
    public List<Flight> call() throws Exception {
        OkHttpClient client = new OkHttpClient().newBuilder().build();
        Response response = client.newCall(buildRequest()).execute();
        String jsonData = response.body().string();
        return parseDataToFlights(jsonData);
    }

    /**
     * 模板方法
     */
    abstract Request buildRequest();

    /**
     * 模板方法
     */
    abstract List<Flight> parseDataToFlights(String jsonData);

    Date getStart() {
        return start;
    }

    String getSource() {
        return source.getName();
    }

    String getDestination() {
        return destination.getName();
    }
}
