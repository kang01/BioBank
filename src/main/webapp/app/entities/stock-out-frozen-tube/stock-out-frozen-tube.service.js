(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutFrozenTube', StockOutFrozenTube);

    StockOutFrozenTube.$inject = ['$resource'];

    function StockOutFrozenTube ($resource) {
        var resourceUrl =  'api/stock-out-frozen-tubes/:id';

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
