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
            'selectize',
            'blockUI',
            'toastr',
            'angularMoment',
            'pageslide-directive',
            'colorpicker.module'
        ])
        .run(run);

    run.$inject = ['stateHandler', 'translationHandler','Auth'];

    function run(stateHandler, translationHandler,Auth) {
        stateHandler.initialize();
        translationHandler.initialize();


        // window.onbeforeunload = function (e) {
        //
        //     var message = '';
        //     e = e || window.event;
        //
        //     if (e) {
        //         e.returnValue = message;
        //         Auth.logout();
        //     }
        //
        //     return message;
        // };

        // $(window).bind('beforeunload', function(eventObject) {
        //     var returnValue = undefined;
        //     returnValue = "Do you really want to close?";
        //     eventObject.returnValue = returnValue;
        //
        //     Auth.logout();
        //
        //     return returnValue;
        // });
    }

    // zhuyu added for: Fix handsontable Comments plugin, mouseover bug, and classList property to document object.
    //noinspection JSAnnotator
    document.classList = {
        contains: function (className){
            return false;
        }
    };
    window.hasClass = function _hasClass(element, className) {
        var createClassNameRegExp = function createClassNameRegExp(className) {
            return new RegExp('(\\s|^)' + className + '(\\s|$)');
        };

        // http://snipplr.com/view/3561/addclass-removeclass-hasclass/
        return !!element.className.match(createClassNameRegExp(className));
    };
    // end
})();
