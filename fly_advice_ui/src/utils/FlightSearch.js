import axios from 'axios';
import { flightSearchApi } from '@/api';

import mockJson from '@/mockData/flightSearchOnlyDirectCTU-SHA.json';

function sleep(ms) {
    return new Promise(resolve => setTimeout(resolve, ms));
}
function formatTime(date, fmt) {
    var o = {
        "y+": date.getFullYear(),
        "M+": date.getMonth() + 1,                      //月份
        "d+": date.getDate(),                           //日
        "h+": date.getHours(),                          //小时
        "m+": date.getMinutes(),                        //分
        "s+": date.getSeconds(),                        //秒
        "q+": Math.floor((date.getMonth() + 3) / 3),    //季度
        "S+": date.getMilliseconds()                    //毫秒
    };
    for (var k in o) {
        if (new RegExp("(" + k + ")").test(fmt)) {
            if (k == "y+") {
                fmt = fmt.replace(RegExp.$1, ("" + o[k]).substr(4 - RegExp.$1.length));
            }
            else if (k == "S+") {
                var lens = RegExp.$1.length;
                lens = lens == 1 ? 3 : lens;
                fmt = fmt.replace(RegExp.$1, ("00" + o[k]).substr(("" + o[k]).length - 1, lens));
            }
            else {
                fmt = fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr(("" + o[k]).length)));
            }
        }
    }
    return fmt;
}
function parseMsToStr(difference_ms){
    //take out milliseconds
    difference_ms = difference_ms / 1000;
    var seconds = Math.floor(difference_ms % 60);
    difference_ms = difference_ms / 60;
    var minutes = Math.floor(difference_ms % 60);
    difference_ms = difference_ms / 60;
    var hours = Math.floor(difference_ms % 24);
    var days = Math.floor(difference_ms / 24);
    // todo: 如果天数大于一，需要编写这里的逻辑 days + '天, ' + 
    var duringStr = hours + '小时' + minutes + '分';
    return duringStr;
}
function dateBetween(date1, date2) {
    //Get 1 day in milliseconds
    var one_day = 1000 * 60 * 60 * 24;

    // Convert both dates to milliseconds
    var date1_ms = date1.getTime();
    var date2_ms = date2.getTime();

    // Calculate the difference in milliseconds
    var difference_ms = date2_ms - date1_ms;
    var orignDiffMs = difference_ms;
    return {
        str: parseMsToStr(difference_ms),
        ms: orignDiffMs,
    };
}

function parseSupplier(flightSupplier){
    var supplier = {
        lowest: Number.MAX_VALUE,
        list: []
    }
    Object.keys(flightSupplier).forEach(supplierName => {
        var price = flightSupplier[supplierName];
        supplier.lowest = supplier.lowest < price ? supplier.lowest : price;
        supplier.list.push({
            "name": supplierName,
            "price": flightSupplier[supplierName]
        })
    });
    return supplier;
}
function parseStrategyData(strategyName,strategyData,resultList){
    for (let index = 0; index <strategyData.length; index++) {
        const strategyItem = strategyData[index];
        const score = strategyItem.score;
        var sectionResult = []
        var lowestCashCost = 0;
        var totalDuringTime = 0;
        var supplierTotal = [{"name":"","price":0}]; // 所有航段的 supplier 组合 总价格
        for (let sectionIndex = 0; sectionIndex < strategyItem.flightSections.length; sectionIndex++) {
            const element = strategyItem.flightSections[sectionIndex];
            const startTime = new Date(element.startTime);
            const endTime = new Date(element.endTime);
            const flightSupplier = element.flight.supplier;
            var duringTime = dateBetween(startTime, endTime);
            totalDuringTime += duringTime.ms;

            // parse supplier
            var supplier = parseSupplier(flightSupplier);
            lowestCashCost += supplier.lowest;
            var tmpList=[]
            // 创造每一段不同 供应商价格的组合
            supplier.list.forEach(currSupplier => {
                supplierTotal.forEach(beforeSupplier => {
                    tmpList.push({
                        "name":beforeSupplier.name+currSupplier.name+"/",
                        "price":beforeSupplier.price+currSupplier.price
                    })
                });
            });
            supplierTotal = tmpList;

            // console.log(element)
            sectionResult.push({
                startPlace: element.startPlace,
                endPlace: element.endPlace,
                startTime: formatTime(startTime, "hh:dd"),
                endTime: formatTime(endTime, "hh:dd"),
                during: duringTime,

                aircraftModel: element.flight.aircraftModel,
                airline: element.flight.airline,
                flightNumber: element.flight.flightNumber,
                supplier: supplier,

                score: element.score,
            })
        }
        // console.log(supplierTotal)
        var currResult = {
            strategy: strategyName,
            sections: sectionResult,
            supplier:supplierTotal,//supplierTotal
            totalDuringTime:{
                str:parseMsToStr(totalDuringTime),
                ms:totalDuringTime
            },
            lowestCashCost:lowestCashCost,
            score: score,
        }
        resultList.push(currResult);
    }
}
async function searchFlight(searchParamInput) {
    // var flightListOrign = [];
    // var getParams = {
    //     startPlace:'CTU',
    //     endPlace:'SHA',
    //     startDate:'20200701',
    //     cabinClass:'economy',
    //     rtn:0,
    //     rtnDate:'20200702',
    //     preferDirects:0,
    //     preferTransfer:0,
    //     preferStop:0,
    //     outboundAltEnabled:0,
    //     inboundAltEnabled:0,
    //     remainingVotes:3,
    // }
    // // flightSearchApi
    // await axios.get("", {
    //         params: getParams,
    //     })
    //     .then(function (response) {
    //         flightListOrign = response.data;
    //         // console.log(flightListOrign)
    //     })
    //     .catch(function (error) {
    //         // handle error
    //         console.log(error);
    //     })
    //     .then(function () {
    //         // always executed
    //     });
    var mockData = mockJson;
    var resultList = []
    // console.log(mockData)
    parseStrategyData("DIRECT",mockData.DIRECT,resultList);
    parseStrategyData("TRANSFER",mockData.TRANSFER,resultList);
    //todo: remove here
    // await sleep(2000);
    return resultList;
}
export {
    searchFlight
}