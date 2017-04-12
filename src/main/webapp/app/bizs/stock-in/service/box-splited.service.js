/**
 * Created by gaokangkang on 2017/4/10.
 * 分装入库盒子
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SplitedBoxService', SplitedBoxService);

    SplitedBoxService.$inject = ['$resource','$http'];

    function SplitedBoxService ($resource,$http) {
        var service = $resource('api/stock-in-boxes/stock-in/:stockInCode/box/:boxCode/splited', {}, {
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
        service.saveSplit = function(stockInCode,boxCode,boxList){
            var ajaxUrl = 'api/stock-in-boxes/stock-in/'+stockInCode +'/box/'+boxCode+'/splited';

            var req = {
                method: 'PUT',
                url: ajaxUrl,
                headers: {
                    'Content-Type': 'application/json'
                },
                data:boxList
            };

            return $http(req);
        };
        return service;
    }
})();
