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
                parent: 'bizs',
                url: '/transport-record?page&sort',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'transportRecord.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/transport-record/transport-record.html',
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
                parent: 'bizs',
                url: '/transport-record/{transhipId}/{transhipCode}/add',
                params:{transhipId : null,transhipCode : null},
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'transportRecord.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/transport-record/transport-record-new.html',
                        controller: 'TransportRecordNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('transport-record');
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {
                            effectiveSampleNumber: 0,
                            emptyHoleNumber: 0,
                            emptyTubeNumber: 0,
                            frozenBoxNumber: 0,//冻存盒数
                            memo:null ,
                            projectCode: null,
                            projectId: null,
                            projectName:null ,
                            projectSiteCode:null,
                            projectSiteId: null,
                            projectSiteName: null,
                            receiveDate: null,
                            receiver:null,
                            sampleNumber:null,
                            sampleSatisfaction: null,
                            trackNumber: null,
                            transhipBatch: null,
                            transhipDate: null,
                            status: null,
                            transhipState: null,
                            transhipCode:null

                        };
                    }

                }
            })
            .state('transport-record-edit', {
                parent: 'bizs',
                url: '/transport-record/{id}/edit',
                data: {
                    authorities: ['ROLE_ADMIN'],
                    pageTitle: 'transportRecord.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/transport-record/transport-record-new.html',
                        controller: 'TransportRecordNewController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('transport-record');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'TransportRecordService', function($stateParams, TransportRecordService) {
                        return TransportRecordService.get({id : $stateParams.id}).$promise;
                    }]
                }
            })
    }
})();