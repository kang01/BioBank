(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frozen-box-type', {
            parent: 'entity',
            url: '/frozen-box-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenBoxType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-box-type/frozen-box-types.html',
                    controller: 'FrozenBoxTypeController',
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
                    $translatePartialLoader.addPart('frozenBoxType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frozen-box-type-detail', {
            parent: 'frozen-box-type',
            url: '/frozen-box-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenBoxType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-box-type/frozen-box-type-detail.html',
                    controller: 'FrozenBoxTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frozenBoxType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FrozenBoxType', function($stateParams, FrozenBoxType) {
                    return FrozenBoxType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frozen-box-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frozen-box-type-detail.edit', {
            parent: 'frozen-box-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-type/frozen-box-type-dialog.html',
                    controller: 'FrozenBoxTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenBoxType', function(FrozenBoxType) {
                            return FrozenBoxType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-box-type.new', {
            parent: 'frozen-box-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-type/frozen-box-type-dialog.html',
                    controller: 'FrozenBoxTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                frozenBoxTypeCode: null,
                                frozenBoxTypeName: null,
                                frozenBoxTypeRows: null,
                                frozenBoxTypeColumns: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frozen-box-type', null, { reload: 'frozen-box-type' });
                }, function() {
                    $state.go('frozen-box-type');
                });
            }]
        })
        .state('frozen-box-type.edit', {
            parent: 'frozen-box-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-type/frozen-box-type-dialog.html',
                    controller: 'FrozenBoxTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenBoxType', function(FrozenBoxType) {
                            return FrozenBoxType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-box-type', null, { reload: 'frozen-box-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-box-type.delete', {
            parent: 'frozen-box-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-type/frozen-box-type-delete-dialog.html',
                    controller: 'FrozenBoxTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FrozenBoxType', function(FrozenBoxType) {
                            return FrozenBoxType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-box-type', null, { reload: 'frozen-box-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
