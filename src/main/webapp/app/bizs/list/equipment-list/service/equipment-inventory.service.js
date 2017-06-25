/**
 * Created by gaokangkang on 2017/6/25.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('EquipmentInventoryService', EquipmentInventoryService);

    EquipmentInventoryService.$inject = ['$http'];
    function EquipmentInventoryService($http) {
        var service = {
            queryEquipmentList:_queryEquipmentList
        };
        function _queryEquipmentList(data) {
            return $http.post('api/res/stock-list/frozen-position',angular.toJson(data));
        }

        return service;
    }
})();
