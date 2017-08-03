(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('position-change-record', {
            parent: 'entity',
            url: '/position-change-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionChangeRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-change-record/position-change-records.html',
                    controller: 'PositionChangeRecordController',
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
                    $translatePartialLoader.addPart('positionChangeRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('position-change-record-detail', {
            parent: 'position-change-record',
            url: '/position-change-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionChangeRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-change-record/position-change-record-detail.html',
                    controller: 'PositionChangeRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('positionChangeRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PositionChangeRecord', function($stateParams, PositionChangeRecord) {
                    return PositionChangeRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'position-change-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('position-change-record-detail.edit', {
            parent: 'position-change-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change-record/position-change-record-dialog.html',
                    controller: 'PositionChangeRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionChangeRecord', function(PositionChangeRecord) {
                            return PositionChangeRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-change-record.new', {
            parent: 'position-change-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change-record/position-change-record-dialog.html',
                    controller: 'PositionChangeRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('position-change-record', null, { reload: 'position-change-record' });
                }, function() {
                    $state.go('position-change-record');
                });
            }]
        })
        .state('position-change-record.edit', {
            parent: 'position-change-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change-record/position-change-record-dialog.html',
                    controller: 'PositionChangeRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionChangeRecord', function(PositionChangeRecord) {
                            return PositionChangeRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-change-record', null, { reload: 'position-change-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-change-record.delete', {
            parent: 'position-change-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-change-record/position-change-record-delete-dialog.html',
                    controller: 'PositionChangeRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PositionChangeRecord', function(PositionChangeRecord) {
                            return PositionChangeRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-change-record', null, { reload: 'position-change-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
