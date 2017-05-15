(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-required-sample', {
            parent: 'entity',
            url: '/stock-out-required-sample?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutRequiredSample.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-required-sample/stock-out-required-samples.html',
                    controller: 'StockOutRequiredSampleController',
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
                },
                search: null
            },
            resolve: {
                pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                    return {
                        page: PaginationUtil.parsePage($stateParams.page),
                        sort: $stateParams.sort,
                        predicate: PaginationUtil.parsePredicate($stateParams.sort),
                        ascending: PaginationUtil.parseAscending($stateParams.sort),
                        search: $stateParams.search
                    };
                }],
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutRequiredSample');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-required-sample-detail', {
            parent: 'stock-out-required-sample',
            url: '/stock-out-required-sample/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutRequiredSample.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-required-sample/stock-out-required-sample-detail.html',
                    controller: 'StockOutRequiredSampleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutRequiredSample');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutRequiredSample', function($stateParams, StockOutRequiredSample) {
                    return StockOutRequiredSample.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-required-sample',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-required-sample-detail.edit', {
            parent: 'stock-out-required-sample-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-required-sample/stock-out-required-sample-dialog.html',
                    controller: 'StockOutRequiredSampleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutRequiredSample', function(StockOutRequiredSample) {
                            return StockOutRequiredSample.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-required-sample.new', {
            parent: 'stock-out-required-sample',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-required-sample/stock-out-required-sample-dialog.html',
                    controller: 'StockOutRequiredSampleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sampleCode: null,
                                sampleType: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-required-sample', null, { reload: 'stock-out-required-sample' });
                }, function() {
                    $state.go('stock-out-required-sample');
                });
            }]
        })
        .state('stock-out-required-sample.edit', {
            parent: 'stock-out-required-sample',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-required-sample/stock-out-required-sample-dialog.html',
                    controller: 'StockOutRequiredSampleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutRequiredSample', function(StockOutRequiredSample) {
                            return StockOutRequiredSample.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-required-sample', null, { reload: 'stock-out-required-sample' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-required-sample.delete', {
            parent: 'stock-out-required-sample',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-required-sample/stock-out-required-sample-delete-dialog.html',
                    controller: 'StockOutRequiredSampleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutRequiredSample', function(StockOutRequiredSample) {
                            return StockOutRequiredSample.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-required-sample', null, { reload: 'stock-out-required-sample' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
