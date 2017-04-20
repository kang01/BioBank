(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenBoxPosition', FrozenBoxPosition);

    FrozenBoxPosition.$inject = ['$resource'];

    function FrozenBoxPosition ($resource) {
        var resourceUrl =  'api/frozen-box-positions/:id';

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
