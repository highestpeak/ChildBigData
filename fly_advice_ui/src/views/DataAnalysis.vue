<template>
  <div class="DataAnalysis">
    <!-- 机场分析 -->
    <!-- ⭐对于机场的雷达图建模/相对于所有的机场的极值/或者相对于某一省份机场的极值 -->
    <!-- 1. https://echarts.apache.org/examples/zh/editor.html?c=radar -->
    <!-- ⭐全年流量 -->
    <!-- 1. https://echarts.apache.org/examples/zh/editor.html?c=calendar-vertical -->
    <!-- 2. 高德map 航线图 : 某个机场出发，地图/统计图 显示到达机场航线数量/日/月/年 （高德航线图，粗细，速度）-->
    <!-- ⭐主要机场流量分析，不同机场的平均 航班数量 繁忙程度 -->
    <!-- 1. https://echarts.apache.org/examples/zh/editor.html?c=bar-polar-real-estate -->
    <!-- ⭐某一天/某一周/某一月 某个机场/省份 出发/到达 的航班数量 （高德地域图，数量信息） -->
    <!-- 1. 高德地域图，数量信息 -->
    <!-- 2. 折线图 -->
    <!-- ⭐主要航线，该机场飞向其他机场的数量 比例 -->
    <!-- 1. https://echarts.apache.org/examples/zh/editor.html?c=watermark -->

    <!-- 航线分析 -->
    <!-- ⭐A机场到B机场，某一时间段 每天/周/月 流量 （柱形图/折线图 ） -->
    <!--  -->
    <!-- ⭐A机场到B机场，某一时间段 每天/周/月 价格 （柱形图/折线图 ）(平均价格/最高价格/最低价格是不同的柱形) -->
    <!-- 1. https://echarts.apache.org/examples/zh/editor.html?c=mix-line-bar -->
    <!-- ⭐A机场到B机场，航线 最低耗时 平均 最长 数值信息 -->
    <!--  -->
    <el-alert
      class="alertInfo"
      title="消息提示"
      type="info"
      description="更多分析内容敬请期待"
      show-icon
      style="text-align:left;margin:5px auto;"
    ></el-alert>
    <el-alert
      class="alertInfo"
      title="消息提示"
      type="info"
      description="您可以选择机场来进行本应用支持的分析"
      show-icon
      style="text-align:left;margin:5px auto;"
    ></el-alert>

    <!-- 搜索输入框 -->
    <el-autocomplete
      v-model="airportToSearch"
      :fetch-suggestions="querySearchAsync"
      :trigger-on-focus="false"
      placeholder="北京首都国际机场"
      @select="handleAirportSelect"
      style="margin: 18px 0px;width: 61%;border: #000 solid 2px;border: #000 solid 2px;"
    >
      <template slot="prepend" style="border: #000 solid 3px;">选择分析机场</template>
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-autocomplete>

    <!-- 简要信息栏 -->
    <!-- 机场可以到达的/热门的机场航线 -->
    <el-row>
      <el-col :span="6">
        <!-- 机场简要信息 -->
        <div class="DataAnalysisHeadMapLine">
          <span style="display:block;text-align:center;margin:15px 10px;font-size:20px;">北京首都国际机场</span>
          <span style="display:block;text-align:left;margin:5px 10px;font-size:20px;">三字码:asdsadasd</span>
          <span style="display:block;text-align:left;margin:5px 10px;font-size:20px;">机场名:asdsadasd</span>
          <span style="display:block;text-align:left;margin:5px 10px;font-size:20px;">所在地:asdsadasd</span>
          <span style="display:block;text-align:left;margin:5px 10px;font-size:20px;">省份地:asdsadasd</span>
          <span style="display:block;text-align:left;margin:5px 10px;font-size:20px;">经度值:asdsadasd</span>
          <span style="display:block;text-align:left;margin:5px 10px;font-size:20px;">纬度值:asdsadasd</span>
        </div>
      </el-col>

      <el-col :span="18">
        <!-- 地图 -->
        <div id="DataAnalysisHeadMap" class="DataAnalysisHeadMap DataAnalysisHeadMapLine">
          <i class="fa fa-spinner fa-spin fa-4x fa-fw" aria-hidden="true"></i>
        </div>
      </el-col>
    </el-row>

    <h1>省份到达图</h1>
    <div id="DistinctMap" style="height:500px; margin: 20px auto;border: #000 solid 2px;">
      <i class="fa fa-spinner fa-spin fa-4x fa-fw" aria-hidden="true"></i>
    </div>
    <div id="RadarChart" style="width:1000px;height:500px; margin: 20px auto;"></div>
    <div id="HeartMapChart" style="width:1000px;height:500px; margin: 20px auto;"></div>
  </div>
</template>

<script>
import { airportsCached } from "@/utils/LocalCacheData";
import {
  initMapUI,
  getAmapMap,
  buildP2PFlightLineMap,
  buildP2PArcData,
  buildP2PNavg,
  buildDistrictCluster
} from "@/utils/AmapOption.js";
import { findInfoOfAirport } from "@/utils/LocalCacheData.js";
import { initAmap } from "@/utils/remoteLoad.js";
import {
  initEchart,
  buildRadarOption,
  buildHeatMapOption
} from "@/utils/EchartsOption.js";
export default {
  name: "DataAnalysis",
  components: {},
  data() {
    return {
      airportToSearch: ""
    };
  },
  async created() {
    await initAmap();
    this.initAllDestinationFlightMap();
    this.initDistinctMap();
  },
  mounted() {
    this.initAllLineEchart();
  },
  methods: {
    handleAirportSelect() {},
    parseLocation(info) {
      // console.log(info)
      return {
        lot: parseFloat(info.longitude),
        lat: parseFloat(info.latitude)
      };
    },
    // 画航线图
    initAllDestinationFlightMap() {
      var currAirport = findInfoOfAirport("name", "北京首都国际机场");
      var destinationAirportList = [
        findInfoOfAirport("name", "上海虹桥国际机场"),
        findInfoOfAirport("name", "成都双流国际机场"),
        findInfoOfAirport("name", "重庆江北国际机场"),
        findInfoOfAirport("name", "广州白云国际机场"),
        findInfoOfAirport("name", "乌鲁木齐地窝堡国际机场"),
        findInfoOfAirport("name", "武汉天河国际机场"),
        findInfoOfAirport("name", "长春龙嘉国际机场"),
        findInfoOfAirport("name", "厦门高崎国际机场")
      ];
      var currMapId = "DataAnalysisHeadMap";

      var start = this.parseLocation(currAirport);

      var mapConfig = {
        zoom: 4,
        center: [start.lot, start.lat]
      };
      var map = getAmapMap(this, currMapId, mapConfig);

      initMapUI(this, "misc/PathSimplifier", PathSimplifier => {
        var pathSimplifierIns = buildP2PFlightLineMap(map, PathSimplifier);
        var pathDataArray = [];

        for (let index = 0; index < destinationAirportList.length; index++) {
          const destination = destinationAirportList[index];
          var end = this.parseLocation(destination);
          pathDataArray = buildP2PArcData(
            "name",
            start,
            end,
            PathSimplifier,
            pathDataArray
          );
        }

        pathSimplifierIns.setData(pathDataArray);
        for (let index = 0; index < destinationAirportList.length; index++) {
          var navg = buildP2PNavg(index, 1000000, true, pathSimplifierIns);
          navg.start();
        }
      });
    },

    // 地域来源 图
    initDistinctMap() {
      var currAirport = findInfoOfAirport("name", "北京首都国际机场");
      var start = this.parseLocation(currAirport);
      var mapConfig = {
        zoom: 4,
        center: [start.lot, start.lat]
      };
      var currMapId = "DistinctMap";
      var map = getAmapMap(this, currMapId, mapConfig);

      initMapUI(this, "geo/DistrictCluster", DistrictCluster => {
        var province = [
          "上海市",
          "黑龙江省",
          "陕西省",
          "福建省",
          "四川省",
          "广东省",
          "新疆维吾尔自治区",
          "云南省"
        ];
        var districtCluster = buildDistrictCluster(
          map,
          DistrictCluster,
          [],
          province
        );

        //随机创建一批点，仅作示意
        // todo:循环某一个地点的经纬度数量放进去
        var test = { position: [113.864691, 22.942327] };
        var data = [];
        for (let index = 0; index < 30000; index++) {
          data.push({ position: [113.336586, 33.729581] });
          data.push({ position: [104.137953, 30.784276] });
          data.push({ position: [114.141516, 23.159282] });
          data.push({ position: [120.499683, 30.042305] });
          data.push({ position: [120.487242, 32.180365] });
          data.push({ position: [108.94686, 34.362975] });
          data.push({ position: [121.299895, 31.105064] });
        }
        //设置数据
        districtCluster.setData(data);
      });
    },

    querySearchAsync(queryString, cb) {
      var results = queryString
        ? airportsCached.filter(this.createStateFilter(queryString))
        : airportsCached;
      var finalResults = [];
      for (let index = 0; index < results.length; index++) {
        const airportFound = results[index];
        finalResults.push({
          value: airportFound["name"] + "(" + airportFound["IATA"] + ")",
          airport: airportFound
        });
      }
      // console.log(finalResults)
      cb(finalResults);
      // clearTimeout(this.timeout);
      // this.timeout = setTimeout(() => {
      //   cb(finalResults);
      // }, 1000);
    },

    // 过滤字符串，改变格式
    createStateFilter(queryString) {
      return airport => {
        var airportStr = airport["name"] + "(" + airport["IATA"] + ")";
        return airportStr.indexOf(queryString) != -1;
      };
    },

    // 画echartss
    initAllLineEchart() {
      initEchart(this.$echarts, "RadarChart", buildRadarOption());

      function getVirtulData(echarts, year) {
        year = year || "2017";
        var date = +echarts.number.parseDate(year + "-01-01");
        var end = +echarts.number.parseDate(+year + 1 + "-01-01");
        var dayTime = 3600 * 24 * 1000;
        var data = [];
        for (var time = date; time < end; time += dayTime) {
          data.push([
            echarts.format.formatTime("yyyy-MM-dd", time),
            Math.floor(Math.random() * 10000)
          ]);
        }
        return data;
      }
      var testData = getVirtulData(this.$echarts, 2016);
      initEchart(this.$echarts, "HeartMapChart", buildHeatMapOption(testData));
    }
  }
};
</script>

<style>
.DataAnalysis {
  width: 80%;
  display: inline-block;
  overflow: auto;
}
.DataAnalysis .el-input-group__prepend {
  border-right: #000 solid 2px;
}
.DataAnalysis .el-input-group__append {
  border-left: #000 solid 2px;
}
.DataAnalysis .el-input-group__prepend,
.DataAnalysis .el-input-group__append {
  background-color: #e7dfd5;
  color: #000;
  font-weight: 700;
  font-size: 20px;
}

.DataAnalysisHeadMapLine {
  margin-top: 0px;
  margin-bottom: 10px;
  height: 300px;
  border: #1f4068 solid 2px;
  background-color: #e7dfd5;
  color: #000;
}
.DataAnalysisHeadMap {
  display: block;
  float: right;
  width: 100%;
}
</style>