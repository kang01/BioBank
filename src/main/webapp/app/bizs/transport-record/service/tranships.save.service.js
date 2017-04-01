/**
 * Created by gaokangkang on 2017/4/1.
 * 转运记录保存
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipSaveService', TranshipSaveService);

    TranshipSaveService.$inject = ['$resource'];

    function TranshipSaveService ($resource) {
        var service = $resource('api/tranships/update-object', {}, {
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
