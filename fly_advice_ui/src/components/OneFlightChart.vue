<template>
  <div class="OneFlightChart">
    <!-- 用户选择的航班分析 -->
    <!-- ⭐过去一周价格/历史价格变动(用户最后选择要购买的航行计划) -->
    <!-- ⭐航班 日价格、周价格、月价格 平均  -->
    <el-tabs :tab-position="tabPosition" style="width:100%">
      <el-tab-pane label="周价格变化">
        <div id="oneFlightWeek" style="width:700px;height:200px;"></div>
      </el-tab-pane>
      <el-tab-pane label="历史日均价">
        <div id="oneFlightHistoryDay" style="width:700px;height:200px;"></div>
      </el-tab-pane>
      <el-tab-pane label="历史周均价">
        <div id="oneFlightHistoryWeek" style="width:700px;height:200px;"></div>
      </el-tab-pane>
      <el-tab-pane label="历史月均价">
        <div id="oneFlightHistoryMonth" style="width:700px;height:200px;"></div>
      </el-tab-pane>
    </el-tabs>
  </div>
</template>

<script>
import {
  initEchart,
  buildLineChartOption,
  buildSeries
} from "@/utils/EchartsOption.js";
export default {
  name: "OneFlightChart",
  props: [],
  components: {},
  data() {
    return {
      tabPosition: "left"
    };
  },
  created() {},
  mounted() {
    this.initAllLineEchart();
  },
  methods: {
    initAllLineEchart() {
      var dayOfWk = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
      var wkSeries = [820, 932, 901, 934, 129, 133, 132];
      var seriesArray = [];
      seriesArray.push(
        buildSeries("wkFly", wkSeries)
      );
      var option = buildLineChartOption(dayOfWk, seriesArray);
      initEchart(this.$echarts, "oneFlightWeek", option);

      //todo:
      var historyDayData = [];
      var base = +new Date(1968, 9, 3);
      var oneDay = 24 * 3600 * 1000;
      var date = [];
      var data = [Math.random() * 300];
      for (var i = 1; i < 20000; i++) {
        var now = new Date((base += oneDay));
        date.push([now.getFullYear(), now.getMonth() + 1, now.getDate()].join('/'));
        data.push(Math.round((Math.random() - 0.5) * 20 + data[i - 1]));
      }
      seriesArray = [];
      seriesArray.push(
        buildSeries("history", data)
      );
      var  grid= {
            left: 40,
            top: 20,
            right: 10,
            bottom: 60,
        };
      option = buildLineChartOption(date, seriesArray,  true,grid);
      initEchart(this.$echarts, "oneFlightHistoryDay", option);

    //   this.initLineEchart("oneFlightHistoryDay", historyDayData);
    //   this.initLineEchart("oneFlightHistoryWeek", lastWeekData);
    //   this.initLineEchart("oneFlightHistoryMonth", lastWeekData);
    }
  }
};
</script>

<style>
.OneFlightChart {
  overflow: auto;
}
.OneFlightChart .el-tabs__active-bar {
  background-color: lightcoral;
}
.OneFlightChart .is-active {
  color: lightcoral;
}
.OneFlightChart .el-tabs__item:hover {
  color: lightcoral;
  cursor: pointer;
}
</style>