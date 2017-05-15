(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('user-login-history', {
            parent: 'entity',
            url: '/user-login-history?page&sort&search',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.userLoginHistory.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-login-history/user-login-histories.html',
                    controller: 'UserLoginHistoryController',
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
                    $translatePartialLoader.addPart('userLoginHistory');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('user-login-history-detail', {
            parent: 'user-login-history',
            url: '/user-login-history/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'bioBankApp.userLoginHistory.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/user-login-history/user-login-history-detail.html',
                    controller: 'UserLoginHistoryDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('userLoginHistory');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'UserLoginHistory', function($stateParams, UserLoginHistory) {
                    return UserLoginHistory.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'user-login-history',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('user-login-history-detail.edit', {
            parent: 'user-login-history-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-login-history/user-login-history-dialog.html',
                    controller: 'UserLoginHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLoginHistory', function(UserLoginHistory) {
                            return UserLoginHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-login-history.new', {
            parent: 'user-login-history',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-login-history/user-login-history-dialog.html',
                    controller: 'UserLoginHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                userToken: null,
                                loginUserId: null,
                                businessName: null,
                                invalidDate: null,
                                status: null,
                                memo: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('user-login-history', null, { reload: 'user-login-history' });
                }, function() {
                    $state.go('user-login-history');
                });
            }]
        })
        .state('user-login-history.edit', {
            parent: 'user-login-history',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-login-history/user-login-history-dialog.html',
                    controller: 'UserLoginHistoryDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['UserLoginHistory', function(UserLoginHistory) {
                            return UserLoginHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-login-history', null, { reload: 'user-login-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('user-login-history.delete', {
            parent: 'user-login-history',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/user-login-history/user-login-history-delete-dialog.html',
                    controller: 'UserLoginHistoryDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['UserLoginHistory', function(UserLoginHistory) {
                            return UserLoginHistory.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('user-login-history', null, { reload: 'user-login-history' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
