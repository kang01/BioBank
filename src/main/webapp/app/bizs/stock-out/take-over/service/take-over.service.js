/**
 * Created by gaokangkang on 2017/5/12.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TakeOverService', TakeOverService);

    TakeOverService.$inject = ['$http'];
    function TakeOverService($http) {
        var service = {
            //获取交接列表
            queryTakeOverList:_queryTakeOverList,
            queryWaitingTakeOverFrozenBoxesList: _queryWaitingTakeOverFrozenBoxesList,
            saveTakeoverInfo: _saveTakeoverInfo,
            getTakeoverInfo: _getTakeoverInfo,
        };
        function _queryTakeOverList(data,oSettings) {
            return $http.post('/api/res/stock-out-handovers',JSON.stringify(data))
        }
        function _queryWaitingTakeOverFrozenBoxesList(applyId, data,oSettings) {
            return $http.post('/api/res/stock-out-frozen-boxes/apply/'+applyId+'/waiting-handover',JSON.stringify(data))
        }

        function _saveTakeoverInfo(dto){
            var url = '/api/stock-out-handovers';
            if (dto.id){
                return $http.put(url, dto);
            } else {
                return $http.post(url, dto);
            }
        }

        function _getTakeoverInfo(id){
            return $http.get('api/stock-out-handovers/'+id).then(function(res){
                return res.data;
            });
        }
        return service;
    }
})();
