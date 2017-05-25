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
            queryTakeOverList:_queryTakeOverList
        };
        function _queryTakeOverList(data,oSettings) {
            return $http.post('/api/temp/res/stock-out-handovers',JSON.stringify(data))
        }
        return service;
    }
})();
