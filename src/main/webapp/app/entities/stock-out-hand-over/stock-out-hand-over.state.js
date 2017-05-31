(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-hand-over', {
            parent: 'entity',
            url: '/stock-out-hand-over?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandOver.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-hand-over/stock-out-hand-overs.html',
                    controller: 'StockOutHandOverController',
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
                    $translatePartialLoader.addPart('stockOutHandOver');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-hand-over-detail', {
            parent: 'stock-out-hand-over',
            url: '/stock-out-hand-over/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandOver.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-hand-over/stock-out-hand-over-detail.html',
                    controller: 'StockOutHandOverDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutHandOver');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutHandOver', function($stateParams, StockOutHandOver) {
                    return StockOutHandOver.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-hand-over',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-hand-over-detail.edit', {
            parent: 'stock-out-hand-over-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-hand-over/stock-out-hand-over-dialog.html',
                    controller: 'StockOutHandOverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandOver', function(StockOutHandOver) {
                            return StockOutHandOver.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-hand-over.new', {
            parent: 'stock-out-hand-over',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-hand-over/stock-out-hand-over-dialog.html',
                    controller: 'StockOutHandOverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                handoverCode: null,
                                receiverName: null,
                                receiverPhone: null,
                                receiverOrganization: null,
                                handoverPersonId: null,
                                handoverTime: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-hand-over', null, { reload: 'stock-out-hand-over' });
                }, function() {
                    $state.go('stock-out-hand-over');
                });
            }]
        })
        .state('stock-out-hand-over.edit', {
            parent: 'stock-out-hand-over',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-hand-over/stock-out-hand-over-dialog.html',
                    controller: 'StockOutHandOverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandOver', function(StockOutHandOver) {
                            return StockOutHandOver.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-hand-over', null, { reload: 'stock-out-hand-over' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-hand-over.delete', {
            parent: 'stock-out-hand-over',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-hand-over/stock-out-hand-over-delete-dialog.html',
                    controller: 'StockOutHandOverDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutHandOver', function(StockOutHandOver) {
                            return StockOutHandOver.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-hand-over', null, { reload: 'stock-out-hand-over' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
