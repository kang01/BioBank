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
                heightStyle: "=",
                data: "=",
                enlarged: "&"
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
                        text: '样本疾病',
                        // subtext: '样本疾病分布',
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
                                icon: "path://M970.24 9.216h-230.912c-25.088 0-45.568 16.896-45.568 39.424s20.48 39.424 45.568 39.424h196.608v196.608c0 25.088 16.896 45.568 39.424 45.568s39.424-20.48 39.424-45.568v-236.032c0-22.528-19.456-39.424-44.544-39.424zM55.296 9.216h230.912c25.088 0 45.568 16.896 45.568 39.424s-20.48 39.424-45.568 39.424h-196.608v196.608c0 25.088-17.92 45.568-40.448 45.568-22.016 0-40.448-20.48-40.448-45.568v-236.032c0.512-22.528 21.504-39.424 46.592-39.424zM970.24 1014.784h-230.912c-25.088 0-45.568-17.92-45.568-40.448s20.48-40.448 45.568-40.448h196.608v-196.608c0-25.088 16.896-45.568 39.424-45.568s39.424 20.48 39.424 45.568V973.312c0 22.528-19.456 41.472-44.544 41.472zM55.296 1014.784h230.912c25.088 0 45.568-17.92 45.568-40.448s-20.48-40.448-45.568-40.448h-196.608v-196.608c0-25.088-17.92-45.568-40.448-45.568-22.016 0-40.448 20.48-40.448 45.568V973.312c0.512 22.528 21.504 41.472 46.592 41.472zM236.544 795.136h560.128V235.008h-560.128v560.128z m61.952-498.176h436.736v436.736h-436.736v-436.736z",
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
                        data: ['PCI','AMI','不详'],
                        top:"25"
                    },
                    series : [
                        {
                            name: '疾病类型',
                            type: 'pie',
                            radius : '55%',
                            center: ['50%', '60%'],
                            data:diseaseTypeData,
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
                    ]
                };
                if(scope.heightStyle == 'height450'){
                    $("#pie2").height(window.innerHeight -200);
                    option.series[0].label.normal.show = true;
                    option.toolbox.feature.myEnlarged.show = false;
                }
                setTimeout(function () {
                    var myChart = echarts.init(document.getElementById('pie2'));
                    myChart.setOption(option);
                },200);

            });


        }
    }
})();
