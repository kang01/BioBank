(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('PositionDestroy', PositionDestroy);

    PositionDestroy.$inject = ['$resource'];

    function PositionDestroy ($resource) {
        var resourceUrl =  'api/position-destroys/:id';

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
