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
                enlarged: "&",
                data: "=",
                heightStyle: "="
            },
            template: '<div id="bar1" style="width:100%;height:200px;"></div>',
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
                            myEnlarged: {
                                show: true,
                                title: '放大',
                                icon: "path://M970.24 9.216h-230.912c-25.088 0-45.568 16.896-45.568 39.424s20.48 39.424 45.568 39.424h196.608v196.608c0 25.088 16.896 45.568 39.424 45.568s39.424-20.48 39.424-45.568v-236.032c0-22.528-19.456-39.424-44.544-39.424zM55.296 9.216h230.912c25.088 0 45.568 16.896 45.568 39.424s-20.48 39.424-45.568 39.424h-196.608v196.608c0 25.088-17.92 45.568-40.448 45.568-22.016 0-40.448-20.48-40.448-45.568v-236.032c0.512-22.528 21.504-39.424 46.592-39.424zM970.24 1014.784h-230.912c-25.088 0-45.568-17.92-45.568-40.448s20.48-40.448 45.568-40.448h196.608v-196.608c0-25.088 16.896-45.568 39.424-45.568s39.424 20.48 39.424 45.568V973.312c0 22.528-19.456 41.472-44.544 41.472zM55.296 1014.784h230.912c25.088 0 45.568-17.92 45.568-40.448s-20.48-40.448-45.568-40.448h-196.608v-196.608c0-25.088-17.92-45.568-40.448-45.568-22.016 0-40.448 20.48-40.448 45.568V973.312c0.512 22.528 21.504 41.472 46.592 41.472zM236.544 795.136h560.128V235.008h-560.128v560.128z m61.952-498.176h436.736v436.736h-436.736v-436.736z",
                                onclick: function (){
                                    scope.enlarged();
                                }
                            },
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
                if(scope.heightStyle == 'height450'){
                    $("#bar1").height(window.innerHeight -200);
                    option.toolbox.feature.myEnlarged.show = false;
                }
                var  myChart = echarts.init(document.getElementById('bar1'));
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
