(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('position-destroy-record', {
            parent: 'entity',
            url: '/position-destroy-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionDestroyRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-destroy-record/position-destroy-records.html',
                    controller: 'PositionDestroyRecordController',
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
                    $translatePartialLoader.addPart('positionDestroyRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('position-destroy-record-detail', {
            parent: 'position-destroy-record',
            url: '/position-destroy-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionDestroyRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-destroy-record/position-destroy-record-detail.html',
                    controller: 'PositionDestroyRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('positionDestroyRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PositionDestroyRecord', function($stateParams, PositionDestroyRecord) {
                    return PositionDestroyRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'position-destroy-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('position-destroy-record-detail.edit', {
            parent: 'position-destroy-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy-record/position-destroy-record-dialog.html',
                    controller: 'PositionDestroyRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionDestroyRecord', function(PositionDestroyRecord) {
                            return PositionDestroyRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-destroy-record.new', {
            parent: 'position-destroy-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy-record/position-destroy-record-dialog.html',
                    controller: 'PositionDestroyRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('position-destroy-record', null, { reload: 'position-destroy-record' });
                }, function() {
                    $state.go('position-destroy-record');
                });
            }]
        })
        .state('position-destroy-record.edit', {
            parent: 'position-destroy-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy-record/position-destroy-record-dialog.html',
                    controller: 'PositionDestroyRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionDestroyRecord', function(PositionDestroyRecord) {
                            return PositionDestroyRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-destroy-record', null, { reload: 'position-destroy-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-destroy-record.delete', {
            parent: 'position-destroy-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-destroy-record/position-destroy-record-delete-dialog.html',
                    controller: 'PositionDestroyRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PositionDestroyRecord', function(PositionDestroyRecord) {
                            return PositionDestroyRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-destroy-record', null, { reload: 'position-destroy-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
