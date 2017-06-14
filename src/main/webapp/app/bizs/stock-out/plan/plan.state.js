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
            .state('plan-list', {
                parent: 'bizs',
                url: '/plan?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/plan/plan-list.html',
                        controller: 'PlanListController',
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
            .state('plan-new', {
                parent: 'bizs',
                url: '/plan-list/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/plan/plan-detail.html',
                        controller: 'PlanDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }
            })
            .state('plan-edit', {
                parent: 'bizs',
                url: '/plan-list/{planId}/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/plan/plan-detail.html',
                        controller: 'PlanDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]
                }
            });
    }
})();
