/**
 * Created by gaokangkang on 2017/9/25.
 * 所有设备列表
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('EquipmentAllService', EquipmentAllService);

    EquipmentAllService.$inject = ['$resource'];

    function EquipmentAllService ($resource) {
        var resourceUrl =  'api/equipment/all/:id';

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
