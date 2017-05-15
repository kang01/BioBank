(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutPlan', StockOutPlan);

    StockOutPlan.$inject = ['$resource'];

    function StockOutPlan ($resource) {
        var resourceUrl =  'api/stock-out-plans/:id';

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
