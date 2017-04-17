/**
 * Created by gaokangkang on 2017/4/14.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInSaveService', StockInSaveService);

    StockInSaveService.$inject = ['$resource', '$http'];

    function StockInSaveService ($resource, $http) {
        var service = $resource('api/stock-in/:stockInCode/completed', {}, {
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

        service.saveStockIn = function(stockInCode){
            var ajaxUrl = 'api/stock-in/'+stockInCode+'/completed';

            var req = {
                method: 'PUT',
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
