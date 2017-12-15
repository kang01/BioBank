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
        var paginationIndexArray = [];

        function _fnVerification() {
            //输入有误
            vm.errorInfo = false;
            vm.paginationText = _.replace(vm.paginationText, '，', ',');
            paginationIndexArray = _.split(vm.paginationText, ',');
            var rgExp = /\d{1,3}-\d{1,3}/;
            _.forEach(paginationIndexArray,function (pagination) {
                var index = pagination.indexOf("-");
                //检查输入的内容中是否包含-
                if(index != -1){
                    if(rgExp.test(pagination)){
                        //匹配成功后，判断第一个数要小于第二个数，不然就输入错误
                        var num1 = _.toNumber(pagination.substr(0,index));
                        var num2 = _.toNumber(pagination.substring(index+1));
                        if(num1 > num2){
                            vm.errorInfo = true
                        }
                    }else{
                        vm.errorInfo = true;
                    }
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
            $uibModalInstance.close(paginationIndexArray);

        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
