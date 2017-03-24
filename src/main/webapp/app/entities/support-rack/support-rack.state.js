(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('support-rack', {
            parent: 'entity',
            url: '/support-rack?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.supportRack.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/support-rack/support-racks.html',
                    controller: 'SupportRackController',
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
                    $translatePartialLoader.addPart('supportRack');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('support-rack-detail', {
            parent: 'support-rack',
            url: '/support-rack/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.supportRack.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/support-rack/support-rack-detail.html',
                    controller: 'SupportRackDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('supportRack');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SupportRack', function($stateParams, SupportRack) {
                    return SupportRack.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'support-rack',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('support-rack-detail.edit', {
            parent: 'support-rack-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack/support-rack-dialog.html',
                    controller: 'SupportRackDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SupportRack', function(SupportRack) {
                            return SupportRack.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('support-rack.new', {
            parent: 'support-rack',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack/support-rack-dialog.html',
                    controller: 'SupportRackDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                supportRackTypeCode: null,
                                areaCode: null,
                                memo: null,
                                status: null,
                                supportRackCode: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('support-rack', null, { reload: 'support-rack' });
                }, function() {
                    $state.go('support-rack');
                });
            }]
        })
        .state('support-rack.edit', {
            parent: 'support-rack',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack/support-rack-dialog.html',
                    controller: 'SupportRackDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SupportRack', function(SupportRack) {
                            return SupportRack.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('support-rack', null, { reload: 'support-rack' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('support-rack.delete', {
            parent: 'support-rack',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/support-rack/support-rack-delete-dialog.html',
                    controller: 'SupportRackDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SupportRack', function(SupportRack) {
                            return SupportRack.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('support-rack', null, { reload: 'support-rack' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
