(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutHandoverDetails', StockOutHandoverDetails);

    StockOutHandoverDetails.$inject = ['$resource'];

    function StockOutHandoverDetails ($resource) {
        var resourceUrl =  'api/stock-out-handover-details/:id';

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
