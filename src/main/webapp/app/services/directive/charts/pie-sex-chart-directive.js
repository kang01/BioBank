/**
 * Created by gaokangkang on 2017/8/31.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('pieSexChart',  pieSexChart);

    pieSexChart.$inject  =  [];

    function  pieSexChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                heightStyle: "=",
                data: "=",
                enlarged: "&"
            },
            template: '<div id="pie1" style="width:100%;height:200px;"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }

                var sexData = [];
                _.forEach(scope.data,function (data) {
                   sexData.push({value:data.countOfSample,name:data.gender});
                });
                var option = {
                    title : {
                        text: '样本性别',
                        subtext: '',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{b} : {c} ({d}%)"
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
                            saveAsImage: {}
                        }
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['男','女','不详'],
                        top:"25"
                    },
                    series : [
                        {
                            name: '性别',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:sexData,
                            label:{
                                normal:{
                                    show:false
                                }
                            },
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ],
                    grid:{
                        x:30,
                        y:-50,
                        x2:5,
                        y2:20
                    }
                };
                if(scope.heightStyle == "height450"){
                    $("#pie1").height(window.innerHeight -200);
                    option.series[0].label.normal.show = true;
                    option.toolbox.feature.myEnlarged.show = false;
                }
                setTimeout(function () {
                    var myChart = echarts.init(document.getElementById('pie1'));
                    myChart.setOption(option);
                },100);

            });


        }
    }
})();
