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
                        text: '样本性别分布',
                        subtext: '',
                        x:'center'
                    },
                    tooltip : {
                        trigger: 'item',
                        formatter: "{b} : {c} ({d}%)"
                    },
                    toolbox: {
                        feature: {
                            saveAsImage: {}
                        }
                    },
                    legend: {
                        orient: 'vertical',
                        left: 'left',
                        data: ['男','女','不详']
                    },
                    series : [
                        {
                            name: '性别',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:sexData,
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
            });


        }
    }
})();
