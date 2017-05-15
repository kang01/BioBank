(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutRequirement', StockOutRequirement);

    StockOutRequirement.$inject = ['$resource'];

    function StockOutRequirement ($resource) {
        var resourceUrl =  'api/stock-out-requirements/:id';

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
