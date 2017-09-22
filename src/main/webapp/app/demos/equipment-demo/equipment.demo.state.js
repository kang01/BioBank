/**
 * Created by gaokangkang on 2017/9/21.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('equipment-demo', {
                parent: 'demos',
                url: '/equipment-demo',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'EquipmentDemo'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/demos/equipment-demo/equipment-demo.html',
                        controller: 'DemoEquipmentController',
                        controllerAs: 'vm'
                    }
                },
                params: {

                },
                resolve: {
                }
            });

    }
})();
