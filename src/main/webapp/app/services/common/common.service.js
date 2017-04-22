/**
 * Created by gaoyankang on 2017/4/4.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleService', function () {
            var factory = {};
            factory.changeSampleType = function (code,td) {
                //血浆
                if(code == 'S_TYPE_00001' || code == 'S_TYPE_00002'){
                    td.style.backgroundColor = 'rgb(240,224,255)';
                }
                //白细胞
                if(code == 'S_TYPE_00003'){
                    td.style.backgroundColor = 'rgb(255,255,255)';
                }
                //白细胞灰
                if(code == 'S_TYPE_00004'){
                    td.style.backgroundColor = 'rgb(236,236,236)';
                }
                //血浆绿
                if(code == 'S_TYPE_00005' || code == 'S_TYPE_00006'){
                    td.style.backgroundColor = 'rgb(179,255,179)';
                }
                //血清-红
                if(code == 'S_TYPE_00007' || code == 'S_TYPE_00008'){
                    td.style.backgroundColor = 'rgb(255,179,179)';
                }
                //尿-黄
                if(code == 'S_TYPE_00009' || code == 'S_TYPE_00010'){
                    td.style.backgroundColor = 'rgb(255,255,179)';
                }
                if(code == 16){

                }
            };
            return factory;
        })


})();
