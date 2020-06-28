<template>
  <div>
    <!-- 用户 -->
    <!-- 用户选择的航班分析 -->
    <div id="lineAreaChart" style="width: 650px;height:400px;"></div>
    <!-- 航班对比 -->
    <!-- ⭐价格对比/耗时对比 （ 用户选择两个航班 然后两个航班进行 ） -->
    <!-- 相对于评分最高航班的对比 -->
    <!-- ⭐对每一个用户选择的航班进行（ 用户选择两个航班 然后两个航班进行 ） 雷达图建模 -->
    <!-- https://echarts.apache.org/examples/zh/editor.html?c=radar -->


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

    <!-- 何时飞 -->
    <!-- ⭐显示预测和历史价格（横轴：时间：周、月、日 纵轴：价格 ） (平均价格/最高价格/最低价格是不同的柱形)-->
    <!-- 1. https://echarts.apache.org/examples/zh/editor.html?c=mix-line-bar -->
    <!-- ⭐日历图，该航线的每天的最低价格/历史上的每天的最低价格 -->
    <!--  -->


    <!-- 飞去哪儿 -->
    <!-- 1. （高德价格圆圈图） -->

    <!-- 分析维度 -->
    <!-- 时间 -->
    <!-- 省份、机场、城市、航班、航空公司 -->
    <!-- 日、周、月、年 -->
    <!-- 价格、耗时、数量 -->
    <!-- 出发、到达 -->
    <!-- 起飞时间、降落时间 -->
  </div>
</template>

<script>
import { buildLineAreaOption } from "@/utils/EchartsOption.js";
export default {
  name: "ChartView",
  components: {
  },
  data() {
    return {};
  },
  created() {},
  mounted() {
    this.initLineEchart();
  },
  methods: {
    initLineEchart() {
      // var myChart = this.$echarts.init(
      //   document.getElementById("lineAreaChart")
      // );
      // var data = [
      //   ["Mon", 820],
      //   ["Tue", 932],
      //   ["Wed", 901],
      //   ["Thu", 934],
      //   ["Fri", 1290],
      //   ["Sat", 1330],
      //   ["Sun", 1320]
      // ];
      // myChart.setOption(buildLineAreaOption(data));
    }
  }
};
</script>

<style scoped>
</style>