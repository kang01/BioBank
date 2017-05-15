(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StockOutApply', StockOutApply);

    StockOutApply.$inject = ['$resource', 'DateUtils'];

    function StockOutApply ($resource, DateUtils) {
        var resourceUrl =  'api/stock-out-applies/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.startTime = DateUtils.convertLocalDateFromServer(data.startTime);
                        data.endTime = DateUtils.convertLocalDateFromServer(data.endTime);
                        data.recordTime = DateUtils.convertLocalDateFromServer(data.recordTime);
                        data.approveTime = DateUtils.convertLocalDateFromServer(data.approveTime);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startTime = DateUtils.convertLocalDateToServer(copy.startTime);
                    copy.endTime = DateUtils.convertLocalDateToServer(copy.endTime);
                    copy.recordTime = DateUtils.convertLocalDateToServer(copy.recordTime);
                    copy.approveTime = DateUtils.convertLocalDateToServer(copy.approveTime);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.startTime = DateUtils.convertLocalDateToServer(copy.startTime);
                    copy.endTime = DateUtils.convertLocalDateToServer(copy.endTime);
                    copy.recordTime = DateUtils.convertLocalDateToServer(copy.recordTime);
                    copy.approveTime = DateUtils.convertLocalDateToServer(copy.approveTime);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
