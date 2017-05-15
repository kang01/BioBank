(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-apply', {
            parent: 'entity',
            url: '/stock-out-apply?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutApply.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-apply/stock-out-applies.html',
                    controller: 'StockOutApplyController',
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
                    $translatePartialLoader.addPart('stockOutApply');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-apply-detail', {
            parent: 'stock-out-apply',
            url: '/stock-out-apply/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutApply.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-apply/stock-out-apply-detail.html',
                    controller: 'StockOutApplyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutApply');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutApply', function($stateParams, StockOutApply) {
                    return StockOutApply.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-apply',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-apply-detail.edit', {
            parent: 'stock-out-apply-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply/stock-out-apply-dialog.html',
                    controller: 'StockOutApplyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutApply', function(StockOutApply) {
                            return StockOutApply.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-apply.new', {
            parent: 'stock-out-apply',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply/stock-out-apply-dialog.html',
                    controller: 'StockOutApplyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                startTime: null,
                                endTime: null,
                                purposeOfSample: null,
                                recordTime: null,
                                recordId: null,
                                parentApplyId: null,
                                approverId: null,
                                approveTime: null,
                                status: null,
                                memo: null,
                                applyPersonName: null,
                                applyCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-apply', null, { reload: 'stock-out-apply' });
                }, function() {
                    $state.go('stock-out-apply');
                });
            }]
        })
        .state('stock-out-apply.edit', {
            parent: 'stock-out-apply',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply/stock-out-apply-dialog.html',
                    controller: 'StockOutApplyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutApply', function(StockOutApply) {
                            return StockOutApply.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-apply', null, { reload: 'stock-out-apply' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-apply.delete', {
            parent: 'stock-out-apply',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-apply/stock-out-apply-delete-dialog.html',
                    controller: 'StockOutApplyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutApply', function(StockOutApply) {
                            return StockOutApply.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-apply', null, { reload: 'stock-out-apply' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
