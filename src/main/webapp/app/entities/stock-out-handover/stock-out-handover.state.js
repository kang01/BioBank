(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-handover', {
            parent: 'entity',
            url: '/stock-out-handover?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandover.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-handover/stock-out-handovers.html',
                    controller: 'StockOutHandoverController',
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
                    $translatePartialLoader.addPart('stockOutHandover');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-handover-detail', {
            parent: 'stock-out-handover',
            url: '/stock-out-handover/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandover.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-handover/stock-out-handover-detail.html',
                    controller: 'StockOutHandoverDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutHandover');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutHandover', function($stateParams, StockOutHandover) {
                    return StockOutHandover.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-handover',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-handover-detail.edit', {
            parent: 'stock-out-handover-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover/stock-out-handover-dialog.html',
                    controller: 'StockOutHandoverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandover', function(StockOutHandover) {
                            return StockOutHandover.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-handover.new', {
            parent: 'stock-out-handover',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover/stock-out-handover-dialog.html',
                    controller: 'StockOutHandoverDialogController',
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
                    $state.go('stock-out-handover', null, { reload: 'stock-out-handover' });
                }, function() {
                    $state.go('stock-out-handover');
                });
            }]
        })
        .state('stock-out-handover.edit', {
            parent: 'stock-out-handover',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover/stock-out-handover-dialog.html',
                    controller: 'StockOutHandoverDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandover', function(StockOutHandover) {
                            return StockOutHandover.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover', null, { reload: 'stock-out-handover' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-handover.delete', {
            parent: 'stock-out-handover',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover/stock-out-handover-delete-dialog.html',
                    controller: 'StockOutHandoverDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutHandover', function(StockOutHandover) {
                            return StockOutHandover.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover', null, { reload: 'stock-out-handover' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
