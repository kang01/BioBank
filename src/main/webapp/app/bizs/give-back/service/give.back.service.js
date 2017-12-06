/**
 * Created by gaokangkang on 2017/12/5.
 * 归还
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('GiveBackService', GiveBackService);

    GiveBackService.$inject = ['$http'];
    function GiveBackService($http) {
        var service = {
            //获取归还的盒子列表
            queryGiveBackTable:_queryGiveBackTable
        };
        function _queryGiveBackTable(data,oSettings) {
            return $http.post('api/res/stock-out-plans',JSON.stringify(data));
        }
        return service;
    }
})();
