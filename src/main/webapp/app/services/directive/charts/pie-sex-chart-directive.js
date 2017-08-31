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
                legend: "=",
                item: "=",
                data: "="
            },
            template: '<div id="pie1" style="width:100%;height: 200px"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {

            var option = {
                title : {
                    text: '',
                    subtext: '',
                    x:'center'
                },
                tooltip : {
                    trigger: 'item',
                    formatter: "{a} <br/>{b} : {c} ({d}%)"
                },
                series : [
                    {
                        name: '访问来源',
                        type: 'pie',
                        radius : '55%',
                        center: ['50%', '60%'],
                        data:[
                            {value:335, name:'男'},
                            {value:310, name:'女'},
                            {value:234, name:'不详'}
                        ],
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
            var myChart = echarts.init(document.getElementById('pie1'));
            myChart.setOption(option);

        }
    }
})();
