import remoteLoad from "@/utils/remoteLoad.js";
import { findAdCodeExcept, findProvincesCode } from "@/utils/LocalCacheData.js";
import { MapKey, MapCityName } from "@/api.js";

function mergeConfig(fromConfig, toConfig) {
    if (fromConfig != null) {
        Object.keys(fromConfig).forEach(function (key) {
            var value = fromConfig[key];
            // console.log([key,value])
            if (Object.keys(toConfig).includes(key)) {
                if (toConfig[key] != value) {
                    toConfig[key] = value;
                }
            } else {
                toConfig[key] = value;
            }
        });
    }
}

// MapUI、AmapMap
function initMapUI(VUE, AmapUIName, AMapUIFuc) {
    // 加载PositionPicker，loadUI的路径参数为模块名中 'ui/' 之后的部分
    let AMapUI = (VUE.AMapUI = window.AMapUI);
    AMapUI.loadUI(AmapUIName, AMapUIFuc);
}

function getAmapMap(VUE, containerId, mapConfig = null) {
    // let map = new AMap.Map(id, mapConfig);

    let defaultMapConfig = {
        zoom: 4,
        cityName: MapCityName,
        mapStyle: "amap://styles/fresh"
    };
    mergeConfig(mapConfig, defaultMapConfig);
    // if(mapConfig!=null){
    //     // console.log("defaultMapConfig")
    //     Object.keys(mapConfig).forEach(function(key) {
    //         var value=mapConfig[key];
    //         // console.log([key,value])
    //         if (Object.keys(defaultMapConfig).includes(key)) {
    //             if (defaultMapConfig[key] != value) {
    //                 defaultMapConfig[key] = value;
    //             }
    //         }else{
    //             defaultMapConfig[key] = value;
    //         }
    //     });
    // }
    // console.log(defaultMapConfig)
    if (VUE.lat && VUE.lng) {
        mapConfig.center = [this.lng, this.lat];
    }
    let AMap = (VUE.AMap = window.AMap);
    let map = new AMap.Map(containerId, defaultMapConfig);
    return map;
}

// CircleValueMap
/**
 * 构建 圆圈中 含有文字值的地图
 * points example: [ ..., [100.340417, 27.376994],... ]
 * @param {Array(Object{String text,[lot,lat] pos})} points 带有文字和坐标的点
 * @param {AMap.Map mapInstance} instance of Amap
 */
function buildCircleValueMap(points, mapInstance, AMap, SimpleMarker) {
    var circleStyle =
        '<div style="width: 100%;">\
           <div style="width:70px;height:70px;\
                    background-color:#127681;opacity:0.5;border-radius:50%;">\
           </div>\
        </div>';
    var simpleMarkerList = []
    for (var i = 0; i < points.length; i += 1) {
        var currMarker = new SimpleMarker({
            iconLabel: {
                innerHTML: '<div style="margin-top:15px;">' + points.text + '￥</div>',
                style: {
                    color: "#f3c623"
                }
            },
            iconStyle: circleStyle,
            //设置基点偏移--不遮挡坐标点的目标
            offset: new AMap.Pixel(-15, -15),
            map: mapInstance,
            showPositionPoint: true,
            position: points.pos
        });
        simpleMarkerList.push(currMarker);
    }
    return simpleMarkerList;
}

// P2PFlightLineMap
// https://lbs.amap.com/api/amap-ui/demos/amap-ui-pathsimplifier/adjust-navig-style
/**
 * 绘制点对点折线图,飞行路线图
 * @param {Amap.Map} mapInstance amap实例
 */
function buildP2PFlightLineMap(mapInstance, PathSimplifier) {
    var pathSimplifierInstance = new PathSimplifier({
        zIndex: 100,
        map: mapInstance,
        getPath: function (pathData, pathIndex) {
            //返回轨迹数据中的节点坐标信息，[AMap.LngLat, AMap.LngLat...] 或者 [[lng|number,lat|number],...]
            return pathData.path;
        },
        //todo: 返回鼠标悬停时显示的信息
        getHoverTitle: function (pathData, pathIndex, pointIndex) {
            if (pointIndex >= 0) {
                //鼠标悬停在某个轨迹节点上
                return pathData.name;
            }
            //鼠标悬停在节点之间的连线上
            return pathData.name;
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
    return pathSimplifierInstance;
}

/**
 * 折线航线，直线
 * @param {string} name 航线名称
 * @param {[ ...,[lot,lat],... ]} pathData 航线转折点的 经纬度数组
 * @param {[]} resultArray 目标构建数组
 */
function buildP2PPolyData(name, pathData, resultArray) {
    resultArray.push({
        name: name,
        path: pathData
    });
    return resultArray;
}

/**
 * 曲线航线，弧线，大地线
 * @param {string} name 航线名称
 * @param { {lat:xx,lot:xx} } start 航线起始点
 * @param {*} end 
 * @param {[]} resultArray 目标数组
 */
function buildP2PArcData(name, start, end, PathSimplifier, resultArray) {
    resultArray.push({
        name: name,
        //创建一条包括100个插值点的大地线
        path: PathSimplifier.getGeodesicPath(
            [start.lot, start.lat],
            [end.lot, end.lat],
            100
        )
    });
    return resultArray;
}

/**
 * 巡航器动画
 * @param {int} index 目标航线在 数据数组 中的index
 * @param {int} speedx 相对速率
 * @param {bool} isloop 是否循环
 */
function buildP2PNavg(index, speedx, isloop = true, pathSimplifierIns) {
    var navg = pathSimplifierIns.createPathNavigator(
        index, //关联第 index 条轨迹
        {
            loop: isloop, //循环播放
            speed: speedx
        }
    );
    //   navg0.start();
    return navg;
}

// DistrictCluster
function buildDistrictCluster(mapInstance, DistrictCluster, selectAdcodes = [], selectProvince = []) {
    if (selectProvince.length != 0) {
        Array.prototype.push.apply(selectAdcodes, findProvincesCode(selectProvince))
    }
    var excludedAdcodes = findAdCodeExcept(selectAdcodes);
    var distCluster = new DistrictCluster({
        map: mapInstance, //所属的地图实例
        zIndex: 11,
        excludedAdcodes: excludedAdcodes,
        getPosition: function (item) {
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
                country: { fillStyle: "#0e9aa7" },
                //省
                province: { fillStyle: "#35d0ba" },
                //市
                city: { fillStyle: "#ffcd3c" },
                //区县
                district: { fillStyle: "#fe8a71" }
            },
            // 可以根据数量 来改变 省份地域颜色
            getFeatureStyle: function (feature, dataItems) {
                if (dataItems.length > 3000) {
                    return { fillStyle: "#f54291" };
                } else if (dataItems.length > 1000) {
                    return { fillStyle: "orange" };
                }
                return {};
            }
        }
    });
    return distCluster;
}

function buildDistrictData(lot, lat, nums, resultArray) {
    for (let index = 0; index < nums; index++) {
        resultArray.push({ position: [lot, lat] });
    }
    return resultArray;
}

// test points
function generateTestPoints(center, num) {
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
}
export {
    initMapUI, getAmapMap,
    buildCircleValueMap,
    buildP2PFlightLineMap, buildP2PPolyData, buildP2PArcData, buildP2PNavg,
    buildDistrictCluster, buildDistrictData,
    generateTestPoints,
}