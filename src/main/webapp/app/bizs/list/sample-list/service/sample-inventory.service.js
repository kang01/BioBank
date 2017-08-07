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
            queryTubeDes:_queryTubeDes,
            queryTubeDesByIds:_queryTubeDesByIds,
            //保存移位
            saveMovementDes:_saveMovementDes,
            //换位
            changePosition:_changePosition,
            //销毁
            destroySample:_destroySample
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
        function _queryTubeDesByIds(frozenTubeIds) {
            return $http.get('api/frozen-tubes/ids/'+frozenTubeIds);
        }
        function _saveMovementDes(param) {
            if(param.id){
                return $http.put('api/position-moves/forSample',param);
            }else{
                return $http.post('api/position-moves/forSample',param);
            }
        }
        //换位
        function _changePosition(param) {
            return $http.post('api/position-changes/forSample',param);
        }
        //销毁
        function _destroySample(param) {
            return $http.post('api/position-destroys/forSample',param);
        }

        return service;
    }
})();
