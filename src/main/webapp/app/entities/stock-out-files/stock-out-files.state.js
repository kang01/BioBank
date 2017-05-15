(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-files', {
            parent: 'entity',
            url: '/stock-out-files?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutFiles.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-files/stock-out-files.html',
                    controller: 'StockOutFilesController',
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
                    $translatePartialLoader.addPart('stockOutFiles');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-files-detail', {
            parent: 'stock-out-files',
            url: '/stock-out-files/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutFiles.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-files/stock-out-files-detail.html',
                    controller: 'StockOutFilesDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutFiles');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutFiles', function($stateParams, StockOutFiles) {
                    return StockOutFiles.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-files',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-files-detail.edit', {
            parent: 'stock-out-files-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-files/stock-out-files-dialog.html',
                    controller: 'StockOutFilesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutFiles', function(StockOutFiles) {
                            return StockOutFiles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-files.new', {
            parent: 'stock-out-files',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-files/stock-out-files-dialog.html',
                    controller: 'StockOutFilesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                businessId: null,
                                filePath: null,
                                fileName: null,
                                fileType: null,
                                fileSize: null,
                                files: null,
                                filesContentType: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-files', null, { reload: 'stock-out-files' });
                }, function() {
                    $state.go('stock-out-files');
                });
            }]
        })
        .state('stock-out-files.edit', {
            parent: 'stock-out-files',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-files/stock-out-files-dialog.html',
                    controller: 'StockOutFilesDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutFiles', function(StockOutFiles) {
                            return StockOutFiles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-files', null, { reload: 'stock-out-files' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-files.delete', {
            parent: 'stock-out-files',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-files/stock-out-files-delete-dialog.html',
                    controller: 'StockOutFilesDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutFiles', function(StockOutFiles) {
                            return StockOutFiles.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-files', null, { reload: 'stock-out-files' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
