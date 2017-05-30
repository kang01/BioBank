(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-task-frozen-tube', {
            parent: 'entity',
            url: '/stock-out-task-frozen-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutTaskFrozenTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-task-frozen-tube/stock-out-task-frozen-tubes.html',
                    controller: 'StockOutTaskFrozenTubeController',
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
                    $translatePartialLoader.addPart('stockOutTaskFrozenTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-task-frozen-tube-detail', {
            parent: 'stock-out-task-frozen-tube',
            url: '/stock-out-task-frozen-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutTaskFrozenTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-task-frozen-tube/stock-out-task-frozen-tube-detail.html',
                    controller: 'StockOutTaskFrozenTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutTaskFrozenTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutTaskFrozenTube', function($stateParams, StockOutTaskFrozenTube) {
                    return StockOutTaskFrozenTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-task-frozen-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-task-frozen-tube-detail.edit', {
            parent: 'stock-out-task-frozen-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task-frozen-tube/stock-out-task-frozen-tube-dialog.html',
                    controller: 'StockOutTaskFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutTaskFrozenTube', function(StockOutTaskFrozenTube) {
                            return StockOutTaskFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-task-frozen-tube.new', {
            parent: 'stock-out-task-frozen-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task-frozen-tube/stock-out-task-frozen-tube-dialog.html',
                    controller: 'StockOutTaskFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-task-frozen-tube', null, { reload: 'stock-out-task-frozen-tube' });
                }, function() {
                    $state.go('stock-out-task-frozen-tube');
                });
            }]
        })
        .state('stock-out-task-frozen-tube.edit', {
            parent: 'stock-out-task-frozen-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task-frozen-tube/stock-out-task-frozen-tube-dialog.html',
                    controller: 'StockOutTaskFrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutTaskFrozenTube', function(StockOutTaskFrozenTube) {
                            return StockOutTaskFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-task-frozen-tube', null, { reload: 'stock-out-task-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-task-frozen-tube.delete', {
            parent: 'stock-out-task-frozen-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task-frozen-tube/stock-out-task-frozen-tube-delete-dialog.html',
                    controller: 'StockOutTaskFrozenTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutTaskFrozenTube', function(StockOutTaskFrozenTube) {
                            return StockOutTaskFrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-task-frozen-tube', null, { reload: 'stock-out-task-frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
