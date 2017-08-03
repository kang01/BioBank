(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('PositionChangeRecord', PositionChangeRecord);

    PositionChangeRecord.$inject = ['$resource'];

    function PositionChangeRecord ($resource) {
        var resourceUrl =  'api/position-change-records/:id';

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
