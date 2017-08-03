(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('position-destroy', {
            parent: 'entity',
            url: '/position-destroy?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionDestroy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-destroy/position-destroys.html',
                    controller: 'PositionDestroyController',
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
                    $translatePartialLoader.addPart('positionDestroy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('position-destroy-detail', {
            parent: 'position-destroy',
            url: '/position-destroy/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionDestroy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-destroy/position-destroy-detail.html',
                    controller: 'PositionDestroyDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('positionDestroy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PositionDestroy', function($stateParams, PositionDestroy) {
                    return PositionDestroy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'position-destroy',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('position-destroy-detail.edit', {
            parent: 'position-destroy-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy/position-destroy-dialog.html',
                    controller: 'PositionDestroyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionDestroy', function(PositionDestroy) {
                            return PositionDestroy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-destroy.new', {
            parent: 'position-destroy',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy/position-destroy-dialog.html',
                    controller: 'PositionDestroyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                destroyReason: null,
                                destroyType: null,
                                operatorId1: null,
                                operatorId2: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('position-destroy', null, { reload: 'position-destroy' });
                }, function() {
                    $state.go('position-destroy');
                });
            }]
        })
        .state('position-destroy.edit', {
            parent: 'position-destroy',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy/position-destroy-dialog.html',
                    controller: 'PositionDestroyDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionDestroy', function(PositionDestroy) {
                            return PositionDestroy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-destroy', null, { reload: 'position-destroy' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-destroy.delete', {
            parent: 'position-destroy',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy/position-destroy-delete-dialog.html',
                    controller: 'PositionDestroyDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PositionDestroy', function(PositionDestroy) {
                            return PositionDestroy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-destroy', null, { reload: 'position-destroy' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
