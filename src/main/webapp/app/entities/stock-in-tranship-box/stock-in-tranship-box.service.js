(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockInTranshipBox', StockInTranshipBox);

    StockInTranshipBox.$inject = ['$resource'];

    function StockInTranshipBox ($resource) {
        var resourceUrl =  'api/stock-in-tranship-boxes/:id';

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
