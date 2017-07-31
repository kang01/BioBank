/**
 * Created by gaokangkang on 2017/7/31.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('viewer',  viewer);

    viewer.$inject  =  [];

    function  viewer()  {
        return {
            restrict: 'A',
            replace: false,
            link: function($scope, element, attrs) {

                var viewer = new Viewer(document.getElementById('dowebok'), {
                    url: 'data-original',

                    // build: function() {
                    //     alert('build');
                    // },
                    //
                    // built: function() {
                    //     alert('built');
                    // },
                    //
                    // show: function() {
                    //     alert('show');
                    // },
                    // shown: function() {
                    //     alert('shown');
                    // },
                    //
                    // hide: function() {
                    //     alert('hide');
                    // },
                    //
                    // hidden: function() {
                    //     alert('hidden');
                    // },
                    //
                    // view: function() {
                    //     alert('view');
                    // },
                    //
                    // viewed: function() {
                    //     alert('viewed');
                    // }
                });
            }
        };
    }
})();
