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
            .state('tube-box-table', {
                parent: 'demos',
                url: '/tube-box-table',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'tube-box-table'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/demos/tube-box-table/tube-box-table.html',
                        controller: 'DemoTubeBoxTableController',
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
