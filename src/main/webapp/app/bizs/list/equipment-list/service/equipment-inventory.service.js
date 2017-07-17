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
            queryAreaList:_queryAreaList,
            //获取架子类型
            querySupportRackTypes:_querySupportRackTypes,
            queryEquipmentById:_queryEquipmentById,
            saveMovement:_saveMovement
        };
        function _queryEquipmentList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-position?searchForm='+searchForm,angular.toJson(data));
        }
        function _queryAreaList(data,searchForm) {
            return $http.post('api/res/areas-list?searchForm='+searchForm,angular.toJson(data));
        }
        function _querySupportRackTypes() {
            return $http.get('/api/support-rack-types');
        }
        function _queryEquipmentById(equipmentId) {
            return $http.get('/api/equipment/'+equipmentId);
        }
        function _saveMovement(param) {
            if(param.id){
                return $http.put('api/position-moves/forShelf',param);
            }else{
                return $http.post('api/position-moves/forShelf',param);
            }
        }

        return service;
    }
})();
