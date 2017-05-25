(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-req-frozen-tube', {
            parent: 'entity',
            url: '/stock-out-req-frozen-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutReqFrozenTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-req-frozen-tube/stock-out-req-frozen-tubes.html',
                    controller: 'StockOutReqFrozenTubeController',
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
                    $translatePartialLoader.addPart('stockOutReqFrozenTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-req-frozen-tube-detail', {
            parent: 'stock-out-req-frozen-tube',
            url: '/stock-out-req-frozen-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutReqFrozenTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-req-frozen-tube/stock-out-req-frozen-tube-detail.html',
                    controller: 'StockOutReqFrozenTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutReqFrozenTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutReqFrozenTube', function($stateParams, StockOutReqFrozenTube) {
                    return StockOutReqFrozenTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-req-frozen-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-req-frozen-tube-detail.edit', {
            parent: 'stock-out-req-frozen-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-req-frozen-tube/stock-out-req-frozen-tube-dialog.html',
                    controller: 'StockOutReqFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutReqFrozenTube', function(StockOutReqFrozenTube) {
                            return StockOutReqFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-req-frozen-tube.new', {
            parent: 'stock-out-req-frozen-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-req-frozen-tube/stock-out-req-frozen-tube-dialog.html',
                    controller: 'StockOutReqFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                memo: null,
                                tubeRows: null,
                                tubeColumns: null,
                                importingSampleId: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-req-frozen-tube', null, { reload: 'stock-out-req-frozen-tube' });
                }, function() {
                    $state.go('stock-out-req-frozen-tube');
                });
            }]
        })
        .state('stock-out-req-frozen-tube.edit', {
            parent: 'stock-out-req-frozen-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-req-frozen-tube/stock-out-req-frozen-tube-dialog.html',
                    controller: 'StockOutReqFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutReqFrozenTube', function(StockOutReqFrozenTube) {
                            return StockOutReqFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-req-frozen-tube', null, { reload: 'stock-out-req-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-req-frozen-tube.delete', {
            parent: 'stock-out-req-frozen-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-req-frozen-tube/stock-out-req-frozen-tube-delete-dialog.html',
                    controller: 'StockOutReqFrozenTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutReqFrozenTube', function(StockOutReqFrozenTube) {
                            return StockOutReqFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-req-frozen-tube', null, { reload: 'stock-out-req-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
