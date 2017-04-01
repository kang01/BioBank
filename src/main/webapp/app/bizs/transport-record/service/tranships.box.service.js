/**
 * Created by gaokangkang on 2017/4/1.
 * 保存转运冻存盒
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipBoxService', TranshipBoxService);

    TranshipBoxService.$inject = ['$resource'];

    function TranshipBoxService ($resource) {
        var service = $resource('api/tranship-boxes/batch', {}, {
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
