(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutReqFrozenTube', StockOutReqFrozenTube);

    StockOutReqFrozenTube.$inject = ['$resource'];

    function StockOutReqFrozenTube ($resource) {
        var resourceUrl =  'api/stock-out-req-frozen-tubes/:id';

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
