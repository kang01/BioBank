/**
 * Created by gaokangkang on 2017/4/11.
 * 转运成功
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipStockInService', TranshipStockInService);

    TranshipStockInService.$inject = ['$resource','$http'];

    function TranshipStockInService ($resource,$http) {
        var service = $resource('api/stock-in/tranship/:transhipCode', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });
        //转运完成
        service.saveTransferFinish = function(transhipCode,transportRecord){
            var ajaxUrl = 'api/tranships/'+transhipCode+'/completed';
            var req = {
                method: 'PUT',
                url: ajaxUrl,
                headers: {
                    'Content-Type': 'application/json'
                },
                data:{
                    receiveDate:transportRecord.receiveDate,
                    login:transportRecord.login,
                    password:transportRecord.password
                }
            };

            return $http(req);
        };
        //开始入库
        service.saveStockIn = function(transhipCodes){
            var ajaxUrl = 'api/stock-in/tranship/codes/'+transhipCodes;
            var req = {
                method: 'POST',
                url: ajaxUrl,
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            return $http(req);
        };
        return service;
    }
})();
