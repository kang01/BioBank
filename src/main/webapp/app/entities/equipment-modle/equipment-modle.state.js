(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('equipment-modle', {
            parent: 'entity',
            url: '/equipment-modle?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.equipmentModle.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/equipment-modle/equipment-modles.html',
                    controller: 'EquipmentModleController',
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
                    $translatePartialLoader.addPart('equipmentModle');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('equipment-modle-detail', {
            parent: 'equipment-modle',
            url: '/equipment-modle/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.equipmentModle.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/equipment-modle/equipment-modle-detail.html',
                    controller: 'EquipmentModleDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('equipmentModle');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'EquipmentModle', function($stateParams, EquipmentModle) {
                    return EquipmentModle.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'equipment-modle',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('equipment-modle-detail.edit', {
            parent: 'equipment-modle-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-modle/equipment-modle-dialog.html',
                    controller: 'EquipmentModleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EquipmentModle', function(EquipmentModle) {
                            return EquipmentModle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('equipment-modle.new', {
            parent: 'equipment-modle',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-modle/equipment-modle-dialog.html',
                    controller: 'EquipmentModleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                equipmentModelCode: null,
                                equipmentModelName: null,
                                equipmentType: null,
                                areaNumber: null,
                                shelveNumberInArea: null,
                                memo: null,
                                status: null,
                                temperatureMax: null,
                                temperatureMin: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('equipment-modle', null, { reload: 'equipment-modle' });
                }, function() {
                    $state.go('equipment-modle');
                });
            }]
        })
        .state('equipment-modle.edit', {
            parent: 'equipment-modle',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-modle/equipment-modle-dialog.html',
                    controller: 'EquipmentModleDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['EquipmentModle', function(EquipmentModle) {
                            return EquipmentModle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('equipment-modle', null, { reload: 'equipment-modle' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('equipment-modle.delete', {
            parent: 'equipment-modle',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/equipment-modle/equipment-modle-delete-dialog.html',
                    controller: 'EquipmentModleDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['EquipmentModle', function(EquipmentModle) {
                            return EquipmentModle.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('equipment-modle', null, { reload: 'equipment-modle' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
