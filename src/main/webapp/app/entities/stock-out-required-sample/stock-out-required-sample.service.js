(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutRequiredSample', StockOutRequiredSample);

    StockOutRequiredSample.$inject = ['$resource'];

    function StockOutRequiredSample ($resource) {
        var resourceUrl =  'api/stock-out-required-samples/:id';

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
