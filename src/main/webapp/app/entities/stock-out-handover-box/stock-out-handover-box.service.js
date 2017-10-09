(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutHandoverBox', StockOutHandoverBox);

    StockOutHandoverBox.$inject = ['$resource'];

    function StockOutHandoverBox ($resource) {
        var resourceUrl =  'api/stock-out-handover-boxes/:id';

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
