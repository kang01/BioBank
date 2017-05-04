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
        function _querySampleType(projectId) {
            return $http.get('api/project-sample-classes/projectId/'+projectId)
        }
        function _queryProjectSampleClasses(projectId,sampleTypeId) {
            return $http.get('api/project-sample-classes/projectId/'+projectId+'/sampleTypeId/'+sampleTypeId)
        }
        return service;
    }
    // function SampleTypeService ($resource) {
    //     var service = $resource('api/sample-types/all', {}, {
    //         'query': {method: 'GET', isArray: true},
    //         'get': {
    //             method: 'GET',
    //             transformResponse: function (data) {
    //                 data = angular.fromJson(data);
    //                 return data;
    //             }
    //         },
    //         'save': { method:'POST' },
    //         'update': { method:'PUT' },
    //         'delete':{ method:'DELETE'}
    //     });
    //
    //     return service;
    // }
})();
