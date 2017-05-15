(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-box-position', {
            parent: 'entity',
            url: '/stock-out-box-position?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutBoxPosition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-box-position/stock-out-box-positions.html',
                    controller: 'StockOutBoxPositionController',
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
                    $translatePartialLoader.addPart('stockOutBoxPosition');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-box-position-detail', {
            parent: 'stock-out-box-position',
            url: '/stock-out-box-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutBoxPosition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-box-position/stock-out-box-position-detail.html',
                    controller: 'StockOutBoxPositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutBoxPosition');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutBoxPosition', function($stateParams, StockOutBoxPosition) {
                    return StockOutBoxPosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-box-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-box-position-detail.edit', {
            parent: 'stock-out-box-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-position/stock-out-box-position-dialog.html',
                    controller: 'StockOutBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutBoxPosition', function(StockOutBoxPosition) {
                            return StockOutBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-box-position.new', {
            parent: 'stock-out-box-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-position/stock-out-box-position-dialog.html',
                    controller: 'StockOutBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                equipmentCode: null,
                                areaCode: null,
                                supportRackCode: null,
                                rowsInShelf: null,
                                columnsInShelf: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-box-position', null, { reload: 'stock-out-box-position' });
                }, function() {
                    $state.go('stock-out-box-position');
                });
            }]
        })
        .state('stock-out-box-position.edit', {
            parent: 'stock-out-box-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-position/stock-out-box-position-dialog.html',
                    controller: 'StockOutBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutBoxPosition', function(StockOutBoxPosition) {
                            return StockOutBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-box-position', null, { reload: 'stock-out-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-box-position.delete', {
            parent: 'stock-out-box-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-box-position/stock-out-box-position-delete-dialog.html',
                    controller: 'StockOutBoxPositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutBoxPosition', function(StockOutBoxPosition) {
                            return StockOutBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-box-position', null, { reload: 'stock-out-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
