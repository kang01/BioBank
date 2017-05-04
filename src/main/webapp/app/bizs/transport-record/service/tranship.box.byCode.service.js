/**
 * Created by gaokangkang on 2017/4/1.
 * 根据转运code获取冻存盒列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TranshipBoxByCodeService', TranshipBoxByCodeService);

    TranshipBoxByCodeService.$inject = ['$resource'];

    function TranshipBoxByCodeService ($resource) {
        var service = $resource('api/tranship-boxes/transhipCode/:code', {}, {
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
