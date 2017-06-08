(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('authExpiredInterceptor', authExpiredInterceptor);

    authExpiredInterceptor.$inject = ['$rootScope', '$q', '$injector'];

    function authExpiredInterceptor($rootScope, $q, $injector) {
        var service = {
            responseError: responseError
        };

        return service;

        function responseError(response) {
            // If we have an unauthorized request we redirect to the login page
            // Don't do this check on the account API to avoid infinite loop
            var isForbidden = response.status === 403 && angular.isDefined(response.data.path) && response.data.path.indexOf('/api/') !== -1;
            isForbidden |= response.status === 500 && angular.isDefined(response.data.exception) && response.data.exception.indexOf('AccessDeniedException') !== -1;
            var isUnauthorized = response.status === 401 && angular.isDefined(response.data.path) && response.data.path.indexOf('/api/account') === -1;
            if (isUnauthorized || isForbidden) {
                var Auth = $injector.get('Auth');
                var to = $rootScope.toState;
                var params = $rootScope.toStateParams;
                Auth.logout();
                if (to.name !== 'accessdenied') {
                    Auth.storePreviousState(to.name, params);
                }
                var LoginService = $injector.get('LoginService');
                LoginService.open();
            }
            return $q.reject(response);
        }
    }
})();
