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
            //获取设备类型
            queryEquipmentType:_queryEquipmentType,
            //获取区域列表
            queryAreaList:_queryAreaList,
            //获取架子类型
            querySupportRackTypes:_querySupportRackTypes,
            //获取架子详情
            queryRack:_queryRack,
            saveMovement:_saveMovement,
            //换位
            changePosition:_changePosition
        };
        function _queryEquipmentList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-position?searchForm='+searchForm,angular.toJson(data));
        }
        function _queryEquipmentType() {
            return $http.get('api/equipment-modles/equipment-type-list');
        }
        function _queryAreaList(data,searchForm) {
            return $http.post('api/res/areas-list?searchForm='+searchForm,angular.toJson(data));
        }
        function _querySupportRackTypes() {
            return $http.get('api/support-rack-types');
        }
        function _queryRack(equipmentId,areaId) {
            return $http.get('api/support-racks/equipment/'+equipmentId+'/area/'+areaId);
        }
        function _saveMovement(param) {
            if(param.id){
                return $http.put('api/position-moves/forShelf',param);
            }else{
                return $http.post('api/position-moves/forShelf',param);
            }
        }
        //换位
        function _changePosition(param) {
            return $http.post('api/position-changes/forShelf',param);
        }

        return service;
    }
})();
