/**
 * Created by gaokangkang on 2017/12/29.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('customScrollbar',  customScrollbar);

    customScrollbar.$inject  =  ['$parse','$compile'];

    function  customScrollbar($parse,$compile)  {
        return {
            restrict: 'A',
            replace:true,
            link: function(scope, elm, attr, ngModelCtrl) {
                setTimeout(function () {
                    elm.mCustomScrollbar({
                        scrollButtons: {
                            scrollAmount: 'auto', // scroll amount when button pressed
                            enable: true // enable scrolling buttons by default
                        },
                        scrollInertia: 400, // adjust however you want
                        axis: 'y', // enable 2 axis scrollbars by default,
                        theme: 'minimal-dark',
                        autoHideScrollbar: true
                    });
                },500);
            }
        };
    }
})();
