<template>
  <div class="m-map">
    <div id="js-container" class="map">正在加载数据 ...</div>
  </div>
</template>
<script>
// 这是一个高德地图使用示例说明文件
// 高德地图整合 https://github.com/zuley/vue-gaode
import remoteLoad from "@/utils/remoteLoad.js";
import { MapKey, MapCityName } from "@/api.js";
export default {
  name: "FlightMap",
  props: ["lat", "lng"],
  components: {},
  data() {
    return {
      searchKey: "",
      placeSearch: null,
      dragStatus: false,
      AMapUI: null,
      AMap: null
    };
  },
  async created() {
    // 已载入高德地图API，则直接初始化地图
    if (window.AMap && window.AMapUI) {
      this.initMap();
      // 未载入高德地图API，则先载入API再初始化
    } else {
      await remoteLoad(`http://webapi.amap.com/maps?v=1.3&key=${MapKey}`);
      await remoteLoad("http://webapi.amap.com/ui/1.0/main.js");
      this.initMap();
    }
  },
  methods: {
    // 实例化地图
    initMap() {
      // 加载PositionPicker，loadUI的路径参数为模块名中 'ui/' 之后的部分
      let AMapUI = (this.AMapUI = window.AMapUI);
      let AMap = (this.AMap = window.AMap);
      // 注意AMap其他用法 AMap.service AMap.plugin

      // loadui 文档 https://lbs.amap.com/api/amap-ui/advanced
      AMapUI.loadUI(["misc/PositionPicker"], PositionPicker => {
        // 地图配置mapconfig https://lbs.amap.com/api/javascript-api/guide/abc/quickstart
        let mapConfig = {
          zoom: 16,
          cityName: MapCityName
        };
        if (this.lat && this.lng) {
          mapConfig.center = [this.lng, this.lat];
        }
        new AMap.Map("js-container", mapConfig);

        console.log(AMap);
        console.log(PositionPicker);
      });
    }
  }
};
</script>

<style>
.m-map {
  min-width: 500px;
  min-height: 300px;
  position: relative;
}
.m-map .map {
  width: 100%;
  height: 100%;
}
.m-map .search {
  position: absolute;
  top: 10px;
  left: 10px;
  width: 285px;
  z-index: 1;
}
.m-map .search input {
  width: 180px;
  border: 1px solid #ccc;
  line-height: 20px;
  padding: 5px;
  outline: none;
}
.m-map .search button {
  line-height: 26px;
  background: #fff;
  border: 1px solid #ccc;
  width: 50px;
  text-align: center;
}
.m-map .result {
  max-height: 300px;
  overflow: auto;
  margin-top: 10px;
}
</style>