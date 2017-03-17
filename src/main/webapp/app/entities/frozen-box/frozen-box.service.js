(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenBox', FrozenBox);

    FrozenBox.$inject = ['$resource'];

    function FrozenBox ($resource) {
        var resourceUrl =  'api/frozen-boxes/:id';

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
