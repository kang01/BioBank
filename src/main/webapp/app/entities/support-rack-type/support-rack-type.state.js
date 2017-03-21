(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('support-rack-type', {
            parent: 'entity',
            url: '/support-rack-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.supportRackType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/support-rack-type/support-rack-types.html',
                    controller: 'SupportRackTypeController',
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
                    $translatePartialLoader.addPart('supportRackType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('support-rack-type-detail', {
            parent: 'support-rack-type',
            url: '/support-rack-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.supportRackType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/support-rack-type/support-rack-type-detail.html',
                    controller: 'SupportRackTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('supportRackType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SupportRackType', function($stateParams, SupportRackType) {
                    return SupportRackType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'support-rack-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('support-rack-type-detail.edit', {
            parent: 'support-rack-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack-type/support-rack-type-dialog.html',
                    controller: 'SupportRackTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SupportRackType', function(SupportRackType) {
                            return SupportRackType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('support-rack-type.new', {
            parent: 'support-rack-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack-type/support-rack-type-dialog.html',
                    controller: 'SupportRackTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                supportRackTypeCode: null,
                                supportRackRows: null,
                                supportRackColumns: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('support-rack-type', null, { reload: 'support-rack-type' });
                }, function() {
                    $state.go('support-rack-type');
                });
            }]
        })
        .state('support-rack-type.edit', {
            parent: 'support-rack-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack-type/support-rack-type-dialog.html',
                    controller: 'SupportRackTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SupportRackType', function(SupportRackType) {
                            return SupportRackType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('support-rack-type', null, { reload: 'support-rack-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('support-rack-type.delete', {
            parent: 'support-rack-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack-type/support-rack-type-delete-dialog.html',
                    controller: 'SupportRackTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SupportRackType', function(SupportRackType) {
                            return SupportRackType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('support-rack-type', null, { reload: 'support-rack-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
