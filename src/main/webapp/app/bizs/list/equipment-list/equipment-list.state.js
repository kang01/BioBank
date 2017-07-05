/**
 * Created by gaokangkang on 2017/6/23.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('equipment-inventory', {
                parent: 'bizs',
                url: '/equipment-inventory?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/list/equipment-list/equipment-inventory.html',
                        controller: 'EquipmentInventoryController',
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
            .state('equipment-movement', {
                parent: 'bizs',
                url: '/equipment-movement',
                params: {
                    selectedEquipment:null
                },
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN']
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/list/equipment-list/equipment-movement.html',
                        controller: 'EquipmentMovementController',
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
