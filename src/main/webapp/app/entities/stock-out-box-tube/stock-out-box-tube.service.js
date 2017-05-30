(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutBoxTube', StockOutBoxTube);

    StockOutBoxTube.$inject = ['$resource'];

    function StockOutBoxTube ($resource) {
        var resourceUrl =  'api/stock-out-box-tubes/:id';

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
