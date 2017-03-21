(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('storage-in', {
            parent: 'entity',
            url: '/storage-in?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.storageIn.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-in/storage-ins.html',
                    controller: 'StorageInController',
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
                    $translatePartialLoader.addPart('storageIn');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('storage-in-detail', {
            parent: 'storage-in',
            url: '/storage-in/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.storageIn.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/storage-in/storage-in-detail.html',
                    controller: 'StorageInDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('storageIn');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StorageIn', function($stateParams, StorageIn) {
                    return StorageIn.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'storage-in',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('storage-in-detail.edit', {
            parent: 'storage-in-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in/storage-in-dialog.html',
                    controller: 'StorageInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StorageIn', function(StorageIn) {
                            return StorageIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-in.new', {
            parent: 'storage-in',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in/storage-in-dialog.html',
                    controller: 'StorageInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                projectCode: null,
                                project_site_code: null,
                                receiveDate: null,
                                receiveId: null,
                                receiveName: null,
                                storageInType: null,
                                storageInPersonId1: null,
                                storageInPersonName1: null,
                                storageInPersonId2: null,
                                storangeInPersonName2: null,
                                storageInDate: null,
                                sampleNumber: null,
                                signId: null,
                                signName: null,
                                signDate: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('storage-in', null, { reload: 'storage-in' });
                }, function() {
                    $state.go('storage-in');
                });
            }]
        })
        .state('storage-in.edit', {
            parent: 'storage-in',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in/storage-in-dialog.html',
                    controller: 'StorageInDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StorageIn', function(StorageIn) {
                            return StorageIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-in', null, { reload: 'storage-in' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('storage-in.delete', {
            parent: 'storage-in',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/storage-in/storage-in-delete-dialog.html',
                    controller: 'StorageInDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StorageIn', function(StorageIn) {
                            return StorageIn.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('storage-in', null, { reload: 'storage-in' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
