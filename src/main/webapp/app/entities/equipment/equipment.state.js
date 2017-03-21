(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('equipment', {
            parent: 'entity',
            url: '/equipment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.equipment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/equipment/equipment.html',
                    controller: 'EquipmentController',
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
                    $translatePartialLoader.addPart('equipment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('equipment-detail', {
            parent: 'equipment',
            url: '/equipment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.equipment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/equipment/equipment-detail.html',
                    controller: 'EquipmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('equipment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Equipment', function($stateParams, Equipment) {
                    return Equipment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'equipment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('equipment-detail.edit', {
            parent: 'equipment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment/equipment-dialog.html',
                    controller: 'EquipmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Equipment', function(Equipment) {
                            return Equipment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('equipment.new', {
            parent: 'equipment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment/equipment-dialog.html',
                    controller: 'EquipmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                equipmentCode: null,
                                temperature: null,
                                equipmentAddress: null,
                                ampoulesMax: null,
                                ampoulesMin: null,
                                label1: null,
                                label2: null,
                                label4: null,
                                memo: null,
                                status: null,
                                label3: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('equipment', null, { reload: 'equipment' });
                }, function() {
                    $state.go('equipment');
                });
            }]
        })
        .state('equipment.edit', {
            parent: 'equipment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment/equipment-dialog.html',
                    controller: 'EquipmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Equipment', function(Equipment) {
                            return Equipment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('equipment', null, { reload: 'equipment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('equipment.delete', {
            parent: 'equipment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment/equipment-delete-dialog.html',
                    controller: 'EquipmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Equipment', function(Equipment) {
                            return Equipment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('equipment', null, { reload: 'equipment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
