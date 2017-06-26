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
            querySampleList:_querySampleList
        };
        function _querySampleList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-tube?searchForm='+searchForm,angular.toJson(data));
        }

        return service;
    }
})();
