/**
 * Created by gengluying on 2017/11/29.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanTaskSelectModalController', PlanTaskSelectModalController);

    PlanTaskSelectModalController.$inject = ['$uibModalInstance','$uibModal','items','toastr','DTOptionsBuilder','PlanService','BioBankDataTable'];

    function PlanTaskSelectModalController($uibModalInstance,$uibModal,items,toastr,DTOptionsBuilder,PlanService,BioBankDataTable) {
        var vm = this;
        var indexArray = [];
        var _indexs = [];

        function _fnVerification() {
            //输入有误
            vm.errorInfo = false;
            vm.indexText = _.replace(vm.indexText, '，', ',');
            indexArray = _.split(vm.indexText, ',');
            var rgExp = /\d{1,3}-\d{1,3}/;
            _.forEach(indexArray,function (indexStr) {
                var index = indexStr.indexOf("-");
                //检查输入的内容中是否包含-
                if(index != -1){
                    if(rgExp.test(indexStr)){
                        //匹配成功后，判断第一个数要小于第二个数，不然就输入错误
                        var num1 = _.toNumber(indexStr.substr(0,index));
                        var num2 = _.toNumber(indexStr.substring(index+1));


                        if(num1 > num2){
                            vm.errorInfo = true
                        }else{
                            for(var i = num1; i<= num2;i++){
                                _indexs.push(i)
                            }

                        }
                    }else{
                        vm.errorInfo = true;
                    }
                }else{
                    _indexs.push(Number(indexStr));
                }
            });
        }



        vm.ok = function () {
            //验证
            _fnVerification();
            if(vm.errorInfo){
                toastr.error("输入有误!");
                return;
            }
            _indexs = _.union(_indexs);
            $uibModalInstance.close(_indexs);

        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
