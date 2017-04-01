/**
 * Created by gaokangkang on 2017/3/28.
 * 所有设备列表
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('EquipmentService', EquipmentService);

    EquipmentService.$inject = ['$resource'];

    function EquipmentService ($resource) {
        var resourceUrl =  'api/equipment/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
