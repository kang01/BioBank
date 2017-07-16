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
            saveMovement:_saveMovement
        };
        function _queryBoxList(data,searchForm) {
            return $http.post('api/res/stock-list/frozen-box?searchForm='+searchForm,angular.toJson(data));
        }
        function _queryShelfList(positon) {
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
        return service;
    }
})();
