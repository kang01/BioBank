(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frozen-box', {
            parent: 'entity',
            url: '/frozen-box?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenBox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-box/frozen-boxes.html',
                    controller: 'FrozenBoxController',
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
                    $translatePartialLoader.addPart('frozenBox');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frozen-box-detail', {
            parent: 'frozen-box',
            url: '/frozen-box/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenBox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-box/frozen-box-detail.html',
                    controller: 'FrozenBoxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frozenBox');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FrozenBox', function($stateParams, FrozenBox) {
                    return FrozenBox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frozen-box',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frozen-box-detail.edit', {
            parent: 'frozen-box-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box/frozen-box-dialog.html',
                    controller: 'FrozenBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenBox', function(FrozenBox) {
                            return FrozenBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-box.new', {
            parent: 'frozen-box',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box/frozen-box-dialog.html',
                    controller: 'FrozenBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                frozenBoxCode: null,
                                frozenBoxTypeCode: null,
                                frozenBoxRows: null,
                                frozenBoxColumns: null,
                                projectCode: null,
                                projectName: null,
                                projectSiteCode: null,
                                projectSiteName: null,
                                equipmentCode: null,
                                areaCode: null,
                                supportRackCode: null,
                                sampleTypeCode: null,
                                sampleTypeName: null,
                                sampleNumber: null,
                                isSplit: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frozen-box', null, { reload: 'frozen-box' });
                }, function() {
                    $state.go('frozen-box');
                });
            }]
        })
        .state('frozen-box.edit', {
            parent: 'frozen-box',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box/frozen-box-dialog.html',
                    controller: 'FrozenBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenBox', function(FrozenBox) {
                            return FrozenBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-box', null, { reload: 'frozen-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-box.delete', {
            parent: 'frozen-box',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-box/frozen-box-delete-dialog.html',
                    controller: 'FrozenBoxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FrozenBox', function(FrozenBox) {
                            return FrozenBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-box', null, { reload: 'frozen-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
