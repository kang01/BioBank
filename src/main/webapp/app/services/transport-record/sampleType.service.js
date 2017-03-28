/**
 * Created by gaokangkang on 2017/3/25.
 * 样本类型
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('SampleTypeService', SampleTypeService);

    SampleTypeService.$inject = ['$resource'];

    function SampleTypeService ($resource) {
        var service = $resource('api/sampleTypes', {}, {
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
