/**
 * Created by gaoyankang on 2017/4/23.
 */
/**
 * Created by gaokangkang on 2017/3/29.
 * 根据冻存盒code字符串查询冻存盒和冻存管信息
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInBoxByCodeService', StockInBoxByCodeService);

    StockInBoxByCodeService.$inject = ['$resource', '$http'];

    function StockInBoxByCodeService ($resource, $http) {
        var service = $resource('api/frozen-boxes/code/:code', {}, {
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
