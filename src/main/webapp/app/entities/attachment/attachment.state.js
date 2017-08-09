(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('attachment', {
            parent: 'entity',
            url: '/attachment?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.attachment.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/attachment/attachments.html',
                    controller: 'AttachmentController',
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
                    $translatePartialLoader.addPart('attachment');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('attachment-detail', {
            parent: 'attachment',
            url: '/attachment/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.attachment.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/attachment/attachment-detail.html',
                    controller: 'AttachmentDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('attachment');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Attachment', function($stateParams, Attachment) {
                    return Attachment.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'attachment',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('attachment-detail.edit', {
            parent: 'attachment-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attachment/attachment-dialog.html',
                    controller: 'AttachmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Attachment', function(Attachment) {
                            return Attachment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('attachment.new', {
            parent: 'attachment',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attachment/attachment-dialog.html',
                    controller: 'AttachmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                businessId: null,
                                businessType: null,
                                fileId1: null,
                                fileId2: null,
                                fileTitle: null,
                                fileName: null,
                                description: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('attachment', null, { reload: 'attachment' });
                }, function() {
                    $state.go('attachment');
                });
            }]
        })
        .state('attachment.edit', {
            parent: 'attachment',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attachment/attachment-dialog.html',
                    controller: 'AttachmentDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Attachment', function(Attachment) {
                            return Attachment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('attachment', null, { reload: 'attachment' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('attachment.delete', {
            parent: 'attachment',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/attachment/attachment-delete-dialog.html',
                    controller: 'AttachmentDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Attachment', function(Attachment) {
                            return Attachment.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('attachment', null, { reload: 'attachment' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
