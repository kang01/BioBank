/**
 * Created by gaokangkang on 2017/3/21.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TransportRecordService', TransportRecordService);

    TransportRecordService.$inject = ['$resource'];

    function TransportRecordService ($resource) {
        var service = $resource('api/temp/tranships', {}, {
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
