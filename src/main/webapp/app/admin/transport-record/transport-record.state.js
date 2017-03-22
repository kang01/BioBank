/**
 * Created by gaokangkang on 2017/3/10.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('transport-record', {
                parent: 'admin',
                url: '/transport-record?page&sort',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'transportRecord.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/transport-record/transport-record.html',
                        controller: 'TransportRecordController',
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
                    }
                },
                resolve: {
                    pagingParams: ['$stateParams', 'PaginationUtil', function ($stateParams, PaginationUtil) {
                        return {
                            page: PaginationUtil.parsePage($stateParams.page),
                            sort: $stateParams.sort,
                            predicate: PaginationUtil.parsePredicate($stateParams.sort),
                            ascending: PaginationUtil.parseAscending($stateParams.sort)
                        };
                    }],
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('transport-record');
                        return $translate.refresh();
                    }]

                }
            })
            .state('transport-record-new', {
                parent: 'transport-record',
                url: 'transport-record/add',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'transportRecord.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/transport-record/transport-record-new.html',
                        controller: 'TransportRecordNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('transport-record');
                        return $translate.refresh();
                    }]
                }
            })
            .state('transport-record-edit', {
                parent: 'transport-record',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'transportRecord.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/admin/transport-record/transport-record-new.html',
                        controller: 'TransportRecordNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('transport-record');
                        return $translate.refresh();
                    }]
                }
            })
    }
})();
