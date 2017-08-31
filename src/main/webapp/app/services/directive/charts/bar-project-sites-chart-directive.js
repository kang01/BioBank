/**
 * Created by gaokangkang on 2017/8/31.
 * 项目点
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('barProjectSitesChart',  barProjectSitesChart);

    barProjectSitesChart.$inject  =  [];

    function  barProjectSitesChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                legend: "=",
                item: "=",
                data: "="
            },
            template: '<div id="bar1" style="width:100%;height: 200px"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            var dataAxis = [220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,220, 182, 191, 234, 290, 330, 310,];
            var data = dataAxis;
            var yMax = 200;
            var dataShadow = [];

            for (var i = 0; i < data.length; i++) {
                dataShadow.push(yMax);
            }

            var option = {
                title: {
                    text: '项目点样本分布',
                    subtext: '',
                    left: 'center'
                },
                tooltip : {
                    trigger: 'item'
                },
                toolbox: {
                    feature: {
                        restore: {},
                        saveAsImage: {}
                    }
                },
                xAxis: {
                    data: dataAxis,
                    axisLabel: {
                        inside: true,
                        textStyle: {
                            color: '#fff'
                        }
                    },
                    axisTick: {
                        show: false
                    },
                    axisLine: {
                        show: false
                    },
                    z: 10
                },
                yAxis: {
                    axisLine: {
                        show: false
                    },
                    axisTick: {
                        show: false
                    },
                    axisLabel: {
                        textStyle: {
                            color: '#999'
                        }
                    }
                },
                dataZoom: [
                    {
                        type: 'inside'
                    }
                ],
                series: [
                    { // For shadow
                        type: 'bar',
                        itemStyle: {
                            normal: {color: 'rgba(0,0,0,0.05)'}
                        },
                        barGap:'-100%',
                        barCategoryGap:'40%',
                        data: dataShadow,
                        animation: false
                    },
                    {
                        type: 'bar',
                        itemStyle: {
                            normal: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#83bff6'},
                                        {offset: 0.5, color: '#188df0'},
                                        {offset: 1, color: '#188df0'}
                                    ]
                                )
                            },
                            emphasis: {
                                color: new echarts.graphic.LinearGradient(
                                    0, 0, 0, 1,
                                    [
                                        {offset: 0, color: '#2378f7'},
                                        {offset: 0.7, color: '#2378f7'},
                                        {offset: 1, color: '#83bff6'}
                                    ]
                                )
                            }
                        },
                        data: data
                    }
                ],
                grid:{
                    x:30,
                    y:45,
                    x2:5,
                    y2:20
                }
            };
            var myChart = echarts.init(document.getElementById('bar1'));
            myChart.setOption(option);
            // Enable data zoom when user click bar.
            var zoomSize = 10;
            // myChart.on('click', function (params) {
            //     console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
            //     myChart.dispatchAction({
            //         type: 'dataZoom',
            //         startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
            //         endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, data.length - 1)]
            //     });
            // });



            // $.get('app/services/directive/china.json', function (chinaJson) {
            //     echarts.registerMap('china', chinaJson);
            //     var chart = echarts.init(document.getElementById('map'));
            //     chart.setOption({
            //         series: [{
            //             type: 'map',
            //             map: 'china'
            //         }]
            //     });
            // });
        }
    }
})();
