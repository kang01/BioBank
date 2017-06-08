(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockInTube', StockInTube);

    StockInTube.$inject = ['$resource'];

    function StockInTube ($resource) {
        var resourceUrl =  'api/stock-in-tubes/:id';

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
