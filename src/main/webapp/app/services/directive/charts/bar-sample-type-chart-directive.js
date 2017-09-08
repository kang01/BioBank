/**
 * Created by gaokangkang on 2017/8/31.
 * 样本分类
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('barSampleTypeChart',  barSampleTypeChart);

    barSampleTypeChart.$inject  =  [];

    function  barSampleTypeChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                heightStyle: "=",
                data: "=",
                enlarged: "&"
            },
            template: '<div id="bar2" style="width:100%;height: 200px;"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {

            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }

                var sampleTypeSampleCountData = scope.data;
                var sampleCountData = [];
                var dataAxis = [];
                _.forEach(sampleTypeSampleCountData,function (data) {
                    sampleCountData.push(data.countOfSample);
                    dataAxis.push(data.sampleTypeName)
                });

                var yMax = 500;
                var dataShadow = [];

                for (var i = 0; i < sampleCountData.length; i++) {
                    dataShadow.push(yMax);
                }

                var option = {
                    title: {
                        text: '样本类型分布',
                        subtext: '',
                        left: 'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter:function (params, ticket, callback) {
                            var msg =  params.name+
                                "<br/>样本量:"+params.value;
                            return msg;
                        }
                    },
                    toolbox: {
                        feature: {
                            myEnlarged: {
                                show: true,
                                title: '放大',
                                icon: "path://M947.509543 898.852403 726.787249 678.130109c55.154992-64.569202 88.411712-148.273808 88.411712-239.653043 0-203.735785-165.77196-369.507745-369.507745-369.507745s-369.507745 165.77196-369.507745 369.507745 165.77196 369.610073 369.507745 369.610073c91.379235 0 175.083841-33.359049 239.653043-88.411712l220.722294 220.722294c5.730389 5.730389 13.20036 8.595583 20.772659 8.595583 7.469971 0 15.04227-2.865194 20.772659-8.595583C958.970321 928.834616 958.970321 910.313181 947.509543 898.852403zM134.817628 438.477066c0-171.40002 139.473569-310.873588 310.873588-310.873588S756.564805 267.077046 756.564805 438.477066c0 171.40002-139.473569 310.873588-310.873588 310.873588S134.817628 609.877086 134.817628 438.477066z",
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
                            data: sampleCountData
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
                    $("#bar2").height(window.innerHeight -200);
                    option.toolbox.feature.myEnlarged.show = false;
                }
                setTimeout(function () {
                    var myChart = echarts.init(document.getElementById('bar2'));
                    myChart.setOption(option);
                    // Enable data zoom when user click bar.
                    var zoomSize = 6;
                    myChart.on('click', function (params) {
                        console.log(dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)]);
                        myChart.dispatchAction({
                            type: 'dataZoom',
                            startValue: dataAxis[Math.max(params.dataIndex - zoomSize / 2, 0)],
                            endValue: dataAxis[Math.min(params.dataIndex + zoomSize / 2, sampleCountData.length - 1)]
                        });
                    });
                },100);


            })




        }
    }
})();
