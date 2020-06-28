<template>
  <div style="display: block;overflow: auto;" class="WhenFly">
    <div class="inlineSearch">
      <el-input
        v-model="airportToSearch"
        :trigger-on-focus="false"
        placeholder="北京首都国际机场"
        style="border: #000 solid 2px;"
      >
        <template slot="prepend" style="border: #000 solid 3px;">选择出发机场</template>
      </el-input>
      <el-date-picker
        v-model="dateValue"
        type="daterange"
        range-separator="至"
        start-placeholder="开始日期"
        end-placeholder="结束日期"
        style="border: #000 solid 2px;"
      ></el-date-picker>
      <el-button
        icon="el-icon-search"
        style="font-weight: 700;font-size: 14px;border: #000 solid 2px;background-color: #e7dfd5;"
      ></el-button>
    </div>
    <div
      id="whenFlyLineChart"
      style="width:50%;height:200px;border: #000 solid 2px;margin: 20px auto;"
    ></div>
    <div
      id="whenFlyColumnarChart"
      style="width:50%;height:200px;border: #000 solid 2px;margin: 20px auto;"
    ></div>
  </div>
</template>

<script>
import {
  initEchart,
  buildLineChartOption,
  buildSeries
} from "@/utils/EchartsOption.js";
export default {
  name: "WhenFly",
  components: {},
  data() {
    return {
      airportToSearch: "",
      dateValue: ""
    };
  },
  created() {},
  mounted() {
    this.initLineEchart();
  },
  methods: {
    initLineEchart() {
      var dayOfWk = ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"];
      var wkSeries = [820, 932, 901, 934, 129, 133, 132];
      var seriesArray = [];
      seriesArray.push(buildSeries("wkFly", wkSeries));
      var option = buildLineChartOption(dayOfWk, seriesArray);
      initEchart(this.$echarts, "whenFlyLineChart", option);
      seriesArray = [];
      seriesArray.push(buildSeries("wkFly", wkSeries, false, false, true));
      option = buildLineChartOption(dayOfWk, seriesArray);
      initEchart(this.$echarts, "whenFlyColumnarChart", option);
    }
  }
};
</script>

<style>
.WhenFly .el-input-group__prepend {
  border-right: #000 solid 2px;
}
.WhenFly .el-input-group__append {
  border-left: #000 solid 2px;
}
.WhenFly .el-input-group__prepend,
.WhenFly .el-input-group__append {
  background-color: #e7dfd5;
  color: #000;
  font-weight: 700;
  font-size: 20px;
}
.inlineSearch {
  display: flex;
  flex-flow: row nowrap;
  margin: 0px auto;
  width: 50%;
}
</style>