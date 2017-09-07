/**
 * Created by gaokangkang on 2017/8/30.
 * 样本流向图 折线图
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('gridMultipleChart',  gridMultipleChart);

    gridMultipleChart.$inject  =  [];

    function  gridMultipleChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                reloadData: "&",
                enlarged: "&",
                data: "=",
                type: "=",
                typeContent: "="
            },
            template: '<div id="line" style="width:100%;height: 350px"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }

                var daySampleData = scope.data;
                var timeData = [];
                var countOfInSample = [];
                var countOfOutSample = [];
                _.forEach(daySampleData,function (data) {
                    timeData.push(data.optDate);
                    countOfInSample.push(data.countOfInSample);
                    countOfOutSample.push(data.countOfOutSample);
                });
                var option = {
                    title: {
                        text: '样本流向图('+scope.typeContent+")",
                        subtext: '',
                        x: 'center'
                    },
                    tooltip: {
                        trigger: 'axis',//坐标轴触发，主要在柱状图，折线图等会使用类目轴的图表中使用
                        axisPointer: {
                            animation: false
                        }
                    },
                    legend: {
                        data:['入库样本','出库样本'],
                        x: 'left'
                    },
                    toolbox: {
                        feature: {
                            myMonth: {
                                show: true,
                                title: '月视图',
                                icon: "path://M58.8,50.2c0,3.6-0.8,6.2-2.3,7.7s-4,2.3-7.5,2.3h-9.5c-2.9,0-4.9-0.7-6.1-2c-1.2-1.4-1.8-3.4-1.8-6.1\
                                c0-2.7,0.4-4.6,1.1-5.8c0.1-0.2,0.1-0.3,0.1-0.4s-0.2-0.2-0.6-0.2h-4.4c-0.2,0-0.4,0.1-0.5,0.3c-0.7,2.9-1.5,5.1-2.4,6.6\
                                c-0.8,1.5-1.7,2.9-2.6,4.2C21.4,58,20.3,59,19,59.6c-1.3,0.6-2.7,0.9-4,0.9c-2.1,0-4.1-0.8-6.1-2.4c-2-1.6-3-3.3-3-5\
                                c0-1.7,0.5-3.6,1.5-5.5c1.7-3.2,2.9-6.6,3.7-10.2c0.7-3.6,1.1-8.3,1.1-14.2v-8c0-4.1,0.8-7,2.4-8.8c1.6-1.8,4.2-2.6,7.8-2.6h26\
                                c4.1,0,6.8,0.8,8.2,2.5c1.4,1.7,2.1,4.5,2.1,8.5V50.2z M56.5,50.2V14.7c0-3.2-0.5-5.4-1.6-6.7c-1.1-1.3-3.2-2-6.4-2h-26\
                                c-2.8,0-4.8,0.7-6.1,2.1c-1.3,1.4-1.9,3.7-1.9,7v8c0,5.9-0.4,10.8-1.2,14.7c-0.8,3.8-2.1,7.4-3.8,10.8c-0.7,1.4-1.1,2.7-1.1,4\
                                c0,1.3,0.7,2.5,2.2,3.8c1.5,1.2,2.9,1.9,4.4,1.9s2.8-0.4,3.9-1.1c1.1-0.8,2.3-2.2,3.5-4.3c1.2-2.1,2.2-4.6,2.9-7.4\
                                c0.2-0.9,0.5-1.4,0.7-1.6c0.2-0.2,0.5-0.3,0.8-0.3h6.4c0.4,0,1-0.2,1.8-0.6c0.8-0.5,2-0.7,3.7-0.7h4.2c0.5,0,0.8,0.5,0.8,1.5\
                                c0,1-0.3,1.5-0.9,1.5h-2.7c-4,0-6,2.1-6,6.2c0,2.5,0.4,4.2,1.2,5.2c0.8,1,2.3,1.5,4.3,1.5H49c2.6,0,4.6-0.6,5.7-1.8\
                                C55.9,55,56.5,53,56.5,50.2z M42.6,30.9c0.7,0,1,0.4,1,1.1c0,0.7-0.3,1.1-1,1.1H28.2c-0.4,0-0.6-0.1-0.7-0.2\
                                c-0.1-0.2-0.2-0.4-0.2-0.8c0-0.4,0.1-0.7,0.2-0.9c0.1-0.2,0.4-0.3,0.7-0.3H42.6z M42.6,17.7c0.7,0,1,0.4,1,1.1c0,0.7-0.3,1.1-1,1.1\
                                H28.6c-0.4,0-0.6-0.1-0.7-0.2c-0.1-0.2-0.2-0.4-0.2-0.8c0-0.4,0.1-0.7,0.2-0.9c0.1-0.2,0.4-0.3,0.7-0.3H42.6z",
                                onclick: function (){
                                    scope.type = 'M';
                                    scope.typeContent = "月视图";
                                    scope.$apply();
                                    scope.reloadData();
                                }
                            },
                            myWeek: {
                                show: true,
                                title: '周视图',
                                icon: "path://M61.1,51.9c0,5.7-2.9,8.6-8.8,8.6h-5.1c-3,0-5.1-0.7-6.2-2c-1.1-1.4-1.8-2.5-1.9-3.3c-0.1-0.3-0.2-0.4-0.5-0.4\
                                h-9c-2.9,0-5.1-0.4-6.5-1.2c-0.1-0.1-0.2-0.1-0.3-0.1c-0.1,0-0.2,0-0.2,0.1s-0.4,0.7-1,1.8c-0.6,1.1-1.2,2.1-2,2.8\
                                c-0.7,0.7-1.7,1.3-2.8,1.7c-1.1,0.4-2.4,0.6-4,0.6c-1.6,0-3.4-0.7-5.6-2c-2.2-1.3-3.3-3-3.3-4.9c0-1.1,0.3-2.5,1-4.3\
                                c1.5-4,2.5-7.4,3-10.3c0.5-2.9,0.7-6.6,0.7-11V14.7c0-5,0.9-8.3,2.8-9.9c1.9-1.7,5.2-2.5,9.9-2.5h28.6c3.5,0,6,0.4,7.4,1.1\
                                c1.4,0.8,2.4,2,3,3.7c0.6,1.7,0.8,4.4,0.8,8V51.9z M58.8,51.9V15.1c0-4.3-0.5-7.1-1.6-8.5c-1.1-1.4-3.5-2.1-7.3-2.1H21.3\
                                c-3.8,0-6.5,0.7-8.1,2.1c-1.5,1.4-2.3,4.1-2.3,8.1v13.3c0,4.5-0.2,8.2-0.7,11.2c-0.5,3-1.5,6.7-3.1,10.9c-0.5,1.2-0.7,2.3-0.7,3\
                                c0,1.4,0.8,2.5,2.4,3.5c1.6,1,3.2,1.5,4.7,1.5c2.6,0,4.5-1,5.7-3.1c1.2-2.1,1.9-3.5,1.9-4.2c0-0.2,0-0.4-0.1-0.6\
                                c-0.3-0.7-0.5-1.8-0.5-3.3v-7.2c0-0.7,0-1.2-0.1-1.4c-0.1-0.2-0.2-0.4-0.5-0.4c-1.6-0.6-2.4-2.5-2.4-5.6c0-2.4,0.9-4.3,2.7-5.7\
                                c0.4-0.3,0.6-0.5,0.6-0.7s-0.2-0.4-0.5-0.7C19.4,24,19,22.7,19,20.9c0-1.8,0.6-3.3,1.7-4.7c1.2-1.3,2.7-2,4.5-2.1\
                                c0.9,0,1.6-0.1,2-0.2c0.4-0.1,0.7-0.4,0.9-0.7c1.2-1.9,3.4-2.8,6.7-2.8c1.7,0,3.1,0.3,4.2,0.8c1.2,0.5,1.9,1.1,2.2,1.7\
                                c0.3,0.6,0.6,1,1,1.1c0.3,0.1,1.1,0.2,2.3,0.2c2.3,0.1,3.9,0.8,4.8,2s1.3,2.6,1.3,4.2c0,1.9-0.5,3.4-1.4,4.4\
                                c-0.2,0.3-0.4,0.5-0.4,0.7c0,0.2,0.2,0.4,0.6,0.6c1.7,0.8,2.5,2.5,2.5,5.3c0,2.7-0.7,4.5-2,5.3c-0.3,0.2-0.6,0.5-0.7,0.8\
                                c-0.1,0.3-0.2,0.8-0.2,1.5v7.9c0,4.2-1.7,6.6-5.1,7.2c-1.4,0.3-2.1,0.7-2.1,1.3c0,0.6,0.4,1.2,1.1,2c0.7,0.7,2.2,1.1,4.3,1.1h5.1\
                                C56.6,58.2,58.8,56.1,58.8,51.9z M41.1,32.5c1.2,0,2.3,0.2,3.4,0.7c1.1,0.5,1.9,0.9,2.3,1.4c0.4,0.5,0.8,0.7,1.3,0.7s0.9-0.3,1.3-1\
                                s0.6-1.6,0.6-3c0-1.4-0.4-2.4-1.1-3.2c-0.8-0.7-2-1.1-3.6-1.1h-4.5c-0.2,0-0.4-0.1-0.5-0.2c-0.1-0.1-0.1-0.4-0.1-0.8\
                                c0-0.4,0-0.7,0.1-0.9s0.2-0.3,0.5-0.3h4c0.9,0,1.8-0.4,2.6-1.3c0.8-0.9,1.2-1.9,1.2-3.1c0-1.2-0.4-2.3-1.2-3.1\
                                c-0.8-0.8-1.7-1.3-2.7-1.3H41c-0.5,0-0.7-0.2-0.7-0.6c0-1-0.5-1.8-1.5-2.4c-1-0.6-2.3-0.9-4-0.9c-1.7,0-3,0.3-4,0.9\
                                c-1,0.6-1.5,1.3-1.5,2.2c0,0.5-0.3,0.7-0.8,0.7h-3.8c-1,0-1.9,0.4-2.7,1.3c-0.8,0.9-1.1,1.9-1.1,3c0,1.2,0.4,2.2,1.2,3.1\
                                c0.8,0.9,1.7,1.4,2.7,1.4h3.9c0.5,0,0.7,0.3,0.7,0.9c0,0.6-0.1,0.9-0.2,1.1C29.1,27,28.9,27,28.6,27h-4.5c-1.4,0-2.6,0.5-3.4,1.5\
                                c-0.9,1-1.3,2.1-1.3,3.3s0.2,2.2,0.5,2.8c0.3,0.6,0.7,0.9,1.2,0.9c0.5,0,1-0.3,1.5-0.9c0.5-0.6,1.3-1.1,2.3-1.5\
                                c1-0.4,2.1-0.6,3.2-0.6H41.1z M47.2,46.2v-6.7c0-1.5-0.6-2.7-1.8-3.7c-1.2-1-2.6-1.5-4.4-1.5H28c-1.8,0-3.2,0.5-4.2,1.6\
                                c-1,1.1-1.5,2.4-1.5,4.2v6.1c0,2.3,0.5,3.9,1.5,4.8c1,0.9,2.9,1.4,5.8,1.4h11.1c2.3,0,4-0.5,5-1.4S47.2,48.5,47.2,46.2z M36.4,41.2\
                                c0.5,0,0.7,0.1,0.8,0.3s0.2,0.6,0.2,1.2c0,0.6-0.1,1-0.2,1.1c-0.1,0.2-0.4,0.3-0.8,0.3h-3.2c-0.4,0-0.6-0.1-0.7-0.2\
                                c-0.1-0.2-0.2-0.5-0.2-1.1s0-1,0.1-1.2s0.3-0.3,0.8-0.3H36.4z",
                                onclick: function (){
                                    scope.type = 'W';
                                    scope.typeContent = "周视图";
                                    scope.$apply();
                                    scope.reloadData();
                                }
                            },
                            myDay: {
                                show: true,
                                title: '日视图',
                                icon: "path://M57.4,47.5c0,4.5-1,7.8-2.9,9.8c-2,2-5.1,3-9.3,3H18.7c-4,0-7-1-8.9-3.1C8,55.1,7,52,7,47.7V13.3\
                                c0-3.1,1-5.7,3.1-7.7c2.1-2,4.8-3,8.1-3H45c4,0,7.1,1,9.2,2.9c2.1,2,3.2,4.8,3.2,8.4V47.5z M55.1,47.5V13.9c0-2.9-0.9-5.1-2.6-6.7\
                                c-1.7-1.6-4.2-2.4-7.5-2.4H18.3c-2.6,0-4.8,0.8-6.5,2.4c-1.7,1.6-2.5,3.6-2.5,6v34.4c0,3.5,0.8,6.1,2.3,7.8\
                                c1.5,1.7,3.9,2.5,7.1,2.5h26.5c3.4,0,5.9-0.8,7.5-2.4C54.3,53.9,55.1,51.2,55.1,47.5z M42.5,24.7c0,0.4-0.1,0.7-0.2,0.8\
                                c-0.2,0.1-0.5,0.2-1,0.2H23.5c-0.5,0-0.8-0.1-0.9-0.2c-0.1-0.2-0.2-0.5-0.2-1v-5.6c0-0.5,0.1-0.9,0.2-1.1c0.1-0.2,0.4-0.3,1-0.3\
                                h17.9c0.7,0,1.1,0.5,1.1,1.5V24.7z M42.8,44.3c0,0.5-0.1,0.8-0.2,0.9c-0.2,0.2-0.5,0.2-1,0.2h-18c-0.5,0-0.8-0.1-0.9-0.2\
                                c-0.1-0.2-0.2-0.5-0.2-1.1v-5c0-0.6,0.1-1,0.2-1.1c0.1-0.1,0.4-0.2,0.9-0.2h18.1c0.5,0,0.9,0.1,1,0.3c0.1,0.2,0.2,0.6,0.2,1.1V44.3\
                                z M40.4,22.9v-2.9c0-0.3-0.1-0.5-0.4-0.5H25c-0.3,0-0.5,0.1-0.5,0.4v3.2c0,0.3,0.2,0.5,0.7,0.5h14.6C40.2,23.6,40.4,23.4,40.4,22.9\
                                z M40.7,42.5v-1.8c0-0.5-0.2-0.8-0.7-0.8H25.8c-0.9,0-1.4,0.4-1.4,1.1v1.5c0,0.4,0.1,0.6,0.2,0.7c0.2,0.1,0.4,0.2,0.8,0.2h14.3\
                                C40.4,43.4,40.7,43.1,40.7,42.5z",
                                onclick: function (){
                                    scope.type = 'D';
                                    scope.typeContent="日视图";
                                    scope.$apply();
                                    scope.reloadData();
                                }
                            },
                            myZoom: {
                                show: true,
                                title: '日视图',
                                icon: "<i class='fa fa-expand'></i>",
                                onclick: function (){
                                    scope.enlarged();
                                }
                            },
                            dataZoom: {
                                yAxisIndex: 'false'
                            },
                            restore: {},//配置项还原
                            saveAsImage: {}

                        }
                    },

                    dataZoom: [
                        {
                            show: true,
                            realtime: true,//拖动时，是否实时更新系列的视图。如果设置为 false，则只在拖拽结束的时候更新。
                            // start: 30,//数据窗口范围的起始百分比
                            // end: 70,
                            xAxisIndex: [0, 1]
                        },
                        {
                            type: 'inside',
                            realtime: true,
                            // start: 30,
                            // end: 70,
                            xAxisIndex: [0, 1]
                        }
                    ],
                    grid: [{
                        left: 60,
                        right: 40,
                        top:60,
                        height: 100

                    }, {
                        left: 60,
                        right: 40,
                        top: 188,
                        height: 100
                    }],
                    xAxis : [ //直角坐标系 grid 中的 x 轴
                        {
                            type : 'category', //类目轴，适用于离散的类目数据，为该类型时必须通过 data 设置类目数据。
                            boundaryGap : false,//坐标轴两边留白策略，类目轴和非类目轴的设置和表现不一样。boundaryGap是一个两个值的组，分别表示数据最小值和最大值的延伸范围
                            axisLine: {onZero: true},//X 轴或者 Y 轴的轴线是否在另一个轴的 0 刻度上，只有在另一个轴为数值轴且包含 0 刻度时有效。
                            data: timeData
                        },
                        {
                            gridIndex: 1,
                            type : 'category',
                            boundaryGap : false,
                            axisLine: {onZero: true},
                            data: timeData,
                            position: 'top'
                        }
                    ],
                    yAxis : [
                        {
                            name : '百支',
                            type : 'value',
                            // max : 20000
                        },
                        {
                            gridIndex: 1,//y 轴所在的 grid 的索引
                            name : '百支',
                            type : 'value',
                            inverse: true, //是否是反向坐标轴，
                            // max : 20000
                        }
                    ],
                    series : [
                        {
                            name:'入库样本',
                            type:'line',
                            symbolSize: 8,
                            hoverAnimation: false,
                            data:countOfInSample
                        },
                        {
                            name:'出库样本',
                            type:'line',
                            xAxisIndex: 1,
                            yAxisIndex: 1,
                            symbolSize: 8,
                            hoverAnimation: false,
                            data:countOfOutSample
                        }
                    ]
                };
                var myChart = echarts.init(document.getElementById('line'),'macarons');
                myChart.setOption(option);
            });

        }
    }
})();
