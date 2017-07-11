(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('PositionMoveRecord', PositionMoveRecord);

    PositionMoveRecord.$inject = ['$resource'];

    function PositionMoveRecord ($resource) {
        var resourceUrl =  'api/position-move-records/:id';

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
