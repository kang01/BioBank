/**
 * Created by gaokangkang on 2017/6/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('box-inventory', {
                parent: 'bizs',
                url: '/box-inventory?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/list/box-list/box-inventory.html',
                        controller: 'BoxInventoryController',
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
            .state('box-movement', {
                parent: 'bizs',
                url: '/box-movement',
                params: {
                    selectedBox:null
                },
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/list/box-list/box-movement.html',
                        controller: 'BoxMovementController',
                        controllerAs: 'vm'
                    }
                },

                resolve: {
                    // pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    //     return {
                    //         page: PaginationUtil.parsePage($stateParams.page),
                    //         sort: $stateParams.sort,
                    //         predicate: PaginationUtil.parsePredicate($stateParams.sort),
                    //         ascending: PaginationUtil.parseAscending($stateParams.sort)
                    //     };
                    // }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }]

                }
            })
    }
})();
