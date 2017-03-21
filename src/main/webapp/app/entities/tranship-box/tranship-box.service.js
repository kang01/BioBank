(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('TranshipBox', TranshipBox);

    TranshipBox.$inject = ['$resource'];

    function TranshipBox ($resource) {
        var resourceUrl =  'api/tranship-boxes/:id';

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
