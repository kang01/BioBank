(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frozen-tube-type', {
            parent: 'entity',
            url: '/frozen-tube-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenTubeType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-tube-type/frozen-tube-types.html',
                    controller: 'FrozenTubeTypeController',
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
                    $translatePartialLoader.addPart('frozenTubeType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frozen-tube-type-detail', {
            parent: 'frozen-tube-type',
            url: '/frozen-tube-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenTubeType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-tube-type/frozen-tube-type-detail.html',
                    controller: 'FrozenTubeTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frozenTubeType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FrozenTubeType', function($stateParams, FrozenTubeType) {
                    return FrozenTubeType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frozen-tube-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frozen-tube-type-detail.edit', {
            parent: 'frozen-tube-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-type/frozen-tube-type-dialog.html',
                    controller: 'FrozenTubeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenTubeType', function(FrozenTubeType) {
                            return FrozenTubeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-tube-type.new', {
            parent: 'frozen-tube-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-type/frozen-tube-type-dialog.html',
                    controller: 'FrozenTubeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                frozenTubeTypeCode: null,
                                frozenTubeTypeName: null,
                                sampleUsedTimesMost: null,
                                frozenTubeVolumn: null,
                                frozenTubeVolumnUnit: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frozen-tube-type', null, { reload: 'frozen-tube-type' });
                }, function() {
                    $state.go('frozen-tube-type');
                });
            }]
        })
        .state('frozen-tube-type.edit', {
            parent: 'frozen-tube-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-type/frozen-tube-type-dialog.html',
                    controller: 'FrozenTubeTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenTubeType', function(FrozenTubeType) {
                            return FrozenTubeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-tube-type', null, { reload: 'frozen-tube-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-tube-type.delete', {
            parent: 'frozen-tube-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-type/frozen-tube-type-delete-dialog.html',
                    controller: 'FrozenTubeTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FrozenTubeType', function(FrozenTubeType) {
                            return FrozenTubeType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-tube-type', null, { reload: 'frozen-tube-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
