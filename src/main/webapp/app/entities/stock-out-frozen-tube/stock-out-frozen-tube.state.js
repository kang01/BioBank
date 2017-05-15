(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-frozen-tube', {
            parent: 'entity',
            url: '/stock-out-frozen-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutFrozenTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-frozen-tube/stock-out-frozen-tubes.html',
                    controller: 'StockOutFrozenTubeController',
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
                    $translatePartialLoader.addPart('stockOutFrozenTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-frozen-tube-detail', {
            parent: 'stock-out-frozen-tube',
            url: '/stock-out-frozen-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutFrozenTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-frozen-tube/stock-out-frozen-tube-detail.html',
                    controller: 'StockOutFrozenTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutFrozenTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutFrozenTube', function($stateParams, StockOutFrozenTube) {
                    return StockOutFrozenTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-frozen-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-frozen-tube-detail.edit', {
            parent: 'stock-out-frozen-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-tube/stock-out-frozen-tube-dialog.html',
                    controller: 'StockOutFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutFrozenTube', function(StockOutFrozenTube) {
                            return StockOutFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-frozen-tube.new', {
            parent: 'stock-out-frozen-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-tube/stock-out-frozen-tube-dialog.html',
                    controller: 'StockOutFrozenTubeDialogController',
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
                    $state.go('stock-out-frozen-tube', null, { reload: 'stock-out-frozen-tube' });
                }, function() {
                    $state.go('stock-out-frozen-tube');
                });
            }]
        })
        .state('stock-out-frozen-tube.edit', {
            parent: 'stock-out-frozen-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-tube/stock-out-frozen-tube-dialog.html',
                    controller: 'StockOutFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutFrozenTube', function(StockOutFrozenTube) {
                            return StockOutFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-frozen-tube', null, { reload: 'stock-out-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-frozen-tube.delete', {
            parent: 'stock-out-frozen-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-tube/stock-out-frozen-tube-delete-dialog.html',
                    controller: 'StockOutFrozenTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutFrozenTube', function(StockOutFrozenTube) {
                            return StockOutFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-frozen-tube', null, { reload: 'stock-out-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
