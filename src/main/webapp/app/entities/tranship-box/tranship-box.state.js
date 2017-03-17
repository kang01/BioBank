(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('tranship-box', {
            parent: 'entity',
            url: '/tranship-box?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipBox.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-box/tranship-boxes.html',
                    controller: 'TranshipBoxController',
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
                    $translatePartialLoader.addPart('transhipBox');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('tranship-box-detail', {
            parent: 'tranship-box',
            url: '/tranship-box/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.transhipBox.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/tranship-box/tranship-box-detail.html',
                    controller: 'TranshipBoxDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('transhipBox');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'TranshipBox', function($stateParams, TranshipBox) {
                    return TranshipBox.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'tranship-box',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('tranship-box-detail.edit', {
            parent: 'tranship-box-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box/tranship-box-dialog.html',
                    controller: 'TranshipBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipBox', function(TranshipBox) {
                            return TranshipBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-box.new', {
            parent: 'tranship-box',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box/tranship-box-dialog.html',
                    controller: 'TranshipBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                frozenBoxCode: null,
                                equipmentCode: null,
                                areaCode: null,
                                supportRackCode: null,
                                rowsInShelf: null,
                                columnsInShelf: null,
                                memo: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('tranship-box', null, { reload: 'tranship-box' });
                }, function() {
                    $state.go('tranship-box');
                });
            }]
        })
        .state('tranship-box.edit', {
            parent: 'tranship-box',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box/tranship-box-dialog.html',
                    controller: 'TranshipBoxDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['TranshipBox', function(TranshipBox) {
                            return TranshipBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-box', null, { reload: 'tranship-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('tranship-box.delete', {
            parent: 'tranship-box',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/tranship-box/tranship-box-delete-dialog.html',
                    controller: 'TranshipBoxDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['TranshipBox', function(TranshipBox) {
                            return TranshipBox.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('tranship-box', null, { reload: 'tranship-box' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
