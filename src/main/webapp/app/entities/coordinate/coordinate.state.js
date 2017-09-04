(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('coordinate', {
            parent: 'entity',
            url: '/coordinate?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.coordinate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coordinate/coordinates.html',
                    controller: 'CoordinateController',
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
                    $translatePartialLoader.addPart('coordinate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('coordinate-detail', {
            parent: 'coordinate',
            url: '/coordinate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.coordinate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/coordinate/coordinate-detail.html',
                    controller: 'CoordinateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('coordinate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Coordinate', function($stateParams, Coordinate) {
                    return Coordinate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'coordinate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('coordinate-detail.edit', {
            parent: 'coordinate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coordinate/coordinate-dialog.html',
                    controller: 'CoordinateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Coordinate', function(Coordinate) {
                            return Coordinate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coordinate.new', {
            parent: 'coordinate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coordinate/coordinate-dialog.html',
                    controller: 'CoordinateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                province: null,
                                city: null,
                                longitude: null,
                                latitude: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('coordinate', null, { reload: 'coordinate' });
                }, function() {
                    $state.go('coordinate');
                });
            }]
        })
        .state('coordinate.edit', {
            parent: 'coordinate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coordinate/coordinate-dialog.html',
                    controller: 'CoordinateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Coordinate', function(Coordinate) {
                            return Coordinate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('coordinate', null, { reload: 'coordinate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('coordinate.delete', {
            parent: 'coordinate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/coordinate/coordinate-delete-dialog.html',
                    controller: 'CoordinateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Coordinate', function(Coordinate) {
                            return Coordinate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('coordinate', null, { reload: 'coordinate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
