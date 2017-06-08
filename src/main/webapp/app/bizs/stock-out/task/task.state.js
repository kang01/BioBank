/**
 * Created by gaokangkang on 2017/5/12.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('task-list', {
                parent: 'bizs',
                url: '/task?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/task/task-list.html',
                        controller: 'TaskListController',
                        controllerAs: 'vm'
                    }
                },
                params: {
                    page: {
                        value: '1',
                        squash: true
                    },
                    sort: {
                        value: 'id,asc',
                        squash: true
                    }
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort)
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]

                }
            })
            .state('task-new', {
                parent: 'bizs',
                url: '/task-list/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/task/task-detail.html',
                        controller: 'TaskDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }
            })
            .state('task-edit', {
                parent: 'bizs',
                url: '/task-list/{taskId}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/task/task-detail.html',
                        controller: 'TaskDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }
            })
            .state('task-view', {
                parent: 'bizs',
                url: '/task-list/{taskId}/view',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/task/task-view.html',
                        controller: 'TaskViewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TaskService', function($stateParams, TaskService) {
                        var id = $stateParams.taskId;
                        return TaskService.queryTaskDesc(id)
                    }]
                }
            })
    }
})();
