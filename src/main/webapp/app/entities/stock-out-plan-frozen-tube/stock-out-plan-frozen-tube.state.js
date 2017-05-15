(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-plan-frozen-tube', {
            parent: 'entity',
            url: '/stock-out-plan-frozen-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutPlanFrozenTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-plan-frozen-tube/stock-out-plan-frozen-tubes.html',
                    controller: 'StockOutPlanFrozenTubeController',
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
                    $translatePartialLoader.addPart('stockOutPlanFrozenTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-plan-frozen-tube-detail', {
            parent: 'stock-out-plan-frozen-tube',
            url: '/stock-out-plan-frozen-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutPlanFrozenTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-plan-frozen-tube/stock-out-plan-frozen-tube-detail.html',
                    controller: 'StockOutPlanFrozenTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutPlanFrozenTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutPlanFrozenTube', function($stateParams, StockOutPlanFrozenTube) {
                    return StockOutPlanFrozenTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-plan-frozen-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-plan-frozen-tube-detail.edit', {
            parent: 'stock-out-plan-frozen-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan-frozen-tube/stock-out-plan-frozen-tube-dialog.html',
                    controller: 'StockOutPlanFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutPlanFrozenTube', function(StockOutPlanFrozenTube) {
                            return StockOutPlanFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-plan-frozen-tube.new', {
            parent: 'stock-out-plan-frozen-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan-frozen-tube/stock-out-plan-frozen-tube-dialog.html',
                    controller: 'StockOutPlanFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                tubeRows: null,
                                tubeColumns: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-plan-frozen-tube', null, { reload: 'stock-out-plan-frozen-tube' });
                }, function() {
                    $state.go('stock-out-plan-frozen-tube');
                });
            }]
        })
        .state('stock-out-plan-frozen-tube.edit', {
            parent: 'stock-out-plan-frozen-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan-frozen-tube/stock-out-plan-frozen-tube-dialog.html',
                    controller: 'StockOutPlanFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutPlanFrozenTube', function(StockOutPlanFrozenTube) {
                            return StockOutPlanFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-plan-frozen-tube', null, { reload: 'stock-out-plan-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-plan-frozen-tube.delete', {
            parent: 'stock-out-plan-frozen-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan-frozen-tube/stock-out-plan-frozen-tube-delete-dialog.html',
                    controller: 'StockOutPlanFrozenTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutPlanFrozenTube', function(StockOutPlanFrozenTube) {
                            return StockOutPlanFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-plan-frozen-tube', null, { reload: 'stock-out-plan-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
