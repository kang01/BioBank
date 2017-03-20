(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('storage-in-box', {
            parent: 'entity',
            url: '/storage-in-box?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.storageInBox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-in-box/storage-in-boxes.html',
                    controller: 'StorageInBoxController',
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
                    $translatePartialLoader.addPart('storageInBox');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('storage-in-box-detail', {
            parent: 'storage-in-box',
            url: '/storage-in-box/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.storageInBox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-in-box/storage-in-box-detail.html',
                    controller: 'StorageInBoxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('storageInBox');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StorageInBox', function($stateParams, StorageInBox) {
                    return StorageInBox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'storage-in-box',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('storage-in-box-detail.edit', {
            parent: 'storage-in-box-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in-box/storage-in-box-dialog.html',
                    controller: 'StorageInBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StorageInBox', function(StorageInBox) {
                            return StorageInBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-in-box.new', {
            parent: 'storage-in-box',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in-box/storage-in-box-dialog.html',
                    controller: 'StorageInBoxDialogController',
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
                                memo: null,
                                status: null,
                                frozenBoxCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('storage-in-box', null, { reload: 'storage-in-box' });
                }, function() {
                    $state.go('storage-in-box');
                });
            }]
        })
        .state('storage-in-box.edit', {
            parent: 'storage-in-box',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in-box/storage-in-box-dialog.html',
                    controller: 'StorageInBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StorageInBox', function(StorageInBox) {
                            return StorageInBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-in-box', null, { reload: 'storage-in-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-in-box.delete', {
            parent: 'storage-in-box',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in-box/storage-in-box-delete-dialog.html',
                    controller: 'StorageInBoxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StorageInBox', function(StorageInBox) {
                            return StorageInBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-in-box', null, { reload: 'storage-in-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
