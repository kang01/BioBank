(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('BoxAndTube', BoxAndTube);

    BoxAndTube.$inject = ['$resource'];

    function BoxAndTube ($resource) {
        var resourceUrl =  'api/box-and-tubes/:id';

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
