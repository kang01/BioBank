(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockInBoxPosition', StockInBoxPosition);

    StockInBoxPosition.$inject = ['$resource'];

    function StockInBoxPosition ($resource) {
        var resourceUrl =  'api/stock-in-box-positions/:id';

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
