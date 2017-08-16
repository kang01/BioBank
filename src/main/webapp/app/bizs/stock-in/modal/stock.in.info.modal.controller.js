/**
 * Created by gaokangkang on 2017/4/11.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInInfoModalController', StockInInfoModalController);

    StockInInfoModalController.$inject = ['$uibModalInstance','$uibModal','toastr','StockInInfoService','SampleUserService','items'];

    function StockInInfoModalController($uibModalInstance,$uibModal,toastr,StockInInfoService,SampleUserService,items) {
        var vm = this;

        vm.stockInInfo = items;
        vm.stockInInfo.stockInDate =  new Date();

        vm.loginName1 = vm.stockInInfo.storeKeeper1;
        vm.loginName2 = vm.stockInInfo.storeKeeper2;
        vm.stockInBox = vm.stockInInfo.stockInBox;

        var putInLen = _.filter(vm.stockInBox,{"status":"2006"}).length;
        //全部已上架
        if(putInLen == vm.stockInBox.length){
            vm.allPutInFlag = true;
        }else{
            vm.allPutInFlag = false;
        }
        //接收人
        vm.loginConfig = {
            valueField:'login',
            labelField:'userName',
            maxItems: 1

        };
        //接收人
        SampleUserService.query({},onReceiverSuccess, onError);
        function onReceiverSuccess(data) {
            vm.loginOptions = data;
        }
        function onError(error) {
            toastr.error(error.message);
        }
        vm.yes = function () {
            vm.allPutInFlag = true;
        };
        vm.no = function () {
            vm.cancel();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            StockInInfoService.complete(vm.stockInInfo.stockInCode, {
                loginName1:vm.loginName1,
                password1:vm.password1,
                loginName2:vm.loginName2,
                password2:vm.password2,
                stockInDate:vm.stockInInfo.stockInDate
            }).success(onSaveSuccess).error(function (data) {
                toastr.error(data.message);
            });
            function onSaveSuccess(data) {
                toastr.success("入库完成成功！");
                $uibModalInstance.close(true);
            }

        };
    }
})();
