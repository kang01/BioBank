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
                if(sampleTypeId == 5 || sampleTypeId == 39){
                    td.style.backgroundColor = 'rgba(204,153,255,0.3)';
                }
                //白细胞
                if(sampleTypeId == 6){
                    td.style.backgroundColor = 'rgba(255,255,255,0.3)';
                }
                //白细胞灰
                if(sampleTypeId == 7){
                    td.style.backgroundColor = 'rgba(192,192,192,0.3)';
                }
                //血浆绿
                if(sampleTypeId == 8 || sampleTypeId == 40){
                    td.style.backgroundColor = 'rgba(0,255,0,0.3)';
                }
                //血清-红
                if(sampleTypeId == 9 || sampleTypeId == 10){
                    td.style.backgroundColor = 'rgba(255,0,0,0.3)';
                }
                //尿-黄
                if(sampleTypeId == 11 || sampleTypeId == 38){
                    td.style.backgroundColor = 'rgba(255,255,0,0.3)';
                }
                if(sampleTypeId == 16){

                }
            };
            return factory;
        })


})();
