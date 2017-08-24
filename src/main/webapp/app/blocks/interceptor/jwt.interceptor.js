(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('jwtInterceptor', jwtInterceptor);

    jwtInterceptor.$inject = ['$q', '$cookies'];

    function jwtInterceptor ($q, $cookies) {
        var service = {
            request: request,
            response: response
        };

        return service;

        function response (response) {
            // var headers = response.headers(); // 'Authorization'});
            // if (headers.length > 0){
            //     var jwt = response.headers('Authorization');
            //     if (jwt){
            //         $cookies.put('Authorization', jwt);
            //     }
            // }
            return response;
        }
        function request(config) {
            var jwt = $cookies.get('Authorization');
            if (jwt){
                config.headers['Authorization'] = jwt;
            }
            return config;
        };
    }
})();
