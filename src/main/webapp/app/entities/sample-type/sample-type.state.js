(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sample-type', {
            parent: 'entity',
            url: '/sample-type?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.sampleType.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sample-type/sample-types.html',
                    controller: 'SampleTypeController',
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
                    $translatePartialLoader.addPart('sampleType');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sample-type-detail', {
            parent: 'sample-type',
            url: '/sample-type/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.sampleType.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sample-type/sample-type-detail.html',
                    controller: 'SampleTypeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sampleType');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SampleType', function($stateParams, SampleType) {
                    return SampleType.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sample-type',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sample-type-detail.edit', {
            parent: 'sample-type-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-type/sample-type-dialog.html',
                    controller: 'SampleTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SampleType', function(SampleType) {
                            return SampleType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sample-type.new', {
            parent: 'sample-type',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-type/sample-type-dialog.html',
                    controller: 'SampleTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sampleTypeCode: null,
                                sampleTypeName: null,
                                memo: null,
                                status: null,
                                frontColor: null,
                                backColor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sample-type', null, { reload: 'sample-type' });
                }, function() {
                    $state.go('sample-type');
                });
            }]
        })
        .state('sample-type.edit', {
            parent: 'sample-type',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-type/sample-type-dialog.html',
                    controller: 'SampleTypeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SampleType', function(SampleType) {
                            return SampleType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sample-type', null, { reload: 'sample-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sample-type.delete', {
            parent: 'sample-type',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-type/sample-type-delete-dialog.html',
                    controller: 'SampleTypeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SampleType', function(SampleType) {
                            return SampleType.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sample-type', null, { reload: 'sample-type' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
