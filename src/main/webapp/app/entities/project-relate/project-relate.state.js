(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('project-relate', {
            parent: 'entity',
            url: '/project-relate?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.projectRelate.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-relate/project-relates.html',
                    controller: 'ProjectRelateController',
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
                    $translatePartialLoader.addPart('projectRelate');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('project-relate-detail', {
            parent: 'project-relate',
            url: '/project-relate/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.projectRelate.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/project-relate/project-relate-detail.html',
                    controller: 'ProjectRelateDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('projectRelate');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProjectRelate', function($stateParams, ProjectRelate) {
                    return ProjectRelate.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'project-relate',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('project-relate-detail.edit', {
            parent: 'project-relate-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-relate/project-relate-dialog.html',
                    controller: 'ProjectRelateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRelate', function(ProjectRelate) {
                            return ProjectRelate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-relate.new', {
            parent: 'project-relate',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-relate/project-relate-dialog.html',
                    controller: 'ProjectRelateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('project-relate', null, { reload: 'project-relate' });
                }, function() {
                    $state.go('project-relate');
                });
            }]
        })
        .state('project-relate.edit', {
            parent: 'project-relate',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-relate/project-relate-dialog.html',
                    controller: 'ProjectRelateDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProjectRelate', function(ProjectRelate) {
                            return ProjectRelate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-relate', null, { reload: 'project-relate' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('project-relate.delete', {
            parent: 'project-relate',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/project-relate/project-relate-delete-dialog.html',
                    controller: 'ProjectRelateDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProjectRelate', function(ProjectRelate) {
                            return ProjectRelate.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('project-relate', null, { reload: 'project-relate' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
