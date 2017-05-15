(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutBoxPosition', StockOutBoxPosition);

    StockOutBoxPosition.$inject = ['$resource'];

    function StockOutBoxPosition ($resource) {
        var resourceUrl =  'api/stock-out-box-positions/:id';

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
