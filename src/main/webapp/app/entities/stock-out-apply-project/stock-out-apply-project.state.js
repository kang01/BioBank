(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-apply-project', {
            parent: 'entity',
            url: '/stock-out-apply-project?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutApplyProject.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-apply-project/stock-out-apply-projects.html',
                    controller: 'StockOutApplyProjectController',
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
                    $translatePartialLoader.addPart('stockOutApplyProject');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-apply-project-detail', {
            parent: 'stock-out-apply-project',
            url: '/stock-out-apply-project/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutApplyProject.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-apply-project/stock-out-apply-project-detail.html',
                    controller: 'StockOutApplyProjectDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutApplyProject');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutApplyProject', function($stateParams, StockOutApplyProject) {
                    return StockOutApplyProject.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-apply-project',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-apply-project-detail.edit', {
            parent: 'stock-out-apply-project-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply-project/stock-out-apply-project-dialog.html',
                    controller: 'StockOutApplyProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutApplyProject', function(StockOutApplyProject) {
                            return StockOutApplyProject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-apply-project.new', {
            parent: 'stock-out-apply-project',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply-project/stock-out-apply-project-dialog.html',
                    controller: 'StockOutApplyProjectDialogController',
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
                    $state.go('stock-out-apply-project', null, { reload: 'stock-out-apply-project' });
                }, function() {
                    $state.go('stock-out-apply-project');
                });
            }]
        })
        .state('stock-out-apply-project.edit', {
            parent: 'stock-out-apply-project',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply-project/stock-out-apply-project-dialog.html',
                    controller: 'StockOutApplyProjectDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutApplyProject', function(StockOutApplyProject) {
                            return StockOutApplyProject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-apply-project', null, { reload: 'stock-out-apply-project' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-apply-project.delete', {
            parent: 'stock-out-apply-project',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply-project/stock-out-apply-project-delete-dialog.html',
                    controller: 'StockOutApplyProjectDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutApplyProject', function(StockOutApplyProject) {
                            return StockOutApplyProject.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-apply-project', null, { reload: 'stock-out-apply-project' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
