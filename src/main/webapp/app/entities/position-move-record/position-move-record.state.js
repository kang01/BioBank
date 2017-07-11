(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('position-move-record', {
            parent: 'entity',
            url: '/position-move-record?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionMoveRecord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-move-record/position-move-records.html',
                    controller: 'PositionMoveRecordController',
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
                    $translatePartialLoader.addPart('positionMoveRecord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('position-move-record-detail', {
            parent: 'position-move-record',
            url: '/position-move-record/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.positionMoveRecord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/position-move-record/position-move-record-detail.html',
                    controller: 'PositionMoveRecordDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('positionMoveRecord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'PositionMoveRecord', function($stateParams, PositionMoveRecord) {
                    return PositionMoveRecord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'position-move-record',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('position-move-record-detail.edit', {
            parent: 'position-move-record-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-move-record/position-move-record-dialog.html',
                    controller: 'PositionMoveRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionMoveRecord', function(PositionMoveRecord) {
                            return PositionMoveRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-move-record.new', {
            parent: 'position-move-record',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-move-record/position-move-record-dialog.html',
                    controller: 'PositionMoveRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                equipmentCode: null,
                                areaCode: null,
                                supportRackCode: null,
                                rowsInShelf: null,
                                columnsInShelf: null,
                                frozenBoxCode: null,
                                tubeRows: null,
                                tubeColumns: null,
                                moveReason: null,
                                moveAffect: null,
                                whetherFreezingAndThawing: null,
                                moveType: null,
                                operator1: null,
                                operator2: null,
                                projectCode: null,
                                projectSiteCode: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('position-move-record', null, { reload: 'position-move-record' });
                }, function() {
                    $state.go('position-move-record');
                });
            }]
        })
        .state('position-move-record.edit', {
            parent: 'position-move-record',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-move-record/position-move-record-dialog.html',
                    controller: 'PositionMoveRecordDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['PositionMoveRecord', function(PositionMoveRecord) {
                            return PositionMoveRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-move-record', null, { reload: 'position-move-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('position-move-record.delete', {
            parent: 'position-move-record',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/position-move-record/position-move-record-delete-dialog.html',
                    controller: 'PositionMoveRecordDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['PositionMoveRecord', function(PositionMoveRecord) {
                            return PositionMoveRecord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('position-move-record', null, { reload: 'position-move-record' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
