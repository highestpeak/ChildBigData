<template>
  <div style="display: block;overflow: auto;" class="FlytoWhere">
    <!-- 搜索输入框 -->
    <el-input
      v-model="airportToSearch"
      :trigger-on-focus="false"
      placeholder="北京首都国际机场"
      style="margin: 18px 0px;width: 61%;border: #000 solid 2px;border: #000 solid 2px;"
    >
      <template slot="prepend" style="border: #000 solid 3px;">选择分析机场</template>
      <el-button slot="append" icon="el-icon-search"></el-button>
    </el-input>
    <el-button
      icon="el-icon-search"
      style="font-weight: 700;
  font-size: 14px;border: #000 solid 2px;background-color: #e7dfd5;"
    ></el-button>

    <MapCircleValue v-if="amapReady" class="mapbox" />
  </div>
</template>
<script>
// 每天航班最繁忙的时间段是哪些？

import MapCircleValue from "@/components/amap/MapCircleValue.vue";
import { remoteLoad } from "@/utils/remoteLoad.js";
import { MapKey, MapCityName } from "@/api.js";
export default {
  name: "FlytoWhere",
  components: {
    MapCircleValue
  },
  data() {
    return {
      amapReady: false,
      airportToSearch: ""
    };
  },
  async created() {
    if (window.AMap && window.AMapUI) {
      // 未载入高德地图API，则先载入API再初始化
      this.amapReady = true;
    } else {
      // console.log("parent 1");
      await remoteLoad(`http://webapi.amap.com/maps?v=1.4.15&key=${MapKey}`);
      await remoteLoad("http://webapi.amap.com/ui/1.0/main.js");

      this.amapReady = true;
      // console.log("parent 1 after");
    }
    // console.log("parent 2");
  },
  methods: {}
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
</style>