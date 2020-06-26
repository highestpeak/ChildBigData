<template>
  <div class="m-map">
    <div id="mapCircleValue-container" class="map">正在加载数据 ...</div>
  </div>
</template>
<script>
// 高德地图整合 https://github.com/zuley/vue-gaode
import remoteLoad from "@/utils/remoteLoad.js";
import { MapKey, MapCityName } from "@/api.js";
export default {
  name: "MapCircleValue",
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
  created() {
    this.initMap();
  },
  methods: {
    // 实例化地图
    initMap() {
      // 加载PositionPicker，loadUI的路径参数为模块名中 'ui/' 之后的部分
      let AMapUI = (this.AMapUI = window.AMapUI);
      let AMap = (this.AMap = window.AMap);
      // 注意AMap其他用法 AMap.service AMap.plugin
      AMapUI.loadUI(["overlay/SimpleMarker"], function(SimpleMarker) {
        var circleMap = new AMap.Map("mapCircleValue-container", {
          zoom: 5,
          cityName: MapCityName,
          mapStyle: "amap://styles/fresh"
        });
        var capitals = [
          [100.340417, 27.376994],
          [108.426354, 37.827452],
          [113.392174, 31.208439],
          [100.340417, 27.376994]
        ];
        var iconStyles = SimpleMarker.getBuiltInIconStyles();
        console.log(iconStyles);
        var circleStyle =
          '<div style="width: 100%;">\
        <div style="width:70px;height:70px;background-color:#127681;opacity:0.5;border-radius:50%;"></div></div>';
        for (var i = 0; i < capitals.length; i += 1) {
          // https://lbs.amap.com/api/amap-ui/reference-amap-ui/overlay/simplemarker
          new SimpleMarker({
            iconLabel: {
              innerHTML: '<div style="margin-top:15px;">武汉1780￥</div>',
              style: {
                color: "#f3c623" //设置文字颜色
              }
            },
            //直接使用dom节点
            // iconStyle:  '<div style="background:red;width:20px;height:60px;"></div>',
            iconStyle: circleStyle,

            //设置基点偏移
            offset: new AMap.Pixel(-50, -50),

            map: circleMap,
            showPositionPoint: true,
            position: capitals[i]
          });

          //   var circleMarker = new AMap.SimpleMarker({
          //       iconLabel: "1222",
          //     map: circleMap,
          //     position: capitals[i],
          //     showPositionPoint: true,
          //     zIndex: 100
          //   });
          //   circleMarker.setMap(circleMap);
        }
      });
    }
  }
};
</script>

<style>
</style>