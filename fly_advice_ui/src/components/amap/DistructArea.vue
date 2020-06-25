<template>
  <div class="m-map" style="width:1000px;height:600px;">
    <div id="distructArea-container" class="map">正在加载数据 ...</div>
  </div>
</template>

<script>
import remoteLoad from "@/utils/remoteLoad.js";
import { MapKey, MapCityName } from "@/api.js";
export default {
  name: "DistructArea",
  components: {},
  data() {
    return {
      AMapUI: null,
      AMap: null,
      currentAdcode: null,
      currentLngLat: null
    };
  },
  created() {
    this.initMap();
  },
  methods: {
    //随机生产点
    createPoints(center, num) {
      var data = [];
      for (var i = 0, len = num; i < len; i++) {
        data.push({
          position: [
            center.getLng() +
              (Math.random() > 0.5 ? 1 : -1) * Math.random() * 30,
            center.getLat() +
              (Math.random() > 0.5 ? 1 : -1) * Math.random() * 20
          ]
        });
      }
      return data;
    },

    // 实例化地图
    initMap() {
      var adcode = [
        { adcode: "110000", name: "北京市" },
        { adcode: "120000", name: "天津市" },
        { adcode: "130000", name: "河北省" },
        { adcode: "140000", name: "山西省" },
        { adcode: "150000", name: "内蒙古自治区" },

        { adcode: "210000", name: "辽宁省" },
        { adcode: "220000", name: "吉林省" },
        { adcode: "230000", name: "黑龙江省" },

        { adcode: "310000", name: "上海市" },
        { adcode: "320000", name: "江苏省" },
        { adcode: "330000", name: "浙江省" },
        { adcode: "340000", name: "安徽省" },
        { adcode: "350000", name: "福建省" },
        { adcode: "360000", name: "江西省" },
        { adcode: "370000", name: "山东省" },

        { adcode: "410000", name: "河南省" },
        { adcode: "420000", name: "湖北省" },
        { adcode: "430000", name: "湖南省" },
        { adcode: "440000", name: "广东省" },
        { adcode: "450000", name: "广西壮族自治区" },
        { adcode: "460000", name: "海南省" },

        { adcode: "500000", name: "重庆市" },
        { adcode: "510000", name: "四川省" },
        { adcode: "520000", name: "贵州省" },
        { adcode: "530000", name: "云南省" },
        { adcode: "540000", name: "西藏自治区" },

        { adcode: "610000", name: "陕西省" },
        { adcode: "620000", name: "甘肃省" },
        { adcode: "630000", name: "青海省" },
        { adcode: "640000", name: "宁夏回族自治区" },
        { adcode: "650000", name: "新疆维吾尔自治区" },

        { adcode: "710000", name: "台湾省" },
        { adcode: "810000", name: "香港特别行政区" },
        { adcode: "820000", name: "澳门特别行政区" }
      ];
      // 加载PositionPicker，loadUI的路径参数为模块名中 'ui/' 之后的部分
      let AMapUI = (this.AMapUI = window.AMapUI);
      let AMap = (this.AMap = window.AMap);
      var currentAdcode = this.currentAdcode;
      var currentLngLat = this.currentLngLat;

      // loadui 文档 https://lbs.amap.com/api/amap-ui/advanced
      // districtCluster 文档 https://developer.amap.com/api/amap-ui/reference-amap-ui/geo/district-explorer
      AMapUI.loadUI(["geo/DistrictCluster"], DistrictCluster => {
        let mapConfig = {
          zoom: 5,
          cityName: MapCityName,
          mapStyle: "amap://styles/fresh"
        };
        if (this.lat && this.lng) {
          mapConfig.center = [this.lng, this.lat];
        }
        let map = new AMap.Map("distructArea-container", mapConfig);

        var distCluster = new DistrictCluster({
          map: map, //所属的地图实例
          zIndex: 11,
          // todo: 把省份代码做成一个字典存起来，方便之后删除不属于 列表中的省份
          excludedAdcodes: [
            // 110000,120000,130000,
            140000,
            150000,
            210000,
            220000,
            230000,
            310000,
            // 320000,330000,340000,
            350000,
            360000,
            370000,
            410000,
            420000,
            430000,
            440000,
            450000,
            460000,
            // 500000,510000,520000,
            530000,
            540000,
            610000,
            620000,
            630000,
            640000
            // 650000,710000,810000,
            // 820000
          ],
          getPosition: function(item) {
            return item.position;
          },
          renderOptions: {
            //基础样式
            featureStyle: {
              fillStyle: "rgba(102,170,0,0.5)", //填充色
              lineWidth: 2, //描边线宽
              strokeStyle: "#1b6ca8", //描边色
              //鼠标Hover后的样式
              hoverOptions: {
                fillStyle: "rgba(255,255,255,0.2)"
              }
            },
            //特定区划级别的默认样式
            featureStyleByLevel: {
              //全国
              country: {
                fillStyle: "#0e9aa7"
              },
              //省
              province: {
                fillStyle: "#35d0ba"
              },
              //市
              city: {
                fillStyle: "#ffcd3c"
              },
              //区县
              district: {
                fillStyle: "#fe8a71"
              }
            },
            //直接定义某写区划面的样式
            // 可以根据数量 来改变 省份地域颜色
            getFeatureStyle: function(feature, dataItems) {
              if (dataItems.length > 3000) {
                return {
                  fillStyle: "#f54291"
                };
              } else if (dataItems.length > 1000) {
                return {
                  fillStyle: "orange"
                };
              }

              return {};
            }

            // getClusterMarker: function(feature, dataItems, recycledMarker) {
            //   return null;
            // }
          }
        });

        //随机创建一批点，仅作示意
        // todo:循环某一个地点的经纬度数量放进去
        var test = { position: [113.864691, 22.942327] };
        var data = [];
        for (let index = 0; index < 30000; index++) {
          data.push({ position: [113.336586, 33.729581] });
          data.push({ position: [104.137953, 30.784276] });
          data.push({ position: [114.141516, 23.159282] });
          data.push({ position: [120.499683, 30.042305] });
          data.push({ position: [120.487242, 32.180365] });
          data.push({ position: [108.94686, 34.362975] });
          data.push({ position: [121.299895, 31.105064] });
        }
        //设置数据
        distCluster.setData(data);
      });
    }
  }
};
</script>



<style >
/* 下面这些是 区域图的弹出 内容 不可删，因为很难找到 */

.amap-ui-district-cluster-container {
  cursor: default;
  /* -webkit-backface-visibility: hidden;
  -webkit-transform: translateZ(0) scale(1, 1); */
}

.amap-ui-district-cluster-container canvas {
  position: absolute;
}

.amap-ui-district-cluster-container .amap-ui-hide {
  display: none !important;
}

.amap-ui-district-cluster-container .overlay-title,
.amap-ui-district-cluster-marker {
  color: #555;
  background-color: #fffeef;
  font-size: 12px;
  white-space: nowrap;
  position: absolute;
}

.amap-ui-district-cluster-container .overlay-title {
  padding: 2px 6px;
  display: inline-block;
  z-index: 99999;
  border: 1px solid #7e7e7e;
  border-radius: 2px;
}

.amap-ui-district-cluster-container .overlay-title:after,
.amap-ui-district-cluster-container .overlay-title:before {
  content: "";
  display: block;
  position: absolute;
  margin: auto;
  width: 0;
  height: 0;
  border: solid transparent;
  border-width: 5px;
}

.amap-ui-district-cluster-container .overlay-title.left {
  transform: translate(10px, -50%);
}

.amap-ui-district-cluster-container .overlay-title.left:before {
  top: 5px;
}

.amap-ui-district-cluster-container .overlay-title.left:after {
  left: -9px;
  top: 5px;
  border-right-color: #fffeef;
}

.amap-ui-district-cluster-container .overlay-title.left:before {
  left: -10px;
  border-right-color: #7e7e7e;
}

.amap-ui-district-cluster-container .overlay-title.top {
  transform: translate(-50%, -130%);
}

.amap-ui-district-cluster-container .overlay-title.top:before {
  left: 0;
  right: 0;
}

.amap-ui-district-cluster-container .overlay-title.top:after {
  bottom: -9px;
  left: 0;
  right: 0;
  border-top-color: #fffeef;
}

.amap-ui-district-cluster-container .overlay-title.top:before {
  bottom: -10px;
  border-top-color: #7e7e7e;
}

.amap-ui-district-cluster-marker {
  border: 1px solid #8e8e8e;
  width: auto;
  height: 22px;
  border-radius: 5px 5px 5px 0;
  left: 0;
  top: 0;
}

.amap-ui-district-cluster-marker:after,
.amap-ui-district-cluster-marker:before {
  content: "";
  display: block;
  position: absolute;
  width: 0;
  height: 0;
  border: solid rgba(0, 0, 0, 0);
  border-width: 6px;
  left: 13px;
}

.amap-ui-district-cluster-marker:after {
  bottom: -12px;
  border-top-color: #fffeef;
}

.amap-ui-district-cluster-marker:before {
  bottom: -13px;
  border-top-color: #8e8e8e;
}

.amap-ui-district-cluster-marker span {
  vertical-align: middle;
  padding: 3px 5px;
  display: inline-block;
  height: 16px;
  line-height: 16px;
}

.amap-ui-district-cluster-marker-title {
  border-radius: 5px 0 0 0;
}

.amap-ui-district-cluster-marker-body {
  background-color: #dc3912;
  color: #fff;
  border-radius: 0 5px 5px 0;
}

/* 国：例如 china */
.amap-ui-district-cluster-marker.level_country
  .amap-ui-district-cluster-marker-body {
  background-color: #4f8a8b !important;
}

/* 省：例如 四川省 */
.amap-ui-district-cluster-marker.level_province
  .amap-ui-district-cluster-marker-body {
  background-color: #5a3d55 !important;
}

/* 市：例如 成都市 */
.amap-ui-district-cluster-marker.level_city
  .amap-ui-district-cluster-marker-body {
  background-color: #990099;
}

/* 区：例如 成华区 */
.amap-ui-district-cluster-marker.level_district
  .amap-ui-district-cluster-marker-body {
  background-color: #dd4477;
}
</style>