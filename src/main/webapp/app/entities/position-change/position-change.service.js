(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('PositionChange', PositionChange);

    PositionChange.$inject = ['$resource'];

    function PositionChange ($resource) {
        var resourceUrl =  'api/position-changes/:id';

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
