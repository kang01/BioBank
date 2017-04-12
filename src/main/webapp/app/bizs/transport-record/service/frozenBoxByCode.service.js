/**
 * Created by gaokangkang on 2017/3/29.
 * 根据冻存盒code字符串查询冻存盒和冻存管信息
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('frozenBoxByCodeService', frozenBoxByCodeService);

    frozenBoxByCodeService.$inject = ['$resource', '$http'];

    function frozenBoxByCodeService ($resource, $http) {
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

        service.queryByCodes = function (codes){
            var codeStr = (codes||[]).join(',');

            // For Testing;
            codeStr = '32,23432,123456,245';
            return $http.get('api/frozen-boxes/code/' + codeStr);
        };

        return service;
    }
})();
