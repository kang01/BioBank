(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('serial-no', {
            parent: 'entity',
            url: '/serial-no?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.serialNo.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serial-no/serial-nos.html',
                    controller: 'SerialNoController',
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
                    $translatePartialLoader.addPart('serialNo');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('serial-no-detail', {
            parent: 'serial-no',
            url: '/serial-no/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.serialNo.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serial-no/serial-no-detail.html',
                    controller: 'SerialNoDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serialNo');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SerialNo', function($stateParams, SerialNo) {
                    return SerialNo.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'serial-no',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('serial-no-detail.edit', {
            parent: 'serial-no-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serial-no/serial-no-dialog.html',
                    controller: 'SerialNoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SerialNo', function(SerialNo) {
                            return SerialNo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serial-no.new', {
            parent: 'serial-no',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serial-no/serial-no-dialog.html',
                    controller: 'SerialNoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                serialNo: null,
                                machineNo: null,
                                status: null,
                                memo: null,
                                usedDate: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('serial-no', null, { reload: 'serial-no' });
                }, function() {
                    $state.go('serial-no');
                });
            }]
        })
        .state('serial-no.edit', {
            parent: 'serial-no',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serial-no/serial-no-dialog.html',
                    controller: 'SerialNoDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SerialNo', function(SerialNo) {
                            return SerialNo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serial-no', null, { reload: 'serial-no' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serial-no.delete', {
            parent: 'serial-no',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serial-no/serial-no-delete-dialog.html',
                    controller: 'SerialNoDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SerialNo', function(SerialNo) {
                            return SerialNo.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serial-no', null, { reload: 'serial-no' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
