/**
 * Created by gaokangkang on 2017/5/17.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('focusMe',  focusMe);

    focusMe.$inject  =  ['$timeout'];

    function  focusMe($timeout)  {
        // return {
        //     scope: { trigger: '=focusMe' },
        //     link: function(scope, element) {
        //         scope.$watch('trigger', function(value) {
        //             if(value === true) {
        //                 element[0].focus();
        //                 scope.trigger = false;
        //             }
        //         });
        //     }
        // };
        return {
            restrict: 'AC',
            link: function(_scope, _element) {
                $timeout(function(){
                    _element[0].focus();
                }, 0);
            }
        };
    }
})();
