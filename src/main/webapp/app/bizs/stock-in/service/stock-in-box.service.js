/**
 * Created by zhuyu on 2017/3/31.
 * 入库单列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInBoxService', StockInBoxService);

    StockInBoxService.$inject = ['$resource', '$http'];

    function StockInBoxService ($resource, $http) {
        var service = $resource('api/temp/stock-in-box/:id', {}, {
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
            var ajaxUrl = 'api/temp/res/stock-in-boxes/stock-in/' + data.stockInCode;

            var req = {
                method: 'POST',
                url: ajaxUrl,
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
