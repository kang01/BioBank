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
        function _querySampleList(data) {
            return $http.post('api/res/stock-list/frozen-tube',angular.toJson(data));
        }

        return service;
    }
})();
