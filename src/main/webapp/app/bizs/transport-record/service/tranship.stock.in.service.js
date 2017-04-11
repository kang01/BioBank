/**
 * Created by gaokangkang on 2017/4/11.
 * 入库
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
        service.saveStockIn = function(transhipCode){
            var ajaxUrl = 'api/stock-in/tranship/' + transhipCode;

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
