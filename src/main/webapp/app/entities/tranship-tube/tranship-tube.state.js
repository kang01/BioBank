(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranship-tube', {
            parent: 'entity',
            url: '/tranship-tube?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipTube.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-tube/tranship-tubes.html',
                    controller: 'TranshipTubeController',
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
                    $translatePartialLoader.addPart('transhipTube');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranship-tube-detail', {
            parent: 'tranship-tube',
            url: '/tranship-tube/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipTube.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-tube/tranship-tube-detail.html',
                    controller: 'TranshipTubeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transhipTube');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TranshipTube', function($stateParams, TranshipTube) {
                    return TranshipTube.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranship-tube',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranship-tube-detail.edit', {
            parent: 'tranship-tube-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-tube/tranship-tube-dialog.html',
                    controller: 'TranshipTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipTube', function(TranshipTube) {
                            return TranshipTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-tube.new', {
            parent: 'tranship-tube',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-tube/tranship-tube-dialog.html',
                    controller: 'TranshipTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                memo: null,
                                columnsInTube: null,
                                rowsInTube: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranship-tube', null, { reload: 'tranship-tube' });
                }, function() {
                    $state.go('tranship-tube');
                });
            }]
        })
        .state('tranship-tube.edit', {
            parent: 'tranship-tube',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-tube/tranship-tube-dialog.html',
                    controller: 'TranshipTubeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipTube', function(TranshipTube) {
                            return TranshipTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-tube', null, { reload: 'tranship-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-tube.delete', {
            parent: 'tranship-tube',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-tube/tranship-tube-delete-dialog.html',
                    controller: 'TranshipTubeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TranshipTube', function(TranshipTube) {
                            return TranshipTube.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-tube', null, { reload: 'tranship-tube' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
