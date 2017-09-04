/**
 * Created by gaokangkang on 2017/8/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('project-management', {
                parent: 'bizs',
                url: '/project-management',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '项目管理'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/project-management/project-management.html',
                        controller: 'ProjectManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {

                        return $translate.refresh();
                    }]

                }
            })
            .state('project-management-add', {
                parent: 'bizs',
                url: '/project-management-add',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '项目管理'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/project-management/project-management-add.html',
                        controller: 'ProjectManagementAddController',
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
