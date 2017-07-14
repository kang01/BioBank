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
            .state('pageslide', {
                parent: 'demos',
                url: '/pageslide',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'pageslide'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/demos/pageslide/pageslide.html',
                        controller: 'DemoPageslideController',
                        controllerAs: 'vm'
                    }
                },
                params: {

                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }
            });

    }
})();
