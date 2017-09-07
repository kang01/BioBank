(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('Province', Province);

    Province.$inject = ['$resource'];

    function Province ($resource) {
        var resourceUrl =  'api/provinces/:id';

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
