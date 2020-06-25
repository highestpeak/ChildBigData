<template>
  <div class="m-map">
    <div id="js-container" class="map">正在加载数据 ...</div>
  </div>
</template>

<script>
// import axios from "axios";
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
  created() {
    this.initMap();
  },
  methods: {
    // 实例化地图
    initMap() {
      // 加载PositionPicker，loadUI的路径参数为模块名中 'ui/' 之后的部分
      let AMapUI = (this.AMapUI = window.AMapUI);
      let AMap = (this.AMap = window.AMap);

      // loadui 文档 https://lbs.amap.com/api/amap-ui/advanced
      AMapUI.loadUI(["misc/PathSimplifier"], PathSimplifier => {
        let mapConfig = {
          zoom: 4,
          cityName: MapCityName,
          mapStyle: "amap://styles/fresh"
        };
        if (this.lat && this.lng) {
          mapConfig.center = [this.lng, this.lat];
        }
        let map = new AMap.Map("js-container", mapConfig);

        // 加载
        // 画线文档 https://lbs.amap.com/api/amap-ui/reference-amap-ui/mass-data/pathsimplifier
        var pathSimplifierIns = new PathSimplifier({
          zIndex: 100,
          map: map, //所属的地图实例
          getPath: function(pathData, pathIndex) {
            //   console.log(pathIndex)
            //返回轨迹数据中的节点坐标信息，[AMap.LngLat, AMap.LngLat...] 或者 [[lng|number,lat|number],...]
            return pathData.path;
          },
          //todo: 返回鼠标悬停时显示的信息
          getHoverTitle: function(pathData, pathIndex, pointIndex) {
            if (pointIndex >= 0) {
              //鼠标悬停在某个轨迹节点上
              return (
                pathData.name +
                "，点:" +
                pointIndex +
                "/" +
                pathData.path.length
              );
            }
            //鼠标悬停在节点之间的连线上
            return pathData.name + "，点数量" + pathData.path.length;
          },
          autoSetFitView: false,
          renderOptions: {
            //轨迹线的样式
            pathLineStyle: {
              strokeStyle: "#111d5e",
              lineWidth: 4,
              dirArrowStyle: true
            },
            pathLineHoverStyle: {},
            pathLineSelectedStyle: {},
            pathNavigatorStyle: {
              pathLinePassedStyle: {}
            }
          }
        });
        //这里构建两条简单的轨迹，仅作示例

        // 返回pathSimplifierIns实例后可以重新调用 setdata
        // todo: 轨迹数组
        pathSimplifierIns.setData([
          {
            name: "轨迹0",
            path: [
              [100.340417, 27.376994],
              [108.426354, 37.827452],
              [113.392174, 31.208439],
              [100.340417, 27.376994]
              //   [124.905846, 42.232876],
            ]
          },
          {
            name: "大地线",
            //创建一条包括100个插值点的大地线
            path: PathSimplifier.getGeodesicPath(
              [116.405289, 39.904987],
              [87.61792, 43.793308],
              100
            )
          }
        ]);

        // todo:巡航器数组
        //创建一个巡航器
        var navg0 = pathSimplifierIns.createPathNavigator(
          1, //关联第1条轨迹
          {
            loop: true, //循环播放
            speed: 1000000
          }
        );

        navg0.start();

        var navg1 = pathSimplifierIns.createPathNavigator(
          0, //关联第1条轨迹
          {
            loop: true, //循环播放
            speed: 5000000
            // todo: 根据航班的时间来调整速率
          }
        );

        navg1.start();

        // 返回选中的轨迹线 pathSimplifierIns.getSelectedPathData()
      });
    }
  }
};
</script>

<style>
.m-map {
  min-width: 800px;
  min-height: 500px;
  position: relative;
}
.m-map .map {
  width: 100%;
  height: 100%;
}
</style>