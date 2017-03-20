(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('FrozenTubeRecord', FrozenTubeRecord);

    FrozenTubeRecord.$inject = ['$resource'];

    function FrozenTubeRecord ($resource) {
        var resourceUrl =  'api/frozen-tube-records/:id';

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
