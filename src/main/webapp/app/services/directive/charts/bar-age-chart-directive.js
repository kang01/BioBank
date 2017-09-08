/**
 * Created by gaokangkang on 2017/8/31.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('barAgeChart',  barAgeChart);

    barAgeChart.$inject  =  [];

    function  barAgeChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                data: "=",
                heightStyle: "=",
                enlarged: "&"
            },
            template: '<div id="bar7" style="width:100%;height: 600px"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }
                var ageXAxis = [];
                var ageData = [];
                _.forEach(scope.data,function (data) {
                    ageXAxis.push(data.age);
                    ageData.push(data.countOfSample);
                });
                var option = {
                    title : {
                        text: '样本年龄分布',
                        subtext: '',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'axis',
                        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
                            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
                        },
                        formatter:function (params, ticket, callback) {
                            var msg =  params[0].name+
                                "<br/>样本量:"+params[0].value;
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
                            saveAsImage: {}
                        }
                    },
                    grid: {
                        left: '1%',
                        right: '4%',
                        bottom: '3%',
                        containLabel: true
                    },
                    xAxis:  {
                        type: 'value'
                    },
                    yAxis: {
                        type: 'category',
                        data: ageXAxis
                    },
                    series: [
                        {
                            name: '样本量',
                            type: 'bar',
                            stack: '总量',
                            label: {
                                normal: {
                                    show: true,
                                    position: 'insideBottomRight'
                                }
                            },
                            data: ageData
                        }
                    ]
                };
                if(scope.heightStyle == "big"){
                    $("#bar7" ).height(window.innerHeight -200);
                    option.toolbox.feature.myEnlarged.show = false;
                }
                var myChart = echarts.init(document.getElementById('bar7'));
                myChart.setOption(option);
            })


        }
    }
})();
