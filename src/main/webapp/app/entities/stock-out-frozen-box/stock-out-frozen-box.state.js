(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-frozen-box', {
            parent: 'entity',
            url: '/stock-out-frozen-box?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutFrozenBox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-frozen-box/stock-out-frozen-boxes.html',
                    controller: 'StockOutFrozenBoxController',
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
                    $translatePartialLoader.addPart('stockOutFrozenBox');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-frozen-box-detail', {
            parent: 'stock-out-frozen-box',
            url: '/stock-out-frozen-box/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutFrozenBox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-frozen-box/stock-out-frozen-box-detail.html',
                    controller: 'StockOutFrozenBoxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutFrozenBox');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutFrozenBox', function($stateParams, StockOutFrozenBox) {
                    return StockOutFrozenBox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-frozen-box',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-frozen-box-detail.edit', {
            parent: 'stock-out-frozen-box-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-box/stock-out-frozen-box-dialog.html',
                    controller: 'StockOutFrozenBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutFrozenBox', function(StockOutFrozenBox) {
                            return StockOutFrozenBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-frozen-box.new', {
            parent: 'stock-out-frozen-box',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-box/stock-out-frozen-box-dialog.html',
                    controller: 'StockOutFrozenBoxDialogController',
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
                    $state.go('stock-out-frozen-box', null, { reload: 'stock-out-frozen-box' });
                }, function() {
                    $state.go('stock-out-frozen-box');
                });
            }]
        })
        .state('stock-out-frozen-box.edit', {
            parent: 'stock-out-frozen-box',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-box/stock-out-frozen-box-dialog.html',
                    controller: 'StockOutFrozenBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutFrozenBox', function(StockOutFrozenBox) {
                            return StockOutFrozenBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-frozen-box', null, { reload: 'stock-out-frozen-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-frozen-box.delete', {
            parent: 'stock-out-frozen-box',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-frozen-box/stock-out-frozen-box-delete-dialog.html',
                    controller: 'StockOutFrozenBoxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutFrozenBox', function(StockOutFrozenBox) {
                            return StockOutFrozenBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-frozen-box', null, { reload: 'stock-out-frozen-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
