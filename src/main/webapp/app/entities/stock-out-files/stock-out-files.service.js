(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutFiles', StockOutFiles);

    StockOutFiles.$inject = ['$resource'];

    function StockOutFiles ($resource) {
        var resourceUrl =  'api/stock-out-files/:id';

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
