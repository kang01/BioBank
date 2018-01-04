/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('give-back-table', {
                parent: 'bizs',
                url: '/give-back-table',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/give-back/give-back-table.html',
                        controller: 'GiveBackTableController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]

                }
            })
            .state('give-back-detail', {
                parent: 'bizs',
                url: '/give-back-detail/{giveBackId}',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                params:{
                    giveBackId:null
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/give-back/give-back-record-detail.html',
                        controller: 'GiveBackDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]

                }
            })
            .state('give-back-view', {
                parent: 'bizs',
                url: '/give-back-view/{giveBackId}',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/give-back/give-back-view.html',
                        controller: 'GiveBackViewController',
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
