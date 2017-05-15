(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-task', {
            parent: 'entity',
            url: '/stock-out-task?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutTask.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-task/stock-out-tasks.html',
                    controller: 'StockOutTaskController',
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
                    $translatePartialLoader.addPart('stockOutTask');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-task-detail', {
            parent: 'stock-out-task',
            url: '/stock-out-task/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutTask.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-task/stock-out-task-detail.html',
                    controller: 'StockOutTaskDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutTask');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutTask', function($stateParams, StockOutTask) {
                    return StockOutTask.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-task',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-task-detail.edit', {
            parent: 'stock-out-task-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task/stock-out-task-dialog.html',
                    controller: 'StockOutTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutTask', function(StockOutTask) {
                            return StockOutTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-task.new', {
            parent: 'stock-out-task',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task/stock-out-task-dialog.html',
                    controller: 'StockOutTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                stockOutHeadId1: null,
                                stockOutHeadId2: null,
                                stockOutDate: null,
                                status: null,
                                memo: null,
                                stockOutTaskCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-task', null, { reload: 'stock-out-task' });
                }, function() {
                    $state.go('stock-out-task');
                });
            }]
        })
        .state('stock-out-task.edit', {
            parent: 'stock-out-task',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task/stock-out-task-dialog.html',
                    controller: 'StockOutTaskDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutTask', function(StockOutTask) {
                            return StockOutTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-task', null, { reload: 'stock-out-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-task.delete', {
            parent: 'stock-out-task',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-task/stock-out-task-delete-dialog.html',
                    controller: 'StockOutTaskDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutTask', function(StockOutTask) {
                            return StockOutTask.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-task', null, { reload: 'stock-out-task' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
