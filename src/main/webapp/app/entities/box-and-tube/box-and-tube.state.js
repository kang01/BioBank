(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('box-and-tube', {
            parent: 'entity',
            url: '/box-and-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.boxAndTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/box-and-tube/box-and-tubes.html',
                    controller: 'BoxAndTubeController',
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
                    $translatePartialLoader.addPart('boxAndTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('box-and-tube-detail', {
            parent: 'box-and-tube',
            url: '/box-and-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.boxAndTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/box-and-tube/box-and-tube-detail.html',
                    controller: 'BoxAndTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('boxAndTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'BoxAndTube', function($stateParams, BoxAndTube) {
                    return BoxAndTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'box-and-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('box-and-tube-detail.edit', {
            parent: 'box-and-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/box-and-tube/box-and-tube-dialog.html',
                    controller: 'BoxAndTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BoxAndTube', function(BoxAndTube) {
                            return BoxAndTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('box-and-tube.new', {
            parent: 'box-and-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/box-and-tube/box-and-tube-dialog.html',
                    controller: 'BoxAndTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('box-and-tube', null, { reload: 'box-and-tube' });
                }, function() {
                    $state.go('box-and-tube');
                });
            }]
        })
        .state('box-and-tube.edit', {
            parent: 'box-and-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/box-and-tube/box-and-tube-dialog.html',
                    controller: 'BoxAndTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['BoxAndTube', function(BoxAndTube) {
                            return BoxAndTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('box-and-tube', null, { reload: 'box-and-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('box-and-tube.delete', {
            parent: 'box-and-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/box-and-tube/box-and-tube-delete-dialog.html',
                    controller: 'BoxAndTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['BoxAndTube', function(BoxAndTube) {
                            return BoxAndTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('box-and-tube', null, { reload: 'box-and-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
