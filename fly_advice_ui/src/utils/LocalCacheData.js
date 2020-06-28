import { airport as airportsCached } from "@/utils/airport"

var districts = [
    { 'name': '北京市', 'center': '116.407394,39.904211' },
    { 'name': '天津市', 'center': '117.200983,39.084158' },
    { 'name': '河北省', 'center': '114.530235,38.037433' },
    { 'name': '山西省', 'center': '112.562678,37.873499' },
    { 'name': '内蒙古自治区', 'center': '111.76629,40.81739' },
    { 'name': '辽宁省', 'center': '123.431382,41.836175' },
    { 'name': '吉林省', 'center': '125.32568,43.897016' },
    { 'name': '黑龙江省', 'center': '126.661665,45.742366' },
    { 'name': '上海市', 'center': '121.473662,31.230372' },
    { 'name': '江苏省', 'center': '118.762765,32.060875' },
    { 'name': '浙江省', 'center': '120.152585,30.266597' },
    { 'name': '安徽省', 'center': '117.329949,31.733806' },
    { 'name': '福建省', 'center': '119.295143,26.100779' },
    { 'name': '江西省', 'center': '115.81635,28.63666' },
    { 'name': '山东省', 'center': '117.019915,36.671156' },
    { 'name': '河南省', 'center': '113.753394,34.765869' },
    { 'name': '湖北省', 'center': '114.341745,30.546557' },
    { 'name': '湖南省', 'center': '112.9836,28.112743' },
    { 'name': '广东省', 'center': '113.26641,23.132324' },
    { 'name': '广西壮族自治区', 'center': '108.327546,22.815478' },
    { 'name': '海南省', 'center': '110.349228,20.017377' },
    { 'name': '重庆市', 'center': '106.551643,29.562849' },
    { 'name': '四川省', 'center': '104.075809,30.651239' },
    { 'name': '贵州省', 'center': '106.70546,26.600055' },
    { 'name': '云南省', 'center': '102.710002,25.045806' },
    { 'name': '西藏自治区', 'center': '91.117525,29.647535' },
    { 'name': '陕西省', 'center': '108.954347,34.265502' },
    { 'name': '甘肃省', 'center': '103.826447,36.05956' },
    { 'name': '青海省', 'center': '101.780268,36.620939' },
    { 'name': '宁夏回族自治区', 'center': '106.259126,38.472641' },
    { 'name': '新疆维吾尔自治区', 'center': '87.627704,43.793026' },
    { 'name': '台湾省', 'center': '121.509062,25.044332' },
    { 'name': '香港特别行政区', 'center': '114.171203,22.277468' },
    { 'name': '澳门特别行政区', 'center': '113.543028,22.186835' }
];
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
/**省份行政码查询 */
function findProvinceCode(name) {
    for (let index = 0; index < adcode.length; index++) {
        const element = adcode[index];
        if (element.name === name) {
            return element;
        }
    }
}
function findProvincesCode(names) {
    var adcodeList = []
    names.forEach(name => {
        adcode.forEach(element => {
            if (element.name === name) {
                adcodeList.push(element.adcode)
                return;
            }
        });
    });
    return adcodeList;
}
function findAdCodeExcept(codes){
    var adcodeList = []
    codes.forEach(code => {
        adcode.forEach(element => {
            if (element.adcode != code) {
                adcodeList.push(element.adcode)
                return;
            }
        });
    });
    return adcodeList;
}
/**省份行政中心的经纬度查询 */
function findProvinceCenter(name) {
    for (let index = 0; index < districts.length; index++) {
        const element = districts[index];
        if (element['name'] === name) {
            return element;
        }
    }
}
function findProvincesCenter(names) {
    var centerList = []
    names.forEach(name => {
        centerList.push(findProvinceCenter(name))
    });
    return centerList;
}
/**机场相关查询 */
function findInfoOfAirport(field,airportName){
    for (let index = 0; index < airportsCached.length; index++) {
        const airport = airportsCached[index];
        if(airport[field]===airportName){
            return airport;
        }
    }
}
function findInfoOfAirports(names) {
    var airportList = []
    names.forEach(name => {
        airportList.push(findInfoOfAirport('name',name))
    });
    return airportList;
}
export {
    airportsCached,
    districts, adcode,
    /**省份行政码查询 */
    findProvincesCode, findProvinceCode,findAdCodeExcept,
    /**省份行政中心的经纬度查询 */
    findProvinceCenter, findProvincesCenter,
    /**机场相关查询 */
    findInfoOfAirports,findInfoOfAirport,
}