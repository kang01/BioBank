(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('equipment-group', {
            parent: 'entity',
            url: '/equipment-group?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.equipmentGroup.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/equipment-group/equipment-groups.html',
                    controller: 'EquipmentGroupController',
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
                    $translatePartialLoader.addPart('equipmentGroup');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('equipment-group-detail', {
            parent: 'equipment-group',
            url: '/equipment-group/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.equipmentGroup.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/equipment-group/equipment-group-detail.html',
                    controller: 'EquipmentGroupDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('equipmentGroup');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EquipmentGroup', function($stateParams, EquipmentGroup) {
                    return EquipmentGroup.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'equipment-group',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('equipment-group-detail.edit', {
            parent: 'equipment-group-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-group/equipment-group-dialog.html',
                    controller: 'EquipmentGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EquipmentGroup', function(EquipmentGroup) {
                            return EquipmentGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('equipment-group.new', {
            parent: 'equipment-group',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-group/equipment-group-dialog.html',
                    controller: 'EquipmentGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                equipmentGroupName: null,
                                equipmentGroupManagerId: null,
                                equipmentManagerName: null,
                                equipmentGroupAddress: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('equipment-group', null, { reload: 'equipment-group' });
                }, function() {
                    $state.go('equipment-group');
                });
            }]
        })
        .state('equipment-group.edit', {
            parent: 'equipment-group',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-group/equipment-group-dialog.html',
                    controller: 'EquipmentGroupDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EquipmentGroup', function(EquipmentGroup) {
                            return EquipmentGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('equipment-group', null, { reload: 'equipment-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('equipment-group.delete', {
            parent: 'equipment-group',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-group/equipment-group-delete-dialog.html',
                    controller: 'EquipmentGroupDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EquipmentGroup', function(EquipmentGroup) {
                            return EquipmentGroup.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('equipment-group', null, { reload: 'equipment-group' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
