/**
 * Created by gaokangkang on 2017/4/1.
 * 新增转运记录
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipNewEmptyService', TranshipNewEmptyService);

    TranshipNewEmptyService.$inject = ['$resource','$http'];

    function TranshipNewEmptyService ($resource,$http) {
        var service = $resource('api/tranships/new-empty/', {}, {
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
        service.saveTransportEmpty = function(projectId,projectSiteId){
            var ajaxUrl = 'api/tranships/new-empty/'+projectId+'/'+projectSiteId;
            var req = {
                method: 'POST',
                url: ajaxUrl,
                headers: {
                    'Content-Type': 'application/json'
                },
                data:{}
            };

            return $http(req);
        };

        return service;
    }
})();
