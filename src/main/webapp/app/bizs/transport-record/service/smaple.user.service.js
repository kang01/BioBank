/**
 * Created by gaokangkang on 2017/4/25.
 * 获得用户列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleUserService', SampleUserService);

    SampleUserService.$inject = ['$resource'];

    function SampleUserService ($resource) {
        var service = $resource('api/users/stockIn', {}, {
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
