(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenTube', FrozenTube);

    FrozenTube.$inject = ['$resource'];

    function FrozenTube ($resource) {
        var resourceUrl =  'api/frozen-tubes/:id';

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
