(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutFrozenBox', StockOutFrozenBox);

    StockOutFrozenBox.$inject = ['$resource'];

    function StockOutFrozenBox ($resource) {
        var resourceUrl =  'api/stock-out-frozen-boxes/:id';

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
