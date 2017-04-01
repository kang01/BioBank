/**
 * Created by gaokangkang on 2017/3/29.
 * 根据冻存盒code字符串查询冻存盒和冻存管信息
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('frozenBoxByCodeService', frozenBoxByCodeService);

    frozenBoxByCodeService.$inject = ['$resource'];

    function frozenBoxByCodeService ($resource) {
        var service = $resource('api/temp/frozen-boxes/code/:code', {}, {
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
