(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-box-tube', {
            parent: 'entity',
            url: '/stock-out-box-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutBoxTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-box-tube/stock-out-box-tubes.html',
                    controller: 'StockOutBoxTubeController',
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
                    $translatePartialLoader.addPart('stockOutBoxTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-box-tube-detail', {
            parent: 'stock-out-box-tube',
            url: '/stock-out-box-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutBoxTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-box-tube/stock-out-box-tube-detail.html',
                    controller: 'StockOutBoxTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutBoxTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutBoxTube', function($stateParams, StockOutBoxTube) {
                    return StockOutBoxTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-box-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-box-tube-detail.edit', {
            parent: 'stock-out-box-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-tube/stock-out-box-tube-dialog.html',
                    controller: 'StockOutBoxTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutBoxTube', function(StockOutBoxTube) {
                            return StockOutBoxTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-box-tube.new', {
            parent: 'stock-out-box-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-tube/stock-out-box-tube-dialog.html',
                    controller: 'StockOutBoxTubeDialogController',
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
                    $state.go('stock-out-box-tube', null, { reload: 'stock-out-box-tube' });
                }, function() {
                    $state.go('stock-out-box-tube');
                });
            }]
        })
        .state('stock-out-box-tube.edit', {
            parent: 'stock-out-box-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-tube/stock-out-box-tube-dialog.html',
                    controller: 'StockOutBoxTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutBoxTube', function(StockOutBoxTube) {
                            return StockOutBoxTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-box-tube', null, { reload: 'stock-out-box-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-box-tube.delete', {
            parent: 'stock-out-box-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-tube/stock-out-box-tube-delete-dialog.html',
                    controller: 'StockOutBoxTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutBoxTube', function(StockOutBoxTube) {
                            return StockOutBoxTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-box-tube', null, { reload: 'stock-out-box-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
