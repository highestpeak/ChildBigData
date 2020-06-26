function mergeOption(fromConfig, toConfig) {
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

function initEchart(echartInstance,id,option){
    var myChart = echartInstance.init(document.getElementById(id));
    myChart.setOption(option);
}

// 曲线图
/**
 * 曲线图
 * @param {array} xData x轴类别数据
 * @param {array} series 曲线数组
 * @param {boolean} dataZoom 是否含有缩放
 */
function buildLineChartOption(xData, series, dataZoom = false,grid=null) {
    var option = {
        xAxis: {
            type: 'category',
            // boundaryGap: false,
            data: xData
        },
        yAxis: {
            type: 'value'
        },
        grid: { // 控制图相对于 canvas 画布的四周边框
            left: 40,
            top: 20,
            right: 10,
            bottom: 30,
        },
        // tooltip: { // 控制有 横纵轴垂直对齐线🔨
        //     trigger: 'axis',
        //     axisPointer: {
        //         type: 'cross',
        //         label: {
        //             backgroundColor: '#6a7985'
        //         }
        //     }
        // },
        series: series
    };
    if(grid){
        option.grid=grid;
    }
    if (dataZoom) {
        option.dataZoom = [{
            type: 'inside',
            start: 0,
            end: 10,
        }, {
            height:20,
            bottom:"10%",
            start: 0,
            end: 10,
            handleIcon: 'M10.7,11.9v-1.3H9.3v1.3c-4.9,0.3-8.8,4.4-8.8,9.4c0,5,3.9,9.1,8.8,9.4v1.3h1.3v-1.3c4.9-0.3,8.8-4.4,8.8-9.4C19.5,16.3,15.6,12.2,10.7,11.9z M13.3,24.4H6.7V23h6.6V24.4z M13.3,19.6H6.7v-1.4h6.6V19.6z',
            handleSize: '80%',
            handleStyle: {
                color: '#fff',
                shadowBlur: 3,
                shadowColor: 'rgba(0, 0, 0, 0.6)',
                shadowOffsetX: 2,
                shadowOffsetY: 2
            }
        }];
    }
    return option;
}

/**
 * 构建曲线的 y数据
 * @param {string} name 曲线名称
 * @param {array} points 曲线y轴数据点
 * @param {boolean} markPoint 是否显示最低最高值标记
 * @param {boolean} markLine 是否显示平均值线
 */
function buildSeries(name, points, markPoint = false, markLine = false) {
    var series = {
        name: name,
        stack: name,
        data: points,
        type: 'line',
        smooth: false,
        sampling: 'average',
        // areaStyle: {},
        label: {
            normal: {
                show: true,
                position: 'bottom'
            }
        },
    };
    if (markPoint) {
        series.markPoint = {
            data: [
                { type: 'min', name: '周最低' },
                { type: 'max', name: '周最低' },
            ]
        };
    }
    if (markLine) {
        series.markLine = {
            data: [
                { type: 'average', name: '平均值' }
            ]
        };
    }
    return series;
}

// 雷达图
function buildRadarOption(){
    var option = {
        tooltip: {},
        grid: { // 控制图相对于 canvas 画布的四周边框
            left: 10,
            top: 20,
            right: 50,
            bottom: 30,
        },
        legend: {
            data: ['预算分配（Allocated Budget）', '实际开销（Actual Spending）']
        },
        radar: {
            // shape: 'circle',
            name: {
                textStyle: {
                    color: '#fff',
                    backgroundColor: '#999',
                    borderRadius: 3,
                    padding: [3, 5]
                }
            },
            indicator: [
                { name: '销售（sales）', max: 6500},
                { name: '管理（Administration）', max: 16000},
                { name: '信息技术（Information Techology）', max: 30000},
                { name: '客服（Customer Support）', max: 38000},
                { name: '研发（Development）', max: 52000},
                { name: '市场（Marketing）', max: 25000}
            ]
        },
        series: [{
            name: '预算 vs 开销（Budget vs spending）',
            type: 'radar',
            // areaStyle: {normal: {}},
            data: [
                {
                    value: [4300, 10000, 28000, 35000, 50000, 19000],
                    name: '预算分配（Allocated Budget）'
                },
                {
                    value: [5000, 14000, 28000, 31000, 42000, 21000],
                    name: '实际开销（Actual Spending）'
                }
            ]
        }]
    };
    return option;
}



// 热力图
function buildHeatMapOption(data){
    var option = {
        title: {
            top: 20,
            left: 'center',
            text: '2016年某人每天的步数'
        },
        tooltip: {},
        visualMap: {
            min: 0,
            max: 10000,
            calculable: true,
            orient: 'horizontal',
            left: 'center',
            top: 50,
            textStyle: {
                color: '#000'
            }
        },
        calendar: {
            top: 120,
            left: 30,
            right: 30,
            cellSize: ['auto', 13],
            range: '2016',
            itemStyle: {
                borderWidth: 0.5
            },
            yearLabel: {show: false}
        },
        series: {
            type: 'heatmap',
            coordinateSystem: 'calendar',
            data: data
        }
    };
    return option;
}

export {
    initEchart,
    buildLineChartOption,buildSeries,
    buildRadarOption,
    buildHeatMapOption,
}