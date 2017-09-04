/**
 * Created by gaokangkang on 2017/8/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('reports-home', {
                parent: 'bizs',
                url: '/reports',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '报表'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/reports/reports.html',
                        controller: 'ReportsController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {

                        return $translate.refresh();
                    }]

                }
            })
    }
})();
