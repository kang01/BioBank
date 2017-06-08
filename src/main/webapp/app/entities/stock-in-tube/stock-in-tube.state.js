(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-in-tube', {
            parent: 'entity',
            url: '/stock-in-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-tube/stock-in-tubes.html',
                    controller: 'StockInTubeController',
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
                    $translatePartialLoader.addPart('stockInTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-in-tube-detail', {
            parent: 'stock-in-tube',
            url: '/stock-in-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-tube/stock-in-tube-detail.html',
                    controller: 'StockInTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockInTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockInTube', function($stateParams, StockInTube) {
                    return StockInTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-in-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-in-tube-detail.edit', {
            parent: 'stock-in-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tube/stock-in-tube-dialog.html',
                    controller: 'StockInTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInTube', function(StockInTube) {
                            return StockInTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-tube.new', {
            parent: 'stock-in-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tube/stock-in-tube-dialog.html',
                    controller: 'StockInTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                rowsInTube: null,
                                columnsInTube: null,
                                status: null,
                                memo: null,
                                frozenBoxCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-in-tube', null, { reload: 'stock-in-tube' });
                }, function() {
                    $state.go('stock-in-tube');
                });
            }]
        })
        .state('stock-in-tube.edit', {
            parent: 'stock-in-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tube/stock-in-tube-dialog.html',
                    controller: 'StockInTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInTube', function(StockInTube) {
                            return StockInTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-tube', null, { reload: 'stock-in-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-tube.delete', {
            parent: 'stock-in-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tube/stock-in-tube-delete-dialog.html',
                    controller: 'StockInTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockInTube', function(StockInTube) {
                            return StockInTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-tube', null, { reload: 'stock-in-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
