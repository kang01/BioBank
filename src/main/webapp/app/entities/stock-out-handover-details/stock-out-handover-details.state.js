(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-handover-details', {
            parent: 'entity',
            url: '/stock-out-handover-details?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandoverDetails.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-handover-details/stock-out-handover-details.html',
                    controller: 'StockOutHandoverDetailsController',
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
                    $translatePartialLoader.addPart('stockOutHandoverDetails');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-handover-details-detail', {
            parent: 'stock-out-handover-details',
            url: '/stock-out-handover-details/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandoverDetails.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-handover-details/stock-out-handover-details-detail.html',
                    controller: 'StockOutHandoverDetailsDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutHandoverDetails');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutHandoverDetails', function($stateParams, StockOutHandoverDetails) {
                    return StockOutHandoverDetails.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-handover-details',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-handover-details-detail.edit', {
            parent: 'stock-out-handover-details-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-details/stock-out-handover-details-dialog.html',
                    controller: 'StockOutHandoverDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandoverDetails', function(StockOutHandoverDetails) {
                            return StockOutHandoverDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-handover-details.new', {
            parent: 'stock-out-handover-details',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-details/stock-out-handover-details-dialog.html',
                    controller: 'StockOutHandoverDetailsDialogController',
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
                    $state.go('stock-out-handover-details', null, { reload: 'stock-out-handover-details' });
                }, function() {
                    $state.go('stock-out-handover-details');
                });
            }]
        })
        .state('stock-out-handover-details.edit', {
            parent: 'stock-out-handover-details',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-details/stock-out-handover-details-dialog.html',
                    controller: 'StockOutHandoverDetailsDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandoverDetails', function(StockOutHandoverDetails) {
                            return StockOutHandoverDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover-details', null, { reload: 'stock-out-handover-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-handover-details.delete', {
            parent: 'stock-out-handover-details',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-details/stock-out-handover-details-delete-dialog.html',
                    controller: 'StockOutHandoverDetailsDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutHandoverDetails', function(StockOutHandoverDetails) {
                            return StockOutHandoverDetails.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover-details', null, { reload: 'stock-out-handover-details' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
