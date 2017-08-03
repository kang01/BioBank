(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('position-change', {
            parent: 'entity',
            url: '/position-change?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionChange.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-change/position-changes.html',
                    controller: 'PositionChangeController',
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
                    $translatePartialLoader.addPart('positionChange');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('position-change-detail', {
            parent: 'position-change',
            url: '/position-change/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionChange.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-change/position-change-detail.html',
                    controller: 'PositionChangeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('positionChange');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PositionChange', function($stateParams, PositionChange) {
                    return PositionChange.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'position-change',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('position-change-detail.edit', {
            parent: 'position-change-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change/position-change-dialog.html',
                    controller: 'PositionChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionChange', function(PositionChange) {
                            return PositionChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-change.new', {
            parent: 'position-change',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change/position-change-dialog.html',
                    controller: 'PositionChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                changeReason: null,
                                changeType: null,
                                whetherFreezingAndThawing: null,
                                operatorId1: null,
                                operatorId2: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('position-change', null, { reload: 'position-change' });
                }, function() {
                    $state.go('position-change');
                });
            }]
        })
        .state('position-change.edit', {
            parent: 'position-change',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change/position-change-dialog.html',
                    controller: 'PositionChangeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionChange', function(PositionChange) {
                            return PositionChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-change', null, { reload: 'position-change' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-change.delete', {
            parent: 'position-change',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change/position-change-delete-dialog.html',
                    controller: 'PositionChangeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PositionChange', function(PositionChange) {
                            return PositionChange.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-change', null, { reload: 'position-change' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
