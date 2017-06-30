/**
 * Created by gaokangkang on 2017/6/25.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleInventoryService', SampleInventoryService);

    SampleInventoryService.$inject = ['$http'];
    function SampleInventoryService($http) {
        var service = {
            querySampleList:_querySampleList,
            querySampleDesList:_querySampleDesList,
            queryTubeDes:_queryTubeDes
        };
        function _querySampleList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-tube?searchForm='+searchForm,angular.toJson(data));
        }
        function _querySampleDesList(frozenTubeId) {
            return $http.get('api/stock-list/frozen-tube-history-detail/'+frozenTubeId);
        }
        function _queryTubeDes(frozenTubeId) {
            return $http.get('api/frozen-tubes/'+frozenTubeId);
        }

        return service;
    }
})();
