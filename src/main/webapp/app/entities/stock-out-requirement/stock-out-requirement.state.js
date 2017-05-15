(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('stock-out-requirement', {
            parent: 'entity',
            url: '/stock-out-requirement?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutRequirement.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-requirement/stock-out-requirements.html',
                    controller: 'StockOutRequirementController',
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
                    $translatePartialLoader.addPart('stockOutRequirement');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('stock-out-requirement-detail', {
            parent: 'stock-out-requirement',
            url: '/stock-out-requirement/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.stockOutRequirement.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/stock-out-requirement/stock-out-requirement-detail.html',
                    controller: 'StockOutRequirementDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('stockOutRequirement');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'StockOutRequirement', function($stateParams, StockOutRequirement) {
                    return StockOutRequirement.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'stock-out-requirement',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('stock-out-requirement-detail.edit', {
            parent: 'stock-out-requirement-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-requirement/stock-out-requirement-dialog.html',
                    controller: 'StockOutRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutRequirement', function(StockOutRequirement) {
                            return StockOutRequirement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-requirement.new', {
            parent: 'stock-out-requirement',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-requirement/stock-out-requirement-dialog.html',
                    controller: 'StockOutRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                apply_number: null,
                                requirementNumber: null,
                                requirementName: null,
                                countOfSample: null,
                                sex: null,
                                ageMin: null,
                                ageMax: null,
                                diseaseType: null,
                                isHemolysis: null,
                                isBloodLipid: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('stock-out-requirement', null, { reload: 'stock-out-requirement' });
                }, function() {
                    $state.go('stock-out-requirement');
                });
            }]
        })
        .state('stock-out-requirement.edit', {
            parent: 'stock-out-requirement',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-requirement/stock-out-requirement-dialog.html',
                    controller: 'StockOutRequirementDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['StockOutRequirement', function(StockOutRequirement) {
                            return StockOutRequirement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-requirement', null, { reload: 'stock-out-requirement' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('stock-out-requirement.delete', {
            parent: 'stock-out-requirement',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/stock-out-requirement/stock-out-requirement-delete-dialog.html',
                    controller: 'StockOutRequirementDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['StockOutRequirement', function(StockOutRequirement) {
                            return StockOutRequirement.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('stock-out-requirement', null, { reload: 'stock-out-requirement' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
