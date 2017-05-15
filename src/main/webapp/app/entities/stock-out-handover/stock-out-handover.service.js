(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutHandover', StockOutHandover);

    StockOutHandover.$inject = ['$resource', 'DateUtils'];

    function StockOutHandover ($resource, DateUtils) {
        var resourceUrl =  'api/stock-out-handovers/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.handoverTime = DateUtils.convertLocalDateFromServer(data.handoverTime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.handoverTime = DateUtils.convertLocalDateToServer(copy.handoverTime);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.handoverTime = DateUtils.convertLocalDateToServer(copy.handoverTime);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
