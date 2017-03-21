(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('frozen-tube-record', {
            parent: 'entity',
            url: '/frozen-tube-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenTubeRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-tube-record/frozen-tube-records.html',
                    controller: 'FrozenTubeRecordController',
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
                    $translatePartialLoader.addPart('frozenTubeRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('frozen-tube-record-detail', {
            parent: 'frozen-tube-record',
            url: '/frozen-tube-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.frozenTubeRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/frozen-tube-record/frozen-tube-record-detail.html',
                    controller: 'FrozenTubeRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('frozenTubeRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'FrozenTubeRecord', function($stateParams, FrozenTubeRecord) {
                    return FrozenTubeRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'frozen-tube-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('frozen-tube-record-detail.edit', {
            parent: 'frozen-tube-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-record/frozen-tube-record-dialog.html',
                    controller: 'FrozenTubeRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenTubeRecord', function(FrozenTubeRecord) {
                            return FrozenTubeRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-tube-record.new', {
            parent: 'frozen-tube-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-record/frozen-tube-record-dialog.html',
                    controller: 'FrozenTubeRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                projectCode: null,
                                sampleTempCode: null,
                                sampleCode: null,
                                frozenTypeCode: null,
                                frozenTypeName: null,
                                sampleUsedTimesMost: null,
                                sampleUsedTimes: null,
                                frozenTubeVolumn: null,
                                frozenTubeVolumnUnit: null,
                                sampleTypeCode: null,
                                sampleTypeName: null,
                                frozenBoxCode: null,
                                tubeRows: null,
                                tubeColumns: null,
                                isModifyState: null,
                                memo: null,
                                status: null,
                                isModifyPosition: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('frozen-tube-record', null, { reload: 'frozen-tube-record' });
                }, function() {
                    $state.go('frozen-tube-record');
                });
            }]
        })
        .state('frozen-tube-record.edit', {
            parent: 'frozen-tube-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-record/frozen-tube-record-dialog.html',
                    controller: 'FrozenTubeRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['FrozenTubeRecord', function(FrozenTubeRecord) {
                            return FrozenTubeRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-tube-record', null, { reload: 'frozen-tube-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('frozen-tube-record.delete', {
            parent: 'frozen-tube-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/frozen-tube-record/frozen-tube-record-delete-dialog.html',
                    controller: 'FrozenTubeRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['FrozenTubeRecord', function(FrozenTubeRecord) {
                            return FrozenTubeRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('frozen-tube-record', null, { reload: 'frozen-tube-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
