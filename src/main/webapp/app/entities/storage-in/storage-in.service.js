(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('StorageIn', StorageIn);

    StorageIn.$inject = ['$resource', 'DateUtils'];

    function StorageIn ($resource, DateUtils) {
        var resourceUrl =  'api/storage-ins/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.receiveDate = DateUtils.convertLocalDateFromServer(data.receiveDate);
                        data.storageInDate = DateUtils.convertLocalDateFromServer(data.storageInDate);
                        data.signDate = DateUtils.convertLocalDateFromServer(data.signDate);
                    }
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.receiveDate = DateUtils.convertLocalDateToServer(copy.receiveDate);
                    copy.storageInDate = DateUtils.convertLocalDateToServer(copy.storageInDate);
                    copy.signDate = DateUtils.convertLocalDateToServer(copy.signDate);
                    return angular.toJson(copy);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    var copy = angular.copy(data);
                    copy.receiveDate = DateUtils.convertLocalDateToServer(copy.receiveDate);
                    copy.storageInDate = DateUtils.convertLocalDateToServer(copy.storageInDate);
                    copy.signDate = DateUtils.convertLocalDateToServer(copy.signDate);
                    return angular.toJson(copy);
                }
            }
        });
    }
})();
