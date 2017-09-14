/**
 * Created by gaokangkang on 2017/8/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('equipment-management', {
                parent: 'bizs',
                url: '/equipment-management',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: '项目管理'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/bizs/equipment-management/equipment-management.html',
                        controller: 'EquipmentManagementController',
                        controllerAs: 'vm'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {

                        return $translate.refresh();
                    }]

                }
            })
    }
})();
