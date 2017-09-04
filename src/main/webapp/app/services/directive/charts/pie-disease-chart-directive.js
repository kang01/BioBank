/**
 * Created by gaokangkang on 2017/8/31.
 * 疾病
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('pieDiseaseChart',  pieDiseaseChart);

    pieDiseaseChart.$inject  =  [];

    function  pieDiseaseChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                legend: "=",
                item: "=",
                data: "="
            },
            template: '<div id="pie2" style="width:100%;height: 200px"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }
                var diseaseTypeData = [];
                _.forEach(scope.data,function (data) {
                    diseaseTypeData.push({value:data.countOfSample,name:data.diseaseType});
                });
                var option = {
                    title : {
                        text: '样本疾病分布',
                        // subtext: '样本疾病分布',
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
                        data: ['PCI','AMI','不详']
                    },
                    series : [
                        {
                            name: '疾病类型',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:diseaseTypeData,
                            itemStyle: {
                                emphasis: {
                                    shadowBlur: 10,
                                    shadowOffsetX: 0,
                                    shadowColor: 'rgba(0, 0, 0, 0.5)'
                                }
                            }
                        }
                    ]
                };
                var myChart = echarts.init(document.getElementById('pie2'));
                myChart.setOption(option);
            });


        }
    }
})();
