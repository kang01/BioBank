(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-in-tubes', {
            parent: 'entity',
            url: '/stock-in-tubes?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInTubes.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-tubes/stock-in-tubes.html',
                    controller: 'StockInTubesController',
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
                    $translatePartialLoader.addPart('stockInTubes');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-in-tubes-detail', {
            parent: 'stock-in-tubes',
            url: '/stock-in-tubes/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockInTubes.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-in-tubes/stock-in-tubes-detail.html',
                    controller: 'StockInTubesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockInTubes');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockInTubes', function($stateParams, StockInTubes) {
                    return StockInTubes.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-in-tubes',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-in-tubes-detail.edit', {
            parent: 'stock-in-tubes-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tubes/stock-in-tubes-dialog.html',
                    controller: 'StockInTubesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInTubes', function(StockInTubes) {
                            return StockInTubes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-tubes.new', {
            parent: 'stock-in-tubes',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tubes/stock-in-tubes-dialog.html',
                    controller: 'StockInTubesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sampleCode: null,
                                frozenTubeCode: null,
                                rowsInTube: null,
                                columnsInTube: null,
                                memo: null,
                                status: null,
                                sampleTempCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-in-tubes', null, { reload: 'stock-in-tubes' });
                }, function() {
                    $state.go('stock-in-tubes');
                });
            }]
        })
        .state('stock-in-tubes.edit', {
            parent: 'stock-in-tubes',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tubes/stock-in-tubes-dialog.html',
                    controller: 'StockInTubesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockInTubes', function(StockInTubes) {
                            return StockInTubes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-tubes', null, { reload: 'stock-in-tubes' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-in-tubes.delete', {
            parent: 'stock-in-tubes',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-in-tubes/stock-in-tubes-delete-dialog.html',
                    controller: 'StockInTubesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockInTubes', function(StockInTubes) {
                            return StockInTubes.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-in-tubes', null, { reload: 'stock-in-tubes' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
