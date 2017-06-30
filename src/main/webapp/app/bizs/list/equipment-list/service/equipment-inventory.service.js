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
            queryEquipmentList:_queryEquipmentList,
            //获取架子类型
            querySupportRackTypes:_querySupportRackTypes
        };
        function _queryEquipmentList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-position?searchForm='+searchForm,angular.toJson(data));
        }
        function _querySupportRackTypes() {
            return $http.get('/api/support-rack-types');
        }

        return service;
    }
})();
