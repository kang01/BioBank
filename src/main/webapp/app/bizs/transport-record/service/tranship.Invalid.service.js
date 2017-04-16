/**
 * Created by gaokangkang on 2017/4/14.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipInvalidService', TranshipInvalidService);

    TranshipInvalidService.$inject = ['$resource'];

    function TranshipInvalidService ($resource) {
        var service = $resource('api/tranships/invalid/:transhipCode', {}, {
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
        service.saveStockIn = function(transhipCode){
            var ajaxUrl = 'api/tranships/invalid/'+transhipCode;

            var req = {
                method: 'PUT',
                url: ajaxUrl,
                headers: {
                    'Content-Type': 'application/json'
                }
            };

            return $http(req);
        };

        return service;
    }
})();
