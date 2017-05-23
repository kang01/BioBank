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
            var isChanging = false;
            $(element).ionRangeSlider({
                type:  "double",
                grid:  true,
                min:  0,
                max:  125,
                step:5,
                from:strs[0],
                to:strs[1],
                postfix:  "  岁",
                value:[30,65],
                onChange: function (data) {
                    isChanging = true;
                },
                onFinish: function (data) {
                    isChanging = false;
                    scope.value = [data.from, data.to].join(";");
                    scope.$apply();
                },
            });
            var slider = $(element).data("ionRangeSlider");
            scope.$watch("value", function(newValue, oldValue){
                if (isChanging){
                    return;
                }
                var  strs  =  (newValue||"30;65").split(";");
                slider.update({
                    from: strs[0],
                    to: strs[1]
                });
            });
        }
    }
})();
