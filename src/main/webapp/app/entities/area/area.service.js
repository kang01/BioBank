(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('Area', Area);

    Area.$inject = ['$resource'];

    function Area ($resource) {
        var resourceUrl =  'api/areas/:id';

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
