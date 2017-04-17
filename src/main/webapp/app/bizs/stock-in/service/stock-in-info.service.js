/**
 * Created by gaokangkang on 2017/4/14.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInInfoService', StockInInfoService);

    StockInInfoService.$inject = ['$resource'];

    function StockInInfoService ($resource) {
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

        return service;
    }
})();
