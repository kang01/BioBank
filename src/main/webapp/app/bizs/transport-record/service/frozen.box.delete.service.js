/**
 * Created by gaokangkang on 2017/4/18.
 * 删除冻存盒
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('FrozenBoxDelService', FrozenBoxDelService);

    FrozenBoxDelService.$inject = ['$resource'];

    function FrozenBoxDelService ($resource) {
        var service = $resource('api/tranship-boxes/frozenBox/:code', {}, {
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
