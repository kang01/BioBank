(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StorageInBox', StorageInBox);

    StorageInBox.$inject = ['$resource'];

    function StorageInBox ($resource) {
        var resourceUrl =  'api/storage-in-boxes/:id';

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
