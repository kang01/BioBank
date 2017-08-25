/**
 * Created by zhuyu on 2017/3/10.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('jwt', {
                parent: 'demos',
                url: '/jwt',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'JWT'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/demos/jwt/jwt.html',
                        controller: 'DemoJWTController',
                        controllerAs: 'vm'
                    }
                },
                params: {

                },
                resolve: {
                }
            });

    }
})();
