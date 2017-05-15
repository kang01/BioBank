(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutTask', StockOutTask);

    StockOutTask.$inject = ['$resource', 'DateUtils'];

    function StockOutTask ($resource, DateUtils) {
        var resourceUrl =  'api/stock-out-tasks/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.stockOutDate = DateUtils.convertLocalDateFromServer(data.stockOutDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.stockOutDate = DateUtils.convertLocalDateToServer(copy.stockOutDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.stockOutDate = DateUtils.convertLocalDateToServer(copy.stockOutDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
