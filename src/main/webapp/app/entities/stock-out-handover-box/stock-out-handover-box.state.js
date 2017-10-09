(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-handover-box', {
            parent: 'entity',
            url: '/stock-out-handover-box?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandoverBox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-handover-box/stock-out-handover-boxes.html',
                    controller: 'StockOutHandoverBoxController',
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
                    $translatePartialLoader.addPart('stockOutHandoverBox');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-handover-box-detail', {
            parent: 'stock-out-handover-box',
            url: '/stock-out-handover-box/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutHandoverBox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-handover-box/stock-out-handover-box-detail.html',
                    controller: 'StockOutHandoverBoxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutHandoverBox');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutHandoverBox', function($stateParams, StockOutHandoverBox) {
                    return StockOutHandoverBox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-handover-box',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-handover-box-detail.edit', {
            parent: 'stock-out-handover-box-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-box/stock-out-handover-box-dialog.html',
                    controller: 'StockOutHandoverBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandoverBox', function(StockOutHandoverBox) {
                            return StockOutHandoverBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-handover-box.new', {
            parent: 'stock-out-handover-box',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-box/stock-out-handover-box-dialog.html',
                    controller: 'StockOutHandoverBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                equipmentCode: null,
                                areaCode: null,
                                supportRackCode: null,
                                rowsInShelf: null,
                                columnsInShelf: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover-box', null, { reload: 'stock-out-handover-box' });
                }, function() {
                    $state.go('stock-out-handover-box');
                });
            }]
        })
        .state('stock-out-handover-box.edit', {
            parent: 'stock-out-handover-box',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-box/stock-out-handover-box-dialog.html',
                    controller: 'StockOutHandoverBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutHandoverBox', function(StockOutHandoverBox) {
                            return StockOutHandoverBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover-box', null, { reload: 'stock-out-handover-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-handover-box.delete', {
            parent: 'stock-out-handover-box',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-handover-box/stock-out-handover-box-delete-dialog.html',
                    controller: 'StockOutHandoverBoxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutHandoverBox', function(StockOutHandoverBox) {
                            return StockOutHandoverBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-handover-box', null, { reload: 'stock-out-handover-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
