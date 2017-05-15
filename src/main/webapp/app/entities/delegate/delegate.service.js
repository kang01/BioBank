(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('Delegate', Delegate);

    Delegate.$inject = ['$resource'];

    function Delegate ($resource) {
        var resourceUrl =  'api/delegates/:id';

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
