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
            .state('project-management-info', {
                parent: 'project-management-add',
                url: '/info',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '项目信息'
                },
                views: {
                    'projectManagementContent': {
                        templateUrl: 'app/bizs/project-management/template/project-management-info.html',
                        controller: 'ProjectManagementInfoController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {

                        return $translate.refresh();
                    }]

                }
            })
            .state('project-management-sample-type', {
                parent: 'project-management-add',
                url: '/sample-type',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '样本分类'
                },
                views: {
                    'projectManagementContent': {
                        templateUrl: 'app/bizs/project-management/template/project-management-sample-type.html',
                        controller: 'ProjectManagementSampleTypeController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {

                        return $translate.refresh();
                    }]

                }
            })
            .state('project-management-sites', {
                parent: 'project-management-add',
                url: '/sites',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '项目点'
                },
                views: {
                    'projectManagementContent': {
                        templateUrl: 'app/bizs/project-management/template/project-management-sites.html',
                        controller: 'ProjectManagementSitesController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {

                        return $translate.refresh();
                    }]

                }
            })
            .state('project-management-equipment', {
                parent: 'project-management-add',
                url: '/equipment',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '设备'
                },
                views: {
                    'projectManagementContent': {
                        templateUrl: 'app/bizs/project-management/template/project-management-equipment.html',
                        controller: 'ProjectManagementEquipmentController',
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
