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
            queryBoxList:_queryBoxList,
            queryShelfList:_queryShelfList,
            queryShelfDesc:_queryShelfDesc,
            saveMovement:_saveMovement,
            //换位
            changePosition:_changePosition,
            //销毁
            destroyBox:_destroyBox
        };
        function _queryBoxList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-box?searchForm='+searchForm,angular.toJson(data));
        }
        function _queryShelfList(data,searchForm) {
            return $http.post('api/res/shelves-list?searchForm='+searchForm,angular.toJson(data));
        }
        function _queryShelfDesc(positon) {
            var array =  _.split(positon, '.');
            var equipmentCode = array[0];
            var areaCode = array[1];
            var shelfCode = array[2];
            return $http.get('api/support-racks/pos/'+equipmentCode+'/'+areaCode+'/'+shelfCode);
        }
        function _saveMovement(param) {
            if(param.id){
                return $http.put('api/position-moves/forBox',param);
            }else{
                return $http.post('api/position-moves/forBox',param);
            }
        }
        //换位
        function _changePosition(param) {
            return $http.post('api/position-changes/forBox',param);
        }
        //销毁
        function _destroyBox(param) {
            return $http.post('api/position-destroys/forBox',param);
        }
        return service;
    }
})();
