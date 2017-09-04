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
                legend: "=",
                item: "=",
                data: "="
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
                                    position: 'insideRight'
                                }
                            },
                            data: ageData
                        }
                    ]
                };
                var myChart = echarts.init(document.getElementById('bar7'));
                myChart.setOption(option);
            })


        }
    }
})();
