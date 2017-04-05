(function() {
    'use strict';

    angular
        .module('bioBankApp', [
            'ngStorage',
            'tmh.dynamicLocale',
            'pascalprecht.translate',
            'ngResource',
            'ngCookies',
            'ngAria',
            'ngCacheBuster',
            'ngFileUpload',
            'ui.bootstrap',
            'ui.bootstrap.datetimepicker',
            'ui.router',
            'infinite-scroll',
            'ds.objectDiff',
            // jhipster-needle-angularjs-add-module JHipster will add new module here
            'angular-loading-bar',
            'datatables',
            'datatables.light-columnfilter',
            'datatables.columnfilter',
            'datatables.bootstrap',
            'datatables.buttons',
            'ngHandsontable',
            'frapontillo.bootstrap-switch',
            'datatables.scroller',
            'selectize'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler'];

    function run(stateHandler, translationHandler) {
        stateHandler.initialize();
        translationHandler.initialize();
    }
})();
