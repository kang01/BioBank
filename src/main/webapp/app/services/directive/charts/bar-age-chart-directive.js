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
                    data: ['17以前','18～28','29～40','41～48','49～55','56～65','66～72','73～84','85以后']
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
                        data: [100000, 200000, 300000, 400000, 500000, 600000, 700000]
                    }
                ]
            };
            var myChart = echarts.init(document.getElementById('bar7'));
            myChart.setOption(option);

        }
    }
})();
