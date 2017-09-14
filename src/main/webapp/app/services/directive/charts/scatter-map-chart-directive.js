/**
 * Created by gaokangkang on 2017/8/31.
 * 样本分布图 散点图 地图
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('scatterMapChart',  scatterMapChart);

    scatterMapChart.$inject  =  [];

    function  scatterMapChart()  {
        var  directive  =  {
            restrict:  'EA',
            scope  :  {
                data: "=",
                left: "=",
                zoom: "=",
                mapStatus: "=",
                geoIndex: "=",
                enlarged: "&",
                querySampleCount: "&"
            },
            template: '<div id="map" style="width:100%;height:600px"></div>',
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            scope.$watch('data',function (newValue,oldValue) {
                if (!newValue || !newValue.length){
                    return;
                }


                var geoCoordMap = {
                    '呼伦贝尔':[119.753086,49.218492],
                    '海门':[121.15,31.89],
                    '鄂尔多斯':[109.781327,39.608266],
                    '招远':[120.38,37.35],
                    '舟山':[122.207216,29.985295],
                    '齐齐哈尔':[123.97,47.33],
                    '盐城':[120.13,33.38],
                    '赤峰':[118.87,42.28],
                    '青岛':[120.33,36.07],
                    '乳山':[121.52,36.89],
                    '金昌':[102.188043,38.520089],
                    '泉州':[118.58,24.93],
                    '莱西':[120.53,36.86],
                    '日照':[119.46,35.42],
                    '胶南':[119.97,35.88],
                    '南通':[121.05,32.08],
                    '拉萨':[91.11,29.97],
                    '云浮':[112.02,22.93],
                    '梅州':[116.1,24.55],
                    '文登':[122.05,37.2],
                    '上海':[121.48,31.22],
                    '攀枝花':[101.718637,26.582347],
                    '威海':[122.1,37.5],
                    '承德':[117.93,40.97],
                    '厦门':[118.1,24.46],
                    '汕尾':[115.375279,22.786211],
                    '潮州':[116.63,23.68],
                    '丹东':[124.37,40.13],
                    '太仓':[121.1,31.45],
                    '曲靖':[103.79,25.51],
                    '烟台':[121.39,37.52],
                    '福州':[119.3,26.08],
                    '瓦房店':[121.979603,39.627114],
                    '即墨':[120.45,36.38],
                    '抚顺':[123.97,41.97],
                    '玉溪':[102.52,24.35],
                    '张家口':[114.87,40.82],
                    '阳泉':[113.57,37.85],
                    '莱州':[119.942327,37.177017],
                    '湖州':[120.1,30.86],
                    '汕头':[116.69,23.39],
                    '昆山':[120.95,31.39],
                    '宁波':[121.56,29.86],
                    '湛江':[110.359377,21.270708],
                    '揭阳':[116.35,23.55],
                    '荣成':[122.41,37.16],
                    '连云港':[119.16,34.59],
                    '葫芦岛':[120.836932,40.711052],
                    '常熟':[120.74,31.64],
                    '东莞':[113.75,23.04],
                    '河源':[114.68,23.73],
                    '淮安':[119.15,33.5],
                    '泰州':[119.9,32.49],
                    '南宁':[108.33,22.84],
                    '营口':[122.18,40.65],
                    '惠州':[114.4,23.09],
                    '江阴':[120.26,31.91],
                    '蓬莱':[120.75,37.8],
                    '韶关':[113.62,24.84],
                    '嘉峪关':[98.289152,39.77313],
                    '广州':[113.23,23.16],
                    '延安':[109.47,36.6],
                    '太原':[112.53,37.87],
                    '清远':[113.01,23.7],
                    '中山':[113.38,22.52],
                    '昆明':[102.73,25.04],
                    '寿光':[118.73,36.86],
                    '盘锦':[122.070714,41.119997],
                    '长治':[113.08,36.18],
                    '深圳':[114.07,22.62],
                    '珠海':[113.52,22.3],
                    '宿迁':[118.3,33.96],
                    '咸阳':[108.72,34.36],
                    '铜川':[109.11,35.09],
                    '平度':[119.97,36.77],
                    '佛山':[113.11,23.05],
                    '海口':[110.35,20.02],
                    '江门':[113.06,22.61],
                    '章丘':[117.53,36.72],
                    '肇庆':[112.44,23.05],
                    '大连':[121.62,38.92],
                    '临汾':[111.5,36.08],
                    '吴江':[120.63,31.16],
                    '石嘴山':[106.39,39.04],
                    '沈阳':[123.38,41.8],
                    '苏州':[120.62,31.32],
                    '茂名':[110.88,21.68],
                    '嘉兴':[120.76,30.77],
                    '长春':[125.35,43.88],
                    '胶州':[120.03336,36.264622],
                    '银川':[106.27,38.47],
                    '张家港':[120.555821,31.875428],
                    '三门峡':[111.19,34.76],
                    '锦州':[121.15,41.13],
                    '南昌':[115.89,28.68],
                    '柳州':[109.4,24.33],
                    '三亚':[109.511909,18.252847],
                    '自贡':[104.778442,29.33903],
                    '吉林':[126.57,43.87],
                    '阳江':[111.95,21.85],
                    '泸州':[105.39,28.91],
                    '西宁':[101.74,36.56],
                    '宜宾':[104.56,29.77],
                    '呼和浩特':[111.65,40.82],
                    '成都':[104.06,30.67],
                    '大同':[113.3,40.12],
                    '镇江':[119.44,32.2],
                    '桂林':[110.28,25.29],
                    '张家界':[110.479191,29.117096],
                    '宜兴':[119.82,31.36],
                    '北海':[109.12,21.49],
                    '西安':[108.95,34.27],
                    '金坛':[119.56,31.74],
                    '东营':[118.49,37.46],
                    '牡丹江':[129.58,44.6],
                    '遵义':[106.9,27.7],
                    '绍兴':[120.58,30.01],
                    '扬州':[119.42,32.39],
                    '常州':[119.95,31.79],
                    '潍坊':[119.1,36.62],
                    '重庆':[106.54,29.59],
                    '台州':[121.420757,28.656386],
                    '南京':[118.78,32.04],
                    '滨州':[118.03,37.36],
                    '贵阳':[106.71,26.57],
                    '无锡':[120.29,31.59],
                    '本溪':[123.73,41.3],
                    '克拉玛依':[84.77,45.59],
                    '渭南':[109.5,34.52],
                    '马鞍山':[118.48,31.56],
                    '宝鸡':[107.15,34.38],
                    '焦作':[113.21,35.24],
                    '句容':[119.16,31.95],
                    '北京':[116.46,39.92],
                    '徐州':[117.2,34.26],
                    '衡水':[115.72,37.72],
                    '包头':[110,40.58],
                    '绵阳':[104.73,31.48],
                    '乌鲁木齐':[87.68,43.77],
                    '枣庄':[117.57,34.86],
                    '杭州':[120.19,30.26],
                    '淄博':[118.05,36.78],
                    '鞍山':[122.85,41.12],
                    '溧阳':[119.48,31.43],
                    '库尔勒':[86.06,41.68],
                    '安阳':[114.35,36.1],
                    '开封':[114.35,34.79],
                    '济南':[117,36.65],
                    '德阳':[104.37,31.13],
                    '温州':[120.65,28.01],
                    '九江':[115.97,29.71],
                    '邯郸':[114.47,36.6],
                    '临安':[119.72,30.23],
                    '兰州':[103.73,36.03],
                    '沧州':[116.83,38.33],
                    '临沂':[118.35,35.05],
                    '南充':[106.110698,30.837793],
                    '天津':[117.2,39.13],
                    '富阳':[119.95,30.07],
                    '泰安':[117.13,36.18],
                    '诸暨':[120.23,29.71],
                    '郑州':[113.65,34.76],
                    '哈尔滨':[126.63,45.75],
                    '聊城':[115.97,36.45],
                    '芜湖':[118.38,31.33],
                    '唐山':[118.02,39.63],
                    '平顶山':[113.29,33.75],
                    '邢台':[114.48,37.05],
                    '德州':[116.29,37.45],
                    '济宁':[116.59,35.38],
                    '荆州':[112.239741,30.335165],
                    '宜昌':[111.3,30.7],
                    '义乌':[120.06,29.32],
                    '丽水':[119.92,28.45],
                    '洛阳':[112.44,34.7],
                    '秦皇岛':[119.57,39.95],
                    '株洲':[113.16,27.83],
                    '石家庄':[114.48,38.03],
                    '莱芜':[117.67,36.19],
                    '常德':[111.69,29.05],
                    '保定':[115.48,38.85],
                    '湘潭':[112.91,27.87],
                    '金华':[119.64,29.12],
                    '岳阳':[113.09,29.37],
                    '长沙':[113,28.21],
                    '衢州':[118.88,28.97],
                    '廊坊':[116.7,39.53],
                    '菏泽':[115.480656,35.23375],
                    '合肥':[117.27,31.86],
                    '武汉':[114.31,30.52],
                    '大庆':[125.03,46.58]
                };
                var citySampleCountdata = scope.data;

                var cityData = [];
                var geoCityMap;
                //城市
                if(scope.mapStatus == "市"){
                    _.forEach(citySampleCountdata,function (data) {
                        data.city = _.replace(data.city, '市', '');
                        cityData.push( _.pick(data, ['longitude', 'latitude','city']));
                    });
                    geoCityMap =  _.groupBy(cityData,"city");

                }
                //省份
                if(scope.mapStatus == "省"){
                    _.forEach(citySampleCountdata,function (data) {
                        data.province = _.replace(data.province, '市', '');
                        data.province = _.replace(data.province, '省', '');
                        cityData.push( _.pick(data, ['longitude', 'latitude','province']));
                    });
                    geoCityMap =  _.groupBy(cityData,"province");
                }
                //项目点
                if(scope.mapStatus == "项目点"){
                    _.forEach(citySampleCountdata,function (data) {
                        cityData.push( _.pick(data, ['longitude', 'latitude','projectSiteName']));
                    });
                    geoCityMap =  _.groupBy(cityData,"projectSiteName");
                }

                for(var city in geoCityMap){
                    var geoCoord = geoCoordMap[city];
                        if (!geoCoord) {
                            var array = [];
                            array.push(geoCityMap[city][0].longitude);
                            array.push(geoCityMap[city][0].latitude);
                            geoCoordMap[city] =  array;
                        }
                }
                var cityColorData = [];
                _.forEach(citySampleCountdata,function (city) {
                    var obj = {};
                    obj.name = city.province;
                    obj.value = city.countOfSample;
                    if(obj.value){
                        cityColorData.push(obj);
                    }

                });

                var convertData = function (data) {
                    var res = [];
                    if(scope.mapStatus === "省"){
                        for (var j = 0; j < data.length; j++) {
                            var geoCoord = geoCoordMap[data[j].province];
                            if (geoCoord) {
                                res.push({
                                    name: data[j].province,
                                    value: geoCoord.concat(data[j].countOfSample)
                                });
                            }
                        }
                    }
                    if(scope.mapStatus === "市"){
                        for (var i = 0; i < data.length; i++) {
                            var geoCoord = geoCoordMap[data[i].city];
                            if (geoCoord) {
                                res.push({
                                    name: data[i].city,
                                    value: geoCoord.concat(data[i].countOfSample)
                                });
                            }
                        }
                    }
                    if( scope.mapStatus === "项目点"){
                        for (var k = 0; k < data.length; k++) {
                            var geoCoord = geoCoordMap[data[k].projectSiteName];
                            if (geoCoord) {
                                res.push({
                                    name: data[k].projectSiteName,
                                    value: geoCoord.concat(data[k].countOfSample)
                                });
                            }
                        }
                    }
                    // console.log(JSON.stringify(res));
                    return res;
                };
                function randomValue() {
                    return Math.round(Math.random()*1000);
                }
                var option = {
                    backgroundColor: '#fff',
                    title: {
                        text: '全国('+scope.mapStatus+')样本分布',
                        subtext: '',
                        sublink: '',
                        left: 'center',
                        textStyle: {
                            color: '#333'
                        }
                    },
                    tooltip : {
                        formatter:function (params, ticket, callback) {
                            // console.log(JSON.stringify(params));
                            var msg =  params.data.name +
                                    "<br>样本量：" + params.value[2];
                            return msg;
                        }
                    },
                    legend: {
                        orient: 'vertical',//图例列表的布局朝向。//'horizontal vertical
                        y: 'bottom',
                        x:'right',
                        data:['样本量'],
                        textStyle: {
                            color: '#333'
                        }
                    },
                    toolbox: {
                        feature: {
                            myProvince: {
                                show: true,
                                title: '省会',
                                icon: "path://M284.768 481.872c149.376-29.6832 267.6576-62.0288 354.8448-97.0304l18.7488 31.4048c-62.4992 23.4368-141.488 46.7232-236.9536 69.8432h274.4544v256.4064h-34.688v-20.624h-263.4368v21.5616h-34.688v-243.5168a8138.112 8138.112 0 0 1-65.6256 14.2944c-3.4368-10.3072-7.6576-21.088-12.656-32.3392z m144.3744-168.7488l25.7824 27.6576a2343.76 2343.76 0 0 1-135.936 98.9056 2287.2736 2287.2736 0 0 0-22.032-30.4672c48.4352-30.9408 92.496-62.9696 132.1856-96.096z m232.032 204.8416h-263.4368v36.5632h263.4368v-36.5632z m-263.4368 104.064h263.4368v-36.5632h-263.4368v36.5632z m0 67.968h263.4368v-37.0304h-263.4368v37.0304z m100.7808-404.5312h35.6256v128.4384h-35.6256V285.4656z m93.7504 54.8448l19.2192-27.6576a2694.4832 2694.4832 0 0 1 138.2816 80.624l-22.4992 32.8128c-41.568-29.0624-86.5632-57.6544-135.0016-85.7792z",
                                onclick: function (){
                                    scope.mapStatus = "省";
                                    scope.geoIndex = 0;
                                    scope.$apply();
                                    scope.querySampleCount();

                                }
                            },
                            myCity: {
                                show: true,
                                title: '城市',
                                icon: "path://M44.032 179.2l451.584 0q-14.336-29.696-32.256-59.904t-35.328-59.904l71.68-35.84q23.552 32.768 42.496 64t33.28 64l-63.488 27.648 472.064 0 0 76.8-432.128 0 0 131.072 348.16 0 0 356.352q3.072 63.488-27.136 95.232t-96.768 28.672l-128 0-16.384-71.68 48.128 0q20.48 3.072 38.912 3.584t32.768 0.512q78.848 6.144 72.704-68.608l0-271.36-272.384 0 0 523.264-79.872 0 0-523.264-268.288 0 0 423.936-75.776 0 0-496.64 344.064 0 0-131.072-428.032 0 0-76.8z",
                                onclick: function (){
                                    scope.mapStatus = "市";
                                    scope.geoIndex = 10;
                                    scope.$apply();
                                    scope.querySampleCount();
                                }
                            },

                            myProjectSites: {
                                show: true,
                                title: '项目点',
                                icon: "path://M140.288 684.032l0-360.448 300.032 0 0-300.032 108.544 0 0 96.256 427.008 0 0 99.328-427.008 0 0 104.448 343.04 0 0 360.448-751.616 0zM251.904 423.936l0 159.744 528.384 0 0-159.744-528.384 0zM132.096 979.968l-103.424-36.864 15.36-27.648q32.768-57.344 54.784-102.4t33.28-77.824l104.448 24.576q-12.288 26.624-30.72 68.096t-45.056 95.744zM888.832 967.68q-9.216-23.552-27.648-59.904t-45.056-88.576q-8.192-20.48-16.896-35.84t-14.848-23.552l104.448-31.744 44.032 83.968 59.392 115.712zM356.352 963.584q-6.144-49.152-35.84-151.552l-16.384-52.224 104.448-12.288 27.648 100.352 27.648 99.328zM608.256 955.392q-9.216-38.912-25.088-87.552t-42.496-112.128l103.424-16.384 20.48 52.224 52.224 140.288z",
                                onclick: function (){
                                    scope.mapStatus = "项目点";
                                    scope.geoIndex = 10;
                                    scope.$apply();
                                    scope.querySampleCount();

                                }
                            },
                            myEnlarged: {
                                show: true,
                                title: '放大',
                                icon: "path://M970.24 9.216h-230.912c-25.088 0-45.568 16.896-45.568 39.424s20.48 39.424 45.568 39.424h196.608v196.608c0 25.088 16.896 45.568 39.424 45.568s39.424-20.48 39.424-45.568v-236.032c0-22.528-19.456-39.424-44.544-39.424zM55.296 9.216h230.912c25.088 0 45.568 16.896 45.568 39.424s-20.48 39.424-45.568 39.424h-196.608v196.608c0 25.088-17.92 45.568-40.448 45.568-22.016 0-40.448-20.48-40.448-45.568v-236.032c0.512-22.528 21.504-39.424 46.592-39.424zM970.24 1014.784h-230.912c-25.088 0-45.568-17.92-45.568-40.448s20.48-40.448 45.568-40.448h196.608v-196.608c0-25.088 16.896-45.568 39.424-45.568s39.424 20.48 39.424 45.568V973.312c0 22.528-19.456 41.472-44.544 41.472zM55.296 1014.784h230.912c25.088 0 45.568-17.92 45.568-40.448s-20.48-40.448-45.568-40.448h-196.608v-196.608c0-25.088-17.92-45.568-40.448-45.568-22.016 0-40.448 20.48-40.448 45.568V973.312c0.512 22.528 21.504 41.472 46.592 41.472zM236.544 795.136h560.128V235.008h-560.128v560.128z m61.952-498.176h436.736v436.736h-436.736v-436.736z",
                                onclick: function (){
                                    scope.enlarged();
                                }
                            },
                            restore: {},
                            saveAsImage: {}
                        }
                    },
                    visualMap: {
                        min: 0,
                        max: 15000,
                        seriesIndex: [0],
                        inRange: {
                            color: ['#e0ffff', '#006edd']
                        },
                        show:false,
                        itemStyle: {
                            normal: {
                                color: '#F95489'
                            }
                        }

                    },
                    geo:  {
                        map: 'china',//地图类型
                        label: {
                            normal: {
                                show: false,
                                textStyle: {
                                    color: 'rgba(0,0,0,0.4)'
                                }
                            }
                        },
                        roam: true, //是否开启鼠标缩放和平移漫游。默认不开启。如果只想要开启缩放或者平移，可以设置成 'scale' 或者 'move'。设置成 true 为都开启
                        itemStyle: {
                            normal: {
                                areaColor: '#CCCCCC',
                                borderColor: '#aaa'

                            },
                            emphasis: {
                                areaColor: '#FDDD31'
                            }
                        },
                        zoom:scope.zoom,
                        left:scope.left
                    },
                    series : [
                        {
                            name: 'categoryA',
                            type: 'map',
                            geoIndex: scope.geoIndex ,
                            tooltip: {show: false},
                            data:cityColorData
                        },
                        {
                            name: '样本量',
                            type: 'scatter',//散点（气泡）图
                            coordinateSystem: 'geo',//使用地理坐标系 cartesian2d使用二维的直角坐标系 polar使用极坐标系
                            data: convertData(citySampleCountdata),
                            symbolSize: function (val) { //标记的大小，可以设置成诸如 10 这样单一的数字，也可以用数组分开表示宽和高，例如 [20, 10] 表示标记宽为20，高为10。
                                var num = val[2] / 200;
                                if(num < 10 && num != 0){
                                    num = 10
                                }
                                return num;
                            },
                            label: {
                                normal: {
                                    formatter: '{b}',//标签内容格式器，支持字符串模板和回调函数两种形式，字符串模板与回调函数返回的字符串均支持用 \n 换行。模板变量有 {a}、{b}、{c}，分别表示系列名，数据名，数据值
                                    position: 'right',
                                    show: false
                                },
                                emphasis: { //图形在高亮状态下的样式
                                    show: true
                                }
                            },
                            itemStyle: { //图形样式，有 normal 和 emphasis 两个状态。normal 是图形在默认状态下的样式；emphasis 是图形在高亮状态下的样式
                                normal: {
                                    color: '#F95489'
                                }
                            }
                        },
                        {
                            name: 'Top 5',
                            type: 'effectScatter',//带有涟漪特效动画的散点（气泡）图
                            coordinateSystem: 'geo',//使用地理坐标系
                            data: convertData(citySampleCountdata.sort(function (a, b) {
                                return b.countOfSample - a.countOfSample;
                            }).slice(0, 5)),
                            symbolSize: function (val) {
                                var num = val[2] / 200;
                                if(num < 10 && num != 0){
                                    num = 10
                                }
                                return num;

                                // if(val[2] < 200){
                                //     return 100
                                // }
                                // return val[2] / 100;
                            },
                            showEffectOn: 'render',//配置何时显示特效。render绘制完成后显示特效。'emphasis' 高亮（hover）的时候显示特效
                            rippleEffect: {//涟漪特效相关配置。
                                brushType: 'stroke' //波纹的绘制方式，可选 'stroke' 和 'fill'。
                            },
                            hoverAnimation: true,
                            label: {
                                normal: {
                                    formatter: '{b}',
                                    position: 'right',
                                    show: true
                                }
                            },
                            itemStyle: {
                                normal: {
                                    color: '#96133D',
                                    shadowBlur: 100,//图形阴影的模糊大小
                                    shadowColor: '#333'
                                }
                            },
                            zlevel: 1 //用于 Canvas 分层 zlevel 大的 Canvas 会放在 zlevel 小的 Canvas 的上面
                        }
                    ]
                };
                if(scope.zoom == 1.2){
                    $("#map").height(window.innerHeight -200);
                    option.toolbox.feature.myEnlarged.show = false;
                }
                if(scope.geoIndex == 0){
                    option.geo.label.normal.show = true;
                }
                var myChart = echarts.init(document.getElementById('map'));
                myChart.setOption(option);



            });

        }
    }
})();
