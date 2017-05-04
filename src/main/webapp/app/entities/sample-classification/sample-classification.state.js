(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sample-classification', {
            parent: 'entity',
            url: '/sample-classification?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.sampleClassification.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sample-classification/sample-classifications.html',
                    controller: 'SampleClassificationController',
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
                    $translatePartialLoader.addPart('sampleClassification');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sample-classification-detail', {
            parent: 'sample-classification',
            url: '/sample-classification/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.sampleClassification.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sample-classification/sample-classification-detail.html',
                    controller: 'SampleClassificationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('sampleClassification');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SampleClassification', function($stateParams, SampleClassification) {
                    return SampleClassification.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sample-classification',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sample-classification-detail.edit', {
            parent: 'sample-classification-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-classification/sample-classification-dialog.html',
                    controller: 'SampleClassificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SampleClassification', function(SampleClassification) {
                            return SampleClassification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sample-classification.new', {
            parent: 'sample-classification',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-classification/sample-classification-dialog.html',
                    controller: 'SampleClassificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                sampleClassificationName: null,
                                sampleClassificationCode: null,
                                status: null,
                                memo: null,
                                frontColor: null,
                                backColor: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('sample-classification', null, { reload: 'sample-classification' });
                }, function() {
                    $state.go('sample-classification');
                });
            }]
        })
        .state('sample-classification.edit', {
            parent: 'sample-classification',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-classification/sample-classification-dialog.html',
                    controller: 'SampleClassificationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SampleClassification', function(SampleClassification) {
                            return SampleClassification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sample-classification', null, { reload: 'sample-classification' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sample-classification.delete', {
            parent: 'sample-classification',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sample-classification/sample-classification-delete-dialog.html',
                    controller: 'SampleClassificationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SampleClassification', function(SampleClassification) {
                            return SampleClassification.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sample-classification', null, { reload: 'sample-classification' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
