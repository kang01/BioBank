(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranship-stock-in', {
            parent: 'entity',
            url: '/tranship-stock-in?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipStockIn.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-stock-in/tranship-stock-ins.html',
                    controller: 'TranshipStockInController',
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
                    $translatePartialLoader.addPart('transhipStockIn');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranship-stock-in-detail', {
            parent: 'tranship-stock-in',
            url: '/tranship-stock-in/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipStockIn.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-stock-in/tranship-stock-in-detail.html',
                    controller: 'TranshipStockInDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transhipStockIn');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TranshipStockIn', function($stateParams, TranshipStockIn) {
                    return TranshipStockIn.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranship-stock-in',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranship-stock-in-detail.edit', {
            parent: 'tranship-stock-in-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-stock-in/tranship-stock-in-dialog.html',
                    controller: 'TranshipStockInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipStockIn', function(TranshipStockIn) {
                            return TranshipStockIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-stock-in.new', {
            parent: 'tranship-stock-in',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-stock-in/tranship-stock-in-dialog.html',
                    controller: 'TranshipStockInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                transhipCode: null,
                                stockInCode: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranship-stock-in', null, { reload: 'tranship-stock-in' });
                }, function() {
                    $state.go('tranship-stock-in');
                });
            }]
        })
        .state('tranship-stock-in.edit', {
            parent: 'tranship-stock-in',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-stock-in/tranship-stock-in-dialog.html',
                    controller: 'TranshipStockInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipStockIn', function(TranshipStockIn) {
                            return TranshipStockIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-stock-in', null, { reload: 'tranship-stock-in' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-stock-in.delete', {
            parent: 'tranship-stock-in',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-stock-in/tranship-stock-in-delete-dialog.html',
                    controller: 'TranshipStockInDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TranshipStockIn', function(TranshipStockIn) {
                            return TranshipStockIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-stock-in', null, { reload: 'tranship-stock-in' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
