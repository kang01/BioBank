(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutTaskFrozenTube', StockOutTaskFrozenTube);

    StockOutTaskFrozenTube.$inject = ['$resource'];

    function StockOutTaskFrozenTube ($resource) {
        var resourceUrl =  'api/stock-out-task-frozen-tubes/:id';

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
