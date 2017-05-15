(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-plan', {
            parent: 'entity',
            url: '/stock-out-plan?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutPlan.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-plan/stock-out-plans.html',
                    controller: 'StockOutPlanController',
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
                    $translatePartialLoader.addPart('stockOutPlan');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-plan-detail', {
            parent: 'stock-out-plan',
            url: '/stock-out-plan/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutPlan.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-plan/stock-out-plan-detail.html',
                    controller: 'StockOutPlanDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutPlan');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutPlan', function($stateParams, StockOutPlan) {
                    return StockOutPlan.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-plan',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-plan-detail.edit', {
            parent: 'stock-out-plan-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan/stock-out-plan-dialog.html',
                    controller: 'StockOutPlanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutPlan', function(StockOutPlan) {
                            return StockOutPlan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-plan.new', {
            parent: 'stock-out-plan',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan/stock-out-plan-dialog.html',
                    controller: 'StockOutPlanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                stockOutPlanCode: null,
                                status: null,
                                memo: null,
                                applyNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-plan', null, { reload: 'stock-out-plan' });
                }, function() {
                    $state.go('stock-out-plan');
                });
            }]
        })
        .state('stock-out-plan.edit', {
            parent: 'stock-out-plan',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan/stock-out-plan-dialog.html',
                    controller: 'StockOutPlanDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutPlan', function(StockOutPlan) {
                            return StockOutPlan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-plan', null, { reload: 'stock-out-plan' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-plan.delete', {
            parent: 'stock-out-plan',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-plan/stock-out-plan-delete-dialog.html',
                    controller: 'StockOutPlanDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutPlan', function(StockOutPlan) {
                            return StockOutPlan.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-plan', null, { reload: 'stock-out-plan' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
