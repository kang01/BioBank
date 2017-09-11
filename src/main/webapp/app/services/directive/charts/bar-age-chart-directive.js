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
                                icon: "path://M970.24 9.216h-230.912c-25.088 0-45.568 16.896-45.568 39.424s20.48 39.424 45.568 39.424h196.608v196.608c0 25.088 16.896 45.568 39.424 45.568s39.424-20.48 39.424-45.568v-236.032c0-22.528-19.456-39.424-44.544-39.424zM55.296 9.216h230.912c25.088 0 45.568 16.896 45.568 39.424s-20.48 39.424-45.568 39.424h-196.608v196.608c0 25.088-17.92 45.568-40.448 45.568-22.016 0-40.448-20.48-40.448-45.568v-236.032c0.512-22.528 21.504-39.424 46.592-39.424zM970.24 1014.784h-230.912c-25.088 0-45.568-17.92-45.568-40.448s20.48-40.448 45.568-40.448h196.608v-196.608c0-25.088 16.896-45.568 39.424-45.568s39.424 20.48 39.424 45.568V973.312c0 22.528-19.456 41.472-44.544 41.472zM55.296 1014.784h230.912c25.088 0 45.568-17.92 45.568-40.448s-20.48-40.448-45.568-40.448h-196.608v-196.608c0-25.088-17.92-45.568-40.448-45.568-22.016 0-40.448 20.48-40.448 45.568V973.312c0.512 22.528 21.504 41.472 46.592 41.472zM236.544 795.136h560.128V235.008h-560.128v560.128z m61.952-498.176h436.736v436.736h-436.736v-436.736z",
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
