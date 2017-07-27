(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-in-tranship-box', {
            parent: 'entity',
            url: '/stock-in-tranship-box?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInTranshipBox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-tranship-box/stock-in-tranship-boxes.html',
                    controller: 'StockInTranshipBoxController',
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
                    $translatePartialLoader.addPart('stockInTranshipBox');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-in-tranship-box-detail', {
            parent: 'stock-in-tranship-box',
            url: '/stock-in-tranship-box/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInTranshipBox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-tranship-box/stock-in-tranship-box-detail.html',
                    controller: 'StockInTranshipBoxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockInTranshipBox');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockInTranshipBox', function($stateParams, StockInTranshipBox) {
                    return StockInTranshipBox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-in-tranship-box',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-in-tranship-box-detail.edit', {
            parent: 'stock-in-tranship-box-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tranship-box/stock-in-tranship-box-dialog.html',
                    controller: 'StockInTranshipBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInTranshipBox', function(StockInTranshipBox) {
                            return StockInTranshipBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-tranship-box.new', {
            parent: 'stock-in-tranship-box',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tranship-box/stock-in-tranship-box-dialog.html',
                    controller: 'StockInTranshipBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                stockInCode: null,
                                transhipCode: null,
                                frozenBoxCode: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-in-tranship-box', null, { reload: 'stock-in-tranship-box' });
                }, function() {
                    $state.go('stock-in-tranship-box');
                });
            }]
        })
        .state('stock-in-tranship-box.edit', {
            parent: 'stock-in-tranship-box',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tranship-box/stock-in-tranship-box-dialog.html',
                    controller: 'StockInTranshipBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInTranshipBox', function(StockInTranshipBox) {
                            return StockInTranshipBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-tranship-box', null, { reload: 'stock-in-tranship-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-tranship-box.delete', {
            parent: 'stock-in-tranship-box',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tranship-box/stock-in-tranship-box-delete-dialog.html',
                    controller: 'StockInTranshipBoxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockInTranshipBox', function(StockInTranshipBox) {
                            return StockInTranshipBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-tranship-box', null, { reload: 'stock-in-tranship-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
