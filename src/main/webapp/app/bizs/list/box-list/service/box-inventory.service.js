/**
 * Created by gaokangkang on 2017/6/25.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('BoxInventoryService', BoxInventoryService);

    BoxInventoryService.$inject = ['$http'];
    function BoxInventoryService($http) {
        var service = {
            queryBoxList:_queryBoxList
        };
        function _queryBoxList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-box?searchForm='+searchForm,angular.toJson(data));
        }

        return service;
    }
})();