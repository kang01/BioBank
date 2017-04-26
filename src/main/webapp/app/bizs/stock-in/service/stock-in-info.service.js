/**
 * Created by gaokangkang on 2017/4/14.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInInfoService', StockInInfoService);

    StockInInfoService.$inject = ['$resource','$http'];

    function StockInInfoService ($resource, $http) {
        var service = $resource('api/stock-in', {}, {
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

        service.complete = function(stockInCode, data){
            var url = 'api/stock-in/'+stockInCode+'/completed';
            var params = data;

            return $http.put(url, params);
        };

        return service;
    }
})();
