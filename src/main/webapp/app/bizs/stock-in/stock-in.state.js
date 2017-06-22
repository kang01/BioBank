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
            .state('stock-in', {
                parent: 'bizs',
                url: '/stock-in?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockIn.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-in/stock-in-table.html',
                        controller: 'StockInController',
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
                        $translatePartialLoader.addPart('stock-in-record');
                        return $translate.refresh();
                    }]

                }
            })
            .state('stock-in-new', {
                parent: 'bizs',
                url: '/stock-in/add',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockIn.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-in/stock-in-new.html',
                        controller: 'StockInNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stock-in-record');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {};
                    }

                }
            })
            .state('stock-in-add-box-edit', {
                parent: 'bizs',
                url: '/stock-in-add-box/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockIn.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-in/stock-in-new.html',
                        controller: 'StockInNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stock-in-record');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockInService', function($stateParams, StockInService) {
                        return StockInService.get({id : $stateParams.id}).$promise;
                    }]

                }
            })
            .state('stock-in-edit', {
                parent: 'bizs',
                url: '/stock-in/{id}/edit',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockIn.edit.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-in/stock-in-edit.html',
                        controller: 'StockInEditController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('stock-in-record');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'StockInService', function($stateParams, StockInService) {
                        return StockInService.get({id : $stateParams.id}).$promise;
                    }]
                }
            });
    }
})();
