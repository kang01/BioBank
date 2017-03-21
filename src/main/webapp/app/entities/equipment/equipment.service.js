(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('Equipment', Equipment);

    Equipment.$inject = ['$resource'];

    function Equipment ($resource) {
        var resourceUrl =  'api/equipment/:id';

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
