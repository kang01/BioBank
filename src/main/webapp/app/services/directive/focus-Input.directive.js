/**
 * Created by gaokangkang on 2017/5/17.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('focusMe',  focusMe);

    focusMe.$inject  =  [];

    function  focusMe()  {
        return {
            scope: { trigger: '=focusMe' },
            link: function(scope, element) {
                scope.$watch('trigger', function(value) {
                    if(value === true) {
                        element[0].focus();
                        scope.trigger = false;
                    }
                });
            }
        };
    }
})();
