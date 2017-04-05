/**
 * Created by gaokangkang on 2017/4/5.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('IncompleteBoxService', IncompleteBoxService);

    IncompleteBoxService.$inject = ['$resource'];

    function IncompleteBoxService ($resource) {
        var service = $resource('api/temp/frozen-boxes/incomplete-boxes/project/:projectCode/type/:sampleType', {}, {
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
