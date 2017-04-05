/**
 * Created by gaoyankang on 2017/4/4.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleService', function () {
            var factory = {};
            factory.changeSampleType = function (sampleTypeId,td) {
                //血浆
                if(sampleTypeId == 'S_TYPE_00001' || sampleTypeId == 'S_TYPE_00002'){
                    td.style.backgroundColor = 'rgba(204,153,255,0.3)';
                }
                //白细胞
                if(sampleTypeId == 'S_TYPE_00003'){
                    td.style.backgroundColor = 'rgba(255,255,255,0.3)';
                }
                //白细胞灰
                if(sampleTypeId == 'S_TYPE_00004'){
                    td.style.backgroundColor = 'rgba(192,192,192,0.3)';
                }
                //血浆绿
                if(sampleTypeId == 'S_TYPE_00005' || sampleTypeId == 'S_TYPE_00006'){
                    td.style.backgroundColor = 'rgba(0,255,0,0.3)';
                }
                //血清-红
                if(sampleTypeId == 'S_TYPE_00007' || sampleTypeId == 'S_TYPE_00008'){
                    td.style.backgroundColor = 'rgba(255,0,0,0.3)';
                }
                //尿-黄
                if(sampleTypeId == 'S_TYPE_00009' || sampleTypeId == 'S_TYPE_00010'){
                    td.style.backgroundColor = 'rgba(255,255,0,0.3)';
                }
                if(sampleTypeId == 16){

                }
            };
            return factory;
        })


})();
