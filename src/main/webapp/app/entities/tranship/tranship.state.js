(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranship', {
            parent: 'entity',
            url: '/tranship?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.tranship.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship/tranships.html',
                    controller: 'TranshipController',
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
                    $translatePartialLoader.addPart('tranship');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranship-detail', {
            parent: 'tranship',
            url: '/tranship/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.tranship.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship/tranship-detail.html',
                    controller: 'TranshipDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('tranship');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Tranship', function($stateParams, Tranship) {
                    return Tranship.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranship',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranship-detail.edit', {
            parent: 'tranship-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship/tranship-dialog.html',
                    controller: 'TranshipDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tranship', function(Tranship) {
                            return Tranship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship.new', {
            parent: 'tranship',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship/tranship-dialog.html',
                    controller: 'TranshipDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                transhipDate: null,
                                projectCode: null,
                                projectName: null,
                                projectSiteCode: null,
                                projectSiteName: null,
                                trackNumber: null,
                                transhipBatch: null,
                                transhipState: null,
                                receiver: null,
                                receiveDate: null,
                                sampleNumber: null,
                                frozenBoxNumber: null,
                                emptyTubeNumber: null,
                                emptyHoleNumber: null,
                                sampleSatisfaction: null,
                                effectiveSampleNumber: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranship', null, { reload: 'tranship' });
                }, function() {
                    $state.go('tranship');
                });
            }]
        })
        .state('tranship.edit', {
            parent: 'tranship',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship/tranship-dialog.html',
                    controller: 'TranshipDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Tranship', function(Tranship) {
                            return Tranship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship', null, { reload: 'tranship' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship.delete', {
            parent: 'tranship',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship/tranship-delete-dialog.html',
                    controller: 'TranshipDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Tranship', function(Tranship) {
                            return Tranship.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship', null, { reload: 'tranship' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
