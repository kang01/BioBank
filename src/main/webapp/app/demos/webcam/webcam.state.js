/**
 * Created by zhuyu on 2017/3/10.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
            .state('webcam', {
                parent: 'demos',
                url: '/webcam',
                data: {
                    authorities: ['ROLE_USER','ROLE_ADMIN'],
                    pageTitle: 'WebCam'
                },
                views: {
                    'content@': {
                        templateUrl: 'app/demos/webcam/webcam.html',
                        controller: 'DemoWebCamController',
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
