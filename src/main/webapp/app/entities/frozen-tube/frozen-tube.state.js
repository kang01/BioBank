(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frozen-tube', {
            parent: 'entity',
            url: '/frozen-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-tube/frozen-tubes.html',
                    controller: 'FrozenTubeController',
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
                    $translatePartialLoader.addPart('frozenTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frozen-tube-detail', {
            parent: 'frozen-tube',
            url: '/frozen-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-tube/frozen-tube-detail.html',
                    controller: 'FrozenTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frozenTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FrozenTube', function($stateParams, FrozenTube) {
                    return FrozenTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frozen-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frozen-tube-detail.edit', {
            parent: 'frozen-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube/frozen-tube-dialog.html',
                    controller: 'FrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenTube', function(FrozenTube) {
                            return FrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-tube.new', {
            parent: 'frozen-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube/frozen-tube-dialog.html',
                    controller: 'FrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                projectCode: null,
                                frozenTubeCode: null,
                                sampleTempCode: null,
                                sampleCode: null,
                                frozenTubeTypeCode: null,
                                frozenTubeTypeName: null,
                                sampleTypeCode: null,
                                sampleTypeName: null,
                                sampleUsedTimesMost: null,
                                sampleUsedTimes: null,
                                frozenTubeVolumns: null,
                                frozenTubeVolumnsUnit: null,
                                tubeRows: null,
                                tubeColumns: null,
                                memo: null,
                                errorType: null,
                                status: null,
                                frozenBoxCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frozen-tube', null, { reload: 'frozen-tube' });
                }, function() {
                    $state.go('frozen-tube');
                });
            }]
        })
        .state('frozen-tube.edit', {
            parent: 'frozen-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube/frozen-tube-dialog.html',
                    controller: 'FrozenTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenTube', function(FrozenTube) {
                            return FrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-tube', null, { reload: 'frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-tube.delete', {
            parent: 'frozen-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube/frozen-tube-delete-dialog.html',
                    controller: 'FrozenTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FrozenTube', function(FrozenTube) {
                            return FrozenTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-tube', null, { reload: 'frozen-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
