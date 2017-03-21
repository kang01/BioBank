(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('Tranship', Tranship);

    Tranship.$inject = ['$resource', 'DateUtils'];

    function Tranship ($resource, DateUtils) {
        var resourceUrl =  'api/tranships/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.transhipDate = DateUtils.convertLocalDateFromServer(data.transhipDate);
                        data.receiveDate = DateUtils.convertLocalDateFromServer(data.receiveDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.transhipDate = DateUtils.convertLocalDateToServer(copy.transhipDate);
                    copy.receiveDate = DateUtils.convertLocalDateToServer(copy.receiveDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.transhipDate = DateUtils.convertLocalDateToServer(copy.transhipDate);
                    copy.receiveDate = DateUtils.convertLocalDateToServer(copy.receiveDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
