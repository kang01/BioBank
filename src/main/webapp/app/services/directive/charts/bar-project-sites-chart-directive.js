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
            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }
                var projectSitesData = scope.data;
                var dataAxis = [];
                var projectSitesSampleCount = [];
                for(var i = 0; i < projectSitesData.length;i++){
                    dataAxis.push(projectSitesData[i].projectSiteCode);
                    projectSitesSampleCount.push(projectSitesData[i].countOfSample);
                }
                var yMax = Number(_.max(dataAxis)) + 1000;
                var dataShadow = [];

                for (var i = 0; i < projectSitesData.length; i++) {
                    dataShadow.push(yMax);
                }
                var option = {
                    title: {
                        text: '项目点样本分布',
                        subtext: '',
                        left: 'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter:function (params, ticket, callback) {
                            var msg = "#: " + params.name+
                                    "<br/>样本量:"+params.value;
                            return msg;
                        }
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
                            data: projectSitesSampleCount
                        }
                    ],
                    grid:{
                        x:40,
                        y:45,
                        x2:5,
                        y2:20
                    }
                };
                var myChart = echarts.init(document.getElementById('bar1'));
                myChart.setOption(option);

                var zoomSize = 6;
                myChart.on('click', function (params) {
                    console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
                    myChart.dispatchAction({
                        type: 'dataZoom',
                        startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                        endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, projectSitesSampleCount.length - 1)]
                    });
                });
            })

        }
    }
})();
