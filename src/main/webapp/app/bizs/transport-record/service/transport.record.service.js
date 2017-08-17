/**
 * Created by gaokangkang on 2017/3/21.
 * 转运记录列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TransportRecordService', TransportRecordService);

    TransportRecordService.$inject = ['$resource', '$http','$q'];

    function TransportRecordService ($resource, $http,$q) {
        var service = $resource('api/tranships/id/:id', {}, {
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
            };

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
        service.uploadTransportRecord = function (transhipId,fb) {
            var req = {
                method: 'POST',
                url: 'api/tranships/'+transhipId+'/upload',
                headers: {
                    'Content-Type': undefined
                },
                data: fb
            };
            return $http(req);
        };
        service.queryTransportRecordFile = function (transportCode) {
            return $http.get('api/attachments/transhipCode/'+transportCode);
        };
        service.editTransportRecordFile = function (param) {
            return $http.put('api/attachments/',param);
        };
        service.delTransportRecordFile = function (id) {
            return $http.delete('api/attachments/'+id);
        };
        service.importData = function (frozenBoxCodeStr,canceller) {
            var opt =null;
            if(canceller){
                opt = {timeout: canceller.promise};
            }
            return $http.get('api/tranship-boxes/frozenBoxCode/'+frozenBoxCodeStr+'/import', opt);
        };
        service.uploadBox = function (file,projectCode) {
            var fb = new FormData();
            fb.append('file',file);
            var req = {
                method: 'POST',
                url: 'api/tranship-boxes/projectCode/'+projectCode+'/upload',
                headers: {
                    'Content-Type': undefined
                },
                data: fb
            };
            return $http(req);
        };
        return service;
    }
})();
