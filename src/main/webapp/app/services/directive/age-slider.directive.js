/**
 * Created by gaokangkang on 2017/5/12.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('ageSlider',  ageSlider);

    ageSlider.$inject  =  [];

    function  ageSlider()  {
        var  directive  =  {
            restrict:  'A',
            scope  :  {
                value  :  '=formTo'
            },
            link:  linkFunc
        };

        return  directive;

        function  linkFunc(scope,  element,  attrs)  {
            var  ageValue  =  "30;65";
            if(scope.value){
                ageValue  =  scope.value;
            }
            var  strs  =  ageValue.split(";");
            $(element).ionRangeSlider({
                type:  "double",
                grid:  true,
                min:  1,
                max:  125,
                step:5,
                from:strs[0],
                to:strs[1],
                postfix:  "  岁",
                value:[30,65]
            });
        }
    }
})();
