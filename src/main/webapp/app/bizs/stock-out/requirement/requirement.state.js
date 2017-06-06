/**
 * Created by gaokangkang on 2017/5/12.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('requirement-list', {
                parent: 'bizs',
                url: '/requirement?page&sort',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockIn.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/requirement/requirement-list.html',
                        controller: 'requirementListController',
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
                        return $translate.refresh();
                    }]

                }
            })
            .state('requirement-new', {
                parent: 'bizs',
                url: '/requirement-list/{applyId}/{applyCode}/new',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockOut.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/requirement/requirement-detail.html',
                        controller: 'RequirementDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: function () {
                        return {
                            delegateId:null,
                            recordTime:new Date(),
                            startTime:new Date(),
                            endTime:new Date()
                        };
                    }
                }
            })
            .state('requirement-edit', {
                parent: 'bizs',
                url: '/requirement-list/{applyId}/edit/{addApplyFlag}',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockOut.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/requirement/requirement-detail.html',
                        controller: 'RequirementDetailController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RequirementService', function($stateParams, RequirementService) {
                        return RequirementService.queryRequirementDesc($stateParams.applyId);
                    }]
                }
            })
            .state('requirement-additionApply', {
                parent: 'bizs',
                url: '/requirement-list/{applyId}/additionApply',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockOut.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/requirement/requirement-additionApply.html',
                        controller: 'RequirementAdditionApplyController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RequirementService', function($stateParams, RequirementService) {
                        return RequirementService.queryRequirementDesc($stateParams.applyId);
                    }]
                }
            })
            .state('requirement-view', {
                parent: 'bizs',
                url: '/requirement-list/{applyId}/view/{viewFlag}',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'stockOut.new.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/stock-out/requirement/requirement-view.html',
                        controller: 'RequirementAdditionApplyController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'RequirementService', function($stateParams, RequirementService) {
                        return RequirementService.queryRequirementDesc($stateParams.applyId);
                    }]
                }
            })
    }
})();
