(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockInTubes', StockInTubes);

    StockInTubes.$inject = ['$resource'];

    function StockInTubes ($resource) {
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
