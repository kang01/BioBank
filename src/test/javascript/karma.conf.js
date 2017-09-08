// Karma configuration
// http://karma-runner.github.io/0.13/config/configuration-file.html

var sourcePreprocessors = ['coverage'];

function isDebug() {
    return process.argv.indexOf('--debug') >= 0;
}

if (isDebug()) {
    // Disable JS minification if Karma is run with debug option.
    sourcePreprocessors = [];
}

module.exports = function (config) {
    config.set({
        // base path, that will be used to resolve files and exclude
        basePath: 'src/test/javascript/'.replace(/[^/]+/g, '..'),

        // testing framework to use (jasmine/mocha/qunit/...)
        frameworks: ['jasmine'],

        // list of files / patterns to load in the browser
        files: [
            // bower:js
            'src/main/webapp/bower_components/jquery/dist/jquery.js',
            'src/main/webapp/bower_components/messageformat/messageformat.js',
            'src/main/webapp/bower_components/json3/lib/json3.js',
            'src/main/webapp/bower_components/lodash/lodash.js',
            'src/main/webapp/bower_components/datatables.net/js/jquery.dataTables.js',
            'src/main/webapp/bower_components/moment/moment.js',
            'src/main/webapp/bower_components/numbro/numbro.js',
            'src/main/webapp/bower_components/pikaday/pikaday.js',
            'src/main/webapp/bower_components/datatables/media/js/jquery.dataTables.js',
            'src/main/webapp/bower_components/select2/dist/js/select2.js',
            'src/main/webapp/bower_components/datatables-light-columnfilter/dist/dataTables.lightColumnFilter.js',
            'src/main/webapp/bower_components/bootstrap-switch/dist/js/bootstrap-switch.js',
            'src/main/webapp/bower_components/sifter/sifter.js',
            'src/main/webapp/bower_components/microplugin/src/microplugin.js',
            'src/main/webapp/bower_components/datatables-buttons/js/dataTables.buttons.js',
            'src/main/webapp/bower_components/datatables-buttons/js/buttons.colVis.js',
            'src/main/webapp/bower_components/datatables-buttons/js/buttons.flash.js',
            'src/main/webapp/bower_components/datatables-buttons/js/buttons.html5.js',
            'src/main/webapp/bower_components/datatables-buttons/js/buttons.print.js',
            'src/main/webapp/bower_components/ion.rangeSlider/js/ion.rangeSlider.js',
            'src/main/webapp/bower_components/copycss/jquery.copycss.js',
            'src/main/webapp/bower_components/angular-bootstrap-colorpicker/js/bootstrap-colorpicker-module.js',
            'src/main/webapp/bower_components/angular/angular.js',
            'src/main/webapp/bower_components/angular-aria/angular-aria.js',
            'src/main/webapp/bower_components/angular-bootstrap/ui-bootstrap-tpls.js',
            'src/main/webapp/bower_components/angular-cache-buster/angular-cache-buster.js',
            'src/main/webapp/bower_components/angular-cookies/angular-cookies.js',
            'src/main/webapp/bower_components/angular-dynamic-locale/src/tmhDynamicLocale.js',
            'src/main/webapp/bower_components/ngstorage/ngStorage.js',
            'src/main/webapp/bower_components/angular-loading-bar/build/loading-bar.js',
            'src/main/webapp/bower_components/angular-resource/angular-resource.js',
            'src/main/webapp/bower_components/angular-sanitize/angular-sanitize.js',
            'src/main/webapp/bower_components/angular-translate/angular-translate.js',
            'src/main/webapp/bower_components/angular-translate-interpolation-messageformat/angular-translate-interpolation-messageformat.js',
            'src/main/webapp/bower_components/angular-translate-loader-partial/angular-translate-loader-partial.js',
            'src/main/webapp/bower_components/angular-translate-storage-cookie/angular-translate-storage-cookie.js',
            'src/main/webapp/bower_components/angular-ui-router/release/angular-ui-router.js',
            'src/main/webapp/bower_components/bootstrap-ui-datetime-picker/dist/datetime-picker.js',
            'src/main/webapp/bower_components/ng-file-upload/ng-file-upload.js',
            'src/main/webapp/bower_components/ngInfiniteScroll/build/ng-infinite-scroll.js',
            'src/main/webapp/bower_components/angular-datatables/dist/angular-datatables.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/bootstrap/angular-datatables.bootstrap.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/colreorder/angular-datatables.colreorder.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/columnfilter/angular-datatables.columnfilter.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/light-columnfilter/angular-datatables.light-columnfilter.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/colvis/angular-datatables.colvis.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/fixedcolumns/angular-datatables.fixedcolumns.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/fixedheader/angular-datatables.fixedheader.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/scroller/angular-datatables.scroller.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/tabletools/angular-datatables.tabletools.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/buttons/angular-datatables.buttons.js',
            'src/main/webapp/bower_components/angular-datatables/dist/plugins/select/angular-datatables.select.js',
            'src/main/webapp/bower_components/handsontable/dist/handsontable.js',
            'src/main/webapp/bower_components/jquery-datatables-columnfilter/jquery.dataTables.columnFilter.js',
            'src/main/webapp/bower_components/angular-object-diff/dist/angular-object-diff.js',
            'src/main/webapp/bower_components/angular-bootstrap-switch/dist/angular-bootstrap-switch.js',
            'src/main/webapp/bower_components/selectize/dist/js/selectize.js',
            'src/main/webapp/bower_components/angular-block-ui/dist/angular-block-ui.js',
            'src/main/webapp/bower_components/angular-toastr/dist/angular-toastr.tpls.js',
            'src/main/webapp/bower_components/angular-moment/angular-moment.js',
            'src/main/webapp/bower_components/angular-pageslide-directive/dist/angular-pageslide-directive.js',
            'src/main/webapp/bower_components/angular-mocks/angular-mocks.js',
            'src/main/webapp/bower_components/ngHandsontable/dist/ngHandsontable.js',
            'src/main/webapp/bower_components/angular-selectize2/dist/angular-selectize.js',
            // endbower
            'src/main/webapp/app/app.module.js',
            'src/main/webapp/app/app.state.js',
            'src/main/webapp/app/app.constants.js',
            'src/main/webapp/app/**/*.+(js|html)',
            'src/test/javascript/spec/helpers/module.js',
            'src/test/javascript/spec/helpers/httpBackend.js',
            'src/test/javascript/**/!(karma.conf).js'
        ],


        // list of files / patterns to exclude
        exclude: [],

        preprocessors: {
            './**/*.js': sourcePreprocessors
        },

        reporters: ['dots', 'junit', 'coverage', 'progress'],

        junitReporter: {
            outputFile: '../target/test-results/karma/TESTS-results.xml'
        },

        coverageReporter: {
            dir: 'target/test-results/coverage',
            reporters: [
                {type: 'lcov', subdir: 'report-lcov'}
            ]
        },

        // web server port
        port: 9876,

        // level of logging
        // possible values: LOG_DISABLE || LOG_ERROR || LOG_WARN || LOG_INFO || LOG_DEBUG
        logLevel: config.LOG_INFO,

        // enable / disable watching file and executing tests whenever any file changes
        autoWatch: false,

        // Start these browsers, currently available:
        // - Chrome
        // - ChromeCanary
        // - Firefox
        // - Opera
        // - Safari (only Mac)
        // - PhantomJS
        // - IE (only Windows)
        browsers: ['PhantomJS'],

        // Continuous Integration mode
        // if true, it capture browsers, run tests and exit
        singleRun: false,

        // to avoid DISCONNECTED messages when connecting to slow virtual machines
        browserDisconnectTimeout: 10000, // default 2000
        browserDisconnectTolerance: 1, // default 0
        browserNoActivityTimeout: 4 * 60 * 1000 //default 10000
    });
};
