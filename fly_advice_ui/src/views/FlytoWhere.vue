<template>
  <div style="display: block;overflow: auto;" class="FlytoWhere">
    <div class="inlineSearch">
      <!-- 搜索输入框 -->
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
        type="date"
        placeholder="选择日期"
        style="border: #000 solid 2px;"
      ></el-date-picker>
      <el-button
        icon="el-icon-search"
        style="font-weight: 700;font-size: 14px;border: #000 solid 2px;background-color: #e7dfd5;"
      ></el-button>
    </div>
    <div id="FlyWhereMap" style="width:80%;height:800px; margin: 20px auto;border: #000 solid 2px;">
      <i class="fa fa-spinner fa-spin fa-4x fa-fw" aria-hidden="true"></i>
    </div>
  </div>
</template>
<script>
// 每天航班最繁忙的时间段是哪些？
import { initAmap } from "@/utils/remoteLoad.js";
import {
  initMapUI,
  getAmapMap,
  buildCircleValueMap
} from "@/utils/AmapOption.js";
export default {
  name: "FlytoWhere",
  components: {
  },
  data() {
    return {
      amapReady: false,
      airportToSearch: "",
      dateValue:"",
    };
  },
  async created() {
    await initAmap();
    this.initCircleValueMap();
  },
  methods: {
    initCircleValueMap() {
      var currMapId = "FlyWhereMap";
      var map = getAmapMap(this, currMapId);
      initMapUI(this, "overlay/SimpleMarker", SimpleMarker => {
         var markDataArray = [
           {text:"北京1001",pos:[116.39,39.78]},
           {text:"上海2010",pos:[121.8,31.15]},
           {text:"成都3421",pos:[103.96,30.58]},
         ];
         var simpleMarkerList = buildCircleValueMap(markDataArray,map, SimpleMarker);
      });
    }
  }
};
</script>

<style>
.mapbox {
  width: 600px;
  height: 400px;
  margin-bottom: 20px;
  float: left;
}

.FlytoWhere {
  width: 100%;
  display: inline-block;
  overflow: auto;
}
.FlytoWhere .el-input-group__prepend {
  border-right: #000 solid 2px;
}
.FlytoWhere .el-input-group__append {
  border-left: #000 solid 2px;
}
.FlytoWhere .el-input-group__prepend,
.FlytoWhere .el-input-group__append {
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
/* .inlineSearch .block{
display: inline-flex;
line-height: 40px;
padding: 3px 10px;

position: relative;
height: 40px;
width: 350px;
}

.xxxx{
display: inline-block;
line-height: 1;
padding: 12px 20px;

margin: 0;
white-space: nowrap;
cursor: pointer;
} */
</style>