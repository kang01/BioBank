(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('EquipmentModle', EquipmentModle);

    EquipmentModle.$inject = ['$resource'];

    function EquipmentModle ($resource) {
        var resourceUrl =  'api/equipment-modles/:id';

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
