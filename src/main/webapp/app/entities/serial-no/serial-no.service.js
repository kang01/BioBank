(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('SerialNo', SerialNo);

    SerialNo.$inject = ['$resource', 'DateUtils'];

    function SerialNo ($resource, DateUtils) {
        var resourceUrl =  'api/serial-nos/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.usedDate = DateUtils.convertLocalDateFromServer(data.usedDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.usedDate = DateUtils.convertLocalDateToServer(copy.usedDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.usedDate = DateUtils.convertLocalDateToServer(copy.usedDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
