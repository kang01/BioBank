/**
 * Created by gaoyankang on 2017/4/4.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleService', function () {
            var factory = {};
            factory.changeSampleType = function (sampleId,td,sampleTypeOptions) {
                for(var i = 0; i < sampleTypeOptions.length; i++){
                    if(sampleId == sampleTypeOptions[i].sampleClassificationId){
                        td.style.backgroundColor = sampleTypeOptions[i].backColor;
                    }
                }
            };
            return factory;
        })


})();
