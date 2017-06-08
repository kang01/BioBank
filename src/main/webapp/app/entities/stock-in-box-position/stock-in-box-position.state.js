(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-in-box-position', {
            parent: 'entity',
            url: '/stock-in-box-position?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInBoxPosition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-box-position/stock-in-box-positions.html',
                    controller: 'StockInBoxPositionController',
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
                    $translatePartialLoader.addPart('stockInBoxPosition');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-in-box-position-detail', {
            parent: 'stock-in-box-position',
            url: '/stock-in-box-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInBoxPosition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-box-position/stock-in-box-position-detail.html',
                    controller: 'StockInBoxPositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockInBoxPosition');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockInBoxPosition', function($stateParams, StockInBoxPosition) {
                    return StockInBoxPosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-in-box-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-in-box-position-detail.edit', {
            parent: 'stock-in-box-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-box-position/stock-in-box-position-dialog.html',
                    controller: 'StockInBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInBoxPosition', function(StockInBoxPosition) {
                            return StockInBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-box-position.new', {
            parent: 'stock-in-box-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-box-position/stock-in-box-position-dialog.html',
                    controller: 'StockInBoxPositionDialogController',
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
                    $state.go('stock-in-box-position', null, { reload: 'stock-in-box-position' });
                }, function() {
                    $state.go('stock-in-box-position');
                });
            }]
        })
        .state('stock-in-box-position.edit', {
            parent: 'stock-in-box-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-box-position/stock-in-box-position-dialog.html',
                    controller: 'StockInBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInBoxPosition', function(StockInBoxPosition) {
                            return StockInBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-box-position', null, { reload: 'stock-in-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-box-position.delete', {
            parent: 'stock-in-box-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-box-position/stock-in-box-position-delete-dialog.html',
                    controller: 'StockInBoxPositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockInBoxPosition', function(StockInBoxPosition) {
                            return StockInBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-box-position', null, { reload: 'stock-in-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
