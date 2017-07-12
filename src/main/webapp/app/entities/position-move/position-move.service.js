(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('PositionMove', PositionMove);

    PositionMove.$inject = ['$resource'];

    function PositionMove ($resource) {
        var resourceUrl =  'api/position-moves/:id';

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
