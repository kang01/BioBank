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
            .state('take-over-list', {
                parent: 'bizs',
                url: '/take-over-list?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/take-over/take-over-list.html',
                        controller: 'TakeOverListController',
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
            .state('take-over-new', {
                parent: 'bizs',
                url: '/take-over-list/new',
                params:{
                    applyId:null,
                    planId:null,
                    taskId:null
                },
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/take-over/take-over-detail.html',
                        controller: 'TakeOverDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {

                        };
                    }
                }
            })
            .state('take-over-edit', {
                parent: 'bizs',
                url: '/take-over-list/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/take-over/take-over-detail.html',
                        controller: 'TakeOverDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TakeOverService', function($stateParams, TakeOverService) {
                        var id = $stateParams.id;
                        return TakeOverService.getTakeoverInfo(id);
                    }]
                }
            })
            .state('take-over-view', {
                parent: 'bizs',
                url: '/take-over-list/{id}/view',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/take-over/take-over-view.html',
                        controller: 'TakeOverViewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TakeOverService', function($stateParams, TakeOverService) {
                        var id = $stateParams.id;
                        return TakeOverService.getTakeoverInfo(id);
                    }]
                }
            })
    }
})();
