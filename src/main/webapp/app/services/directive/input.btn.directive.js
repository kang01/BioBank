/**
 * Created by gaokangkang on 2017/12/25.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('inputBtn',  inputBtn);

    inputBtn.$inject  =  ['$parse','$compile'];

    function  inputBtn($parse,$compile)  {
        return {
            restrict: 'A',
            replace:true,
            link: function(scope, elm, attr, ngModelCtrl) {

                var inputBtnOption = scope.$eval(attr.inputBtnOption);
                if(!inputBtnOption){
                    inputBtnOption = {
                        icon: "fa-close"
                    }
                }
                elm.wrap("<div style='position: relative'></div>");
                var btn = '<a class="ng-hide" ><span id=' + Math.round(Math.random() * 1000000000) + ' class="input-icon  fa '+inputBtnOption.icon+ '"></span></a>';
                var angularBtn = angular.element(btn);
                elm.after(angularBtn);

                angularBtn.on("click", function () {
                    angularBtn.addClass("ng-hide");

                    if(inputBtnOption.icon == "fa-close"){
                        elm.val('').trigger("change");
                        $parse(attr.ngModel).assign(scope, null);
                        scope.$apply();
                    }else{
                        inputBtnOption.makeNewBoxCode();
                    }
                });

                // show  clear btn  on focus
                elm.bind('focus keyup change paste propertychange', function (blurEvent) {

                    if(inputBtnOption.icon == "fa-close"){
                        if (elm.val() && elm.val().length > 0) {
                            angularBtn.removeClass("ng-hide");
                        } else {
                            angularBtn.addClass("ng-hide");
                        }
                    }else{
                        angularBtn.removeClass("ng-hide");
                    }

                });
                // remove  clear btn  on blur
                elm.bind('blur', function (blurEvent) {
                    if (!angularBtn.is(":hover"))
                        angularBtn.addClass("ng-hide");
                });

            }
        };
    }
})();
