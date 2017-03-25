/**
 * Created by gaokangkang on 2017/3/21.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TransportRecordService', TransportRecordService);

    TransportRecordService.$inject = ['$resource', '$http'];

    function TransportRecordService ($resource, $http) {
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

        service.getJqDataTableValues = function(data, oSettings){
            var req = {
                method: 'POST',
                url: 'api/res/tranships',
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(data)
            }

            return $http(req);
        };

        function getConfigPropsComplete (response) {
            var properties = [];
            angular.forEach(response.data, function (data) {
                properties.push(data);
            });
            var orderBy = $filter('orderBy');
            return orderBy(properties, 'prefix');
        }

        return service;
    }
})();
