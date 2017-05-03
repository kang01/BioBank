(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-sample-class', {
            parent: 'entity',
            url: '/project-sample-class?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.projectSampleClass.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-sample-class/project-sample-classes.html',
                    controller: 'ProjectSampleClassController',
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
                    $translatePartialLoader.addPart('projectSampleClass');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-sample-class-detail', {
            parent: 'project-sample-class',
            url: '/project-sample-class/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.projectSampleClass.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-sample-class/project-sample-class-detail.html',
                    controller: 'ProjectSampleClassDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectSampleClass');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectSampleClass', function($stateParams, ProjectSampleClass) {
                    return ProjectSampleClass.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-sample-class',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-sample-class-detail.edit', {
            parent: 'project-sample-class-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-sample-class/project-sample-class-dialog.html',
                    controller: 'ProjectSampleClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectSampleClass', function(ProjectSampleClass) {
                            return ProjectSampleClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-sample-class.new', {
            parent: 'project-sample-class',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-sample-class/project-sample-class-dialog.html',
                    controller: 'ProjectSampleClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                projectCode: null,
                                status: null,
                                memo: null,
                                columnsNumber: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-sample-class', null, { reload: 'project-sample-class' });
                }, function() {
                    $state.go('project-sample-class');
                });
            }]
        })
        .state('project-sample-class.edit', {
            parent: 'project-sample-class',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-sample-class/project-sample-class-dialog.html',
                    controller: 'ProjectSampleClassDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectSampleClass', function(ProjectSampleClass) {
                            return ProjectSampleClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-sample-class', null, { reload: 'project-sample-class' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-sample-class.delete', {
            parent: 'project-sample-class',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-sample-class/project-sample-class-delete-dialog.html',
                    controller: 'ProjectSampleClassDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectSampleClass', function(ProjectSampleClass) {
                            return ProjectSampleClass.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-sample-class', null, { reload: 'project-sample-class' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
