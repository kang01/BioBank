(function() {
    'use strict';
    angular
        .module('bioBankApp')
        .factory('UserLoginHistory', UserLoginHistory);

    UserLoginHistory.$inject = ['$resource', 'DateUtils'];

    function UserLoginHistory ($resource, DateUtils) {
        var resourceUrl =  'api/user-login-histories/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.invalidDate = DateUtils.convertDateTimeFromServer(data.invalidDate);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
