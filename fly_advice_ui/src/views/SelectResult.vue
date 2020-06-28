<template>
  <div class="selectResult">
    <!-- 标头 -->
    <div class="bubble" style="display: inline-block;width:100%;">
      <h1>{{flyPlan.sections[0].endPlace}}</h1>
      <h3 style="color:#e36387">{{supplier.name}} : {{supplier.price}}</h3>
      <h5>
        <i class="fa fa-paper-plane" aria-hidden="true"></i>
      </h5>
    </div>

    <!-- 航段列表 -->
    <div
      v-for="(section,index) in flyPlan.sections"
      :key="index"
      style="display: block;width:90%;overflow: auto;margin: 0 auto;"
    >
      <!-- 地图 -->
      <div :id="mapLineContainerID[index]" class="map">
        <i class="fa fa-spinner fa-spin fa-5x fa-fw" aria-hidden="true"></i>
      </div>

      <!-- 手风琴航段项 -->
      <el-collapse>
        <el-collapse-item>
          <template slot="title">
            <FlightSection :section="section" />
          </template>

          <!-- 价格 -->
          <Supplier
            v-for="(xsupplier,index) in section.supplier.list"
            :key="index"
            :supplier="xsupplier"
            :flyPlan="null"
          />
        </el-collapse-item>
      </el-collapse>

      <!-- 用户选择的航班分析 -->
     <OneFlightChart style="margin: 18px 0px;width: 61%;border: #000 solid 2px;"/>
    </div>
    <br />
    <br />
    {{supplier}}
    <br />
    {{flyPlan}}
  </div>
</template>

<script>
import FlightSection from "@/components/FlightSection.vue";
import OneFlightChart from "@/components/OneFlightChart.vue"
import Supplier from "@/components/Supplier.vue";
import { findInfoOfAirport } from "@/utils/LocalCacheData.js";
import {
  initMapUI,
  getAmapMap,
  buildP2PFlightLineMap,
  buildP2PArcData,
  buildP2PNavg
} from "@/utils/AmapOption.js";
import { initAmap } from "@/utils/remoteLoad.js";
export default {
  name: "SelectResult",
  props: ["supplier", "flyPlan"],
  components: {
    FlightSection,
    Supplier,
    OneFlightChart,
  },
  data() {
    return {
      mapLineContainerID: []
    };
  },
  created() {
    this.initAllSectionFlightMap();
  },
  methods: {
    async initOneFlightLineMap(start, end, mapLineContainerID) {
      await initAmap();
      var mapConfig = {
        zoom: 2,
        center: [start.lot, start.lat]
      };
      var map = getAmapMap(this, mapLineContainerID, mapConfig);
      initMapUI(this, "misc/PathSimplifier", PathSimplifier => {
        var pathSimplifierIns = buildP2PFlightLineMap(map, PathSimplifier);
        var pathDataArray = [];

        pathDataArray = buildP2PArcData(
          "name",
          start,
          end,
          PathSimplifier,
          pathDataArray
        );
        pathSimplifierIns.setData(pathDataArray);
        // var navgList = [];
        var navg = buildP2PNavg(0, 1000000, true, pathSimplifierIns);
        navg.start();
      });
    },
    initAllSectionFlightMap() {
      function findLocation(airportName) {
        var info = findInfoOfAirport('name',airportName);
        // console.log(info)
        return {
          lot: parseFloat(info.longitude),
          lat: parseFloat(info.latitude)
        };
      }
      this.mapLineContainerID = [];
      for (let index = 0; index < this.flyPlan.sections.length; index++) {
        const section = this.flyPlan.sections[index];
        var currMapId = "mapLine-container-" + index;
        this.mapLineContainerID.push(currMapId);
        // var start = {
        //   // section.startPlace
        //   lot: 116.405289,
        //   lat: 39.904987
        // };
        var start = findLocation(section.startPlace);
        var end = findLocation(section.endPlace);
        // console.log(start,end)
        this.initOneFlightLineMap(start, end, currMapId);
      }
    }
  }
};
</script>

<style scoped>
/* 下三角效果 */
.bubble {
  position: relative;
  width: 400px;
  height: 150px;
  padding: 0px;
  background: #1f4068;
  color: #ffffff;
  border: #000 solid 1px;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
  margin-bottom: 30px;
}

.bubble:after {
  content: "";
  position: absolute;
  bottom: -25px;
  left: 48.5%;
  border-style: solid;
  border-width: 25px 25px 0;
  border-color: #1f4068 transparent;
  display: block;
  width: 0;
  z-index: 1;
}

/* .bubble:before {
  content: "";
  position: absolute;
  top: 250px;
  left: 50%;
  border-style: solid;
  border-width: 26px 26px 0;
  border-color: #000 transparent;
  display: block;
  width: 0;
  z-index: 0;
} */

/* 地图 */
.map {
  display: block;
  float: right;
  margin-top: 0px;
  margin-bottom: 10px;
  margin-left: 20px;
  width: 35%;
  height: 300px;
  border: #1f4068 solid 2px;
}
</style>

<style>
.selectResult .el-collapse-item__content {
  padding: 0px;
}
.selectResult .el-collapse-item__header {
  height: fit-content;
  border: 0px;
}
.selectResult .el-collapse-item__header .el-collapse-item__arrow {
  font-size: 30px;
  color: lightcoral;
}
</style>