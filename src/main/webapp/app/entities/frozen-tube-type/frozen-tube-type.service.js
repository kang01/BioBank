(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenTubeType', FrozenTubeType);

    FrozenTubeType.$inject = ['$resource'];

    function FrozenTubeType ($resource) {
        var resourceUrl =  'api/frozen-tube-types/:id';

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
