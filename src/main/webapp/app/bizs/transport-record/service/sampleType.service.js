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
          queryProjectSampleClasses:_queryProjectSampleClasses
        };
        function _querySampleType() {
            return $http.get('api/sample-types/all');
        }
        function _queryProjectSampleClasses(projectId,sampleTypeId) {
            return $http.get('api/project-sample-classes/projectId/'+projectId+'/sampleTypeId/'+sampleTypeId);
        }
        return service;
    }
})();
