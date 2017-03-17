(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('Relations', Relations);

    Relations.$inject = ['$resource'];

    function Relations ($resource) {
        var resourceUrl =  'api/relations/:id';

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
