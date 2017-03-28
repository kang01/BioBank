/**
 * Created by gaokangkang on 2017/3/28.
 * 设备下的区域列表
 */
(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('AreasByEquipmentIdService', AreasByEquipmentIdService);

    AreasByEquipmentIdService.$inject = ['$resource'];

    function AreasByEquipmentIdService ($resource) {
        var resourceUrl =  'api/areasByEquipmentId/:id';

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
