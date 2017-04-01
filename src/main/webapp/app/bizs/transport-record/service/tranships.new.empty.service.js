/**
 * Created by gaokangkang on 2017/4/1.
 * 新增转运记录
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipNewEmptyService', TranshipNewEmptyService);

    TranshipNewEmptyService.$inject = ['$resource'];

    function TranshipNewEmptyService ($resource) {
        var service = $resource('api/tranships/new-empty', {}, {
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
