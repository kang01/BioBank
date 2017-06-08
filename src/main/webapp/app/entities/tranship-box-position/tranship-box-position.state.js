(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranship-box-position', {
            parent: 'entity',
            url: '/tranship-box-position?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipBoxPosition.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-box-position/tranship-box-positions.html',
                    controller: 'TranshipBoxPositionController',
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
                    $translatePartialLoader.addPart('transhipBoxPosition');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranship-box-position-detail', {
            parent: 'tranship-box-position',
            url: '/tranship-box-position/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipBoxPosition.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-box-position/tranship-box-position-detail.html',
                    controller: 'TranshipBoxPositionDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transhipBoxPosition');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TranshipBoxPosition', function($stateParams, TranshipBoxPosition) {
                    return TranshipBoxPosition.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranship-box-position',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranship-box-position-detail.edit', {
            parent: 'tranship-box-position-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box-position/tranship-box-position-dialog.html',
                    controller: 'TranshipBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipBoxPosition', function(TranshipBoxPosition) {
                            return TranshipBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-box-position.new', {
            parent: 'tranship-box-position',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box-position/tranship-box-position-dialog.html',
                    controller: 'TranshipBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                status: null,
                                memo: null,
                                equipmentCode: null,
                                areaCode: null,
                                supportRackCode: null,
                                rowsInShelf: null,
                                columnsInShelf: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranship-box-position', null, { reload: 'tranship-box-position' });
                }, function() {
                    $state.go('tranship-box-position');
                });
            }]
        })
        .state('tranship-box-position.edit', {
            parent: 'tranship-box-position',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box-position/tranship-box-position-dialog.html',
                    controller: 'TranshipBoxPositionDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipBoxPosition', function(TranshipBoxPosition) {
                            return TranshipBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-box-position', null, { reload: 'tranship-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-box-position.delete', {
            parent: 'tranship-box-position',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box-position/tranship-box-position-delete-dialog.html',
                    controller: 'TranshipBoxPositionDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TranshipBoxPosition', function(TranshipBoxPosition) {
                            return TranshipBoxPosition.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-box-position', null, { reload: 'tranship-box-position' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
