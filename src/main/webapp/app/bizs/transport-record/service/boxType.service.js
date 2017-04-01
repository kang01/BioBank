/**
 * Created by gaokangkang on 2017/3/25.
 */
//盒子类型
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('FrozenBoxTypesService', FrozenBoxTypesService);

    FrozenBoxTypesService.$inject = ['$resource'];

    function FrozenBoxTypesService ($resource) {
        var service = $resource('api/frozen-box-types/all', {}, {
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
