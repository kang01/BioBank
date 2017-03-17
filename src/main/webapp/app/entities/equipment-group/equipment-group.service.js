(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('EquipmentGroup', EquipmentGroup);

    EquipmentGroup.$inject = ['$resource'];

    function EquipmentGroup ($resource) {
        var resourceUrl =  'api/equipment-groups/:id';

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
