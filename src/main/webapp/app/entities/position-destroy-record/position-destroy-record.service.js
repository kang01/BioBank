(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('PositionDestroyRecord', PositionDestroyRecord);

    PositionDestroyRecord.$inject = ['$resource'];

    function PositionDestroyRecord ($resource) {
        var resourceUrl =  'api/position-destroy-records/:id';

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
