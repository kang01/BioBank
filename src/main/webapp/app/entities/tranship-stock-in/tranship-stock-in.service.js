(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('TranshipStockIn', TranshipStockIn);

    TranshipStockIn.$inject = ['$resource'];

    function TranshipStockIn ($resource) {
        var resourceUrl =  'api/tranship-stock-ins/:id';

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
