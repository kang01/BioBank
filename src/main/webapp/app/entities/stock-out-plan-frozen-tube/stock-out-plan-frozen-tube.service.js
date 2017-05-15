(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutPlanFrozenTube', StockOutPlanFrozenTube);

    StockOutPlanFrozenTube.$inject = ['$resource'];

    function StockOutPlanFrozenTube ($resource) {
        var resourceUrl =  'api/stock-out-plan-frozen-tubes/:id';

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
