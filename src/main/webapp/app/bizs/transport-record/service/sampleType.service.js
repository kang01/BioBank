/**
 * Created by gaokangkang on 2017/3/25.
 * 样本类型
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleTypeService', SampleTypeService);

    SampleTypeService.$inject = ['$http'];
    function SampleTypeService($http) {
        var service = {
          querySampleType:_querySampleType,
          queryProjectSampleClasses:_queryProjectSampleClasses,
            getBoxTypeCode:_getBoxTypeCode,
        };
        function _querySampleType() {
            return $http.get('api/sample-types/all');
        }
        function _queryProjectSampleClasses(projectId,sampleTypeId) {
            return $http.get('api/project-sample-classes/projectId/'+projectId+'/sampleTypeId/'+sampleTypeId);
        }
        function _getBoxTypeCode(sampleTypeCode){
            var boxTypeCode = null;
            switch (sampleTypeCode){
                case "RNA":
                    boxTypeCode = "DJH";
                    break;
                case "DNA":
                    boxTypeCode = "96KB";
                    break;
                default:
                    boxTypeCode = "DCH";
                    break;
            }

            return boxTypeCode;
        }
        return service;
    }
})();
