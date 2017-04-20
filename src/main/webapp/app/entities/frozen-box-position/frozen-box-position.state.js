(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frozen-box-position', {
            parent: 'entity',
            url: '/frozen-box-position?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenBoxPosition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-box-position/frozen-box-positions.html',
                    controller: 'FrozenBoxPositionController',
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
                    $translatePartialLoader.addPart('frozenBoxPosition');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frozen-box-position-detail', {
            parent: 'frozen-box-position',
            url: '/frozen-box-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenBoxPosition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-box-position/frozen-box-position-detail.html',
                    controller: 'FrozenBoxPositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frozenBoxPosition');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FrozenBoxPosition', function($stateParams, FrozenBoxPosition) {
                    return FrozenBoxPosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frozen-box-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frozen-box-position-detail.edit', {
            parent: 'frozen-box-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-position/frozen-box-position-dialog.html',
                    controller: 'FrozenBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenBoxPosition', function(FrozenBoxPosition) {
                            return FrozenBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-box-position.new', {
            parent: 'frozen-box-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-position/frozen-box-position-dialog.html',
                    controller: 'FrozenBoxPositionDialogController',
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
                                frozenBoxCode: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frozen-box-position', null, { reload: 'frozen-box-position' });
                }, function() {
                    $state.go('frozen-box-position');
                });
            }]
        })
        .state('frozen-box-position.edit', {
            parent: 'frozen-box-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-position/frozen-box-position-dialog.html',
                    controller: 'FrozenBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenBoxPosition', function(FrozenBoxPosition) {
                            return FrozenBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-box-position', null, { reload: 'frozen-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-box-position.delete', {
            parent: 'frozen-box-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box-position/frozen-box-position-delete-dialog.html',
                    controller: 'FrozenBoxPositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FrozenBoxPosition', function(FrozenBoxPosition) {
                            return FrozenBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-box-position', null, { reload: 'frozen-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
