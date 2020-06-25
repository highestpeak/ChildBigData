<template>
  <div>
    <MapCircleValue v-if="amapReady" class="mapbox" />
    <FlightMap v-if="amapReady" class="mapbox" />
    <DistructArea v-if="amapReady" class="mapbox" />
  </div>
</template>
<script>
// 每天航班最繁忙的时间段是哪些？
// 


// 高德地图整合 https://github.com/zuley/vue-gaode
import FlightMap from "@/components/amap/p2pFlightMap.vue";
import DistructArea from "@/components/amap/DistructArea.vue";
import MapCircleValue from "@/components/amap/MapCircleValue.vue";
import remoteLoad from "@/utils/remoteLoad.js";
import { MapKey, MapCityName } from "@/api.js";
export default {
  name: "MapAnalysis",
  components: {
    FlightMap,
    DistructArea,
    MapCircleValue,
  },
  data() {
    return {
        amapReady:false,
    };
  },
  async created() {
    if (window.AMap && window.AMapUI) {
      // 未载入高德地图API，则先载入API再初始化
      this.amapReady = true;
    } else {
      console.log("parent 1");
      await remoteLoad(`http://webapi.amap.com/maps?v=1.4.15&key=${MapKey}`);
      await remoteLoad("http://webapi.amap.com/ui/1.0/main.js");
      
      this.amapReady = true;
      console.log("parent 1 after");
    }
    console.log("parent 2");
  },
  methods: {

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
</style>