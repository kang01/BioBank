/**
 * Created by gaokangkang on 2017/3/27.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('FrozenBoxByIdService', FrozenBoxByIdService);

    FrozenBoxByIdService.$inject = ['$resource'];

    function FrozenBoxByIdService ($resource) {
        var service = $resource('api/findFrozenBoxAndTubeByBoxId/:id', {}, {
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
