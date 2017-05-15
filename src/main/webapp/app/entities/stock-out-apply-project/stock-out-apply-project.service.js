(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutApplyProject', StockOutApplyProject);

    StockOutApplyProject.$inject = ['$resource'];

    function StockOutApplyProject ($resource) {
        var resourceUrl =  'api/stock-out-apply-projects/:id';

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
