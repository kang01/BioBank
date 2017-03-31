/**
 * Created by zhuyu on 2017/3/31.
 * 入库单列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInService', StockInService);

    StockInService.$inject = ['$resource', '$http'];

    function StockInService ($resource, $http) {
        var service = $resource('api/temp/stock-in/:id', {}, {
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

        service.getJqDataTableValues = function(data, oSettings){
            var req = {
                method: 'POST',
                url: 'api/temp/res/stock-in',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(data)
            }

            return $http(req);
        };

        return service;
    }
})();
