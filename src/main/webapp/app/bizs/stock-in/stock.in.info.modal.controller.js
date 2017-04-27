/**
 * Created by gaokangkang on 2017/4/11.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInInfoModalController', StockInInfoModalController);

    StockInInfoModalController.$inject = ['$uibModalInstance','$uibModal','AlertService','StockInInfoService','SampleUserService','items'];

    function StockInInfoModalController($uibModalInstance,$uibModal,AlertService,StockInInfoService,SampleUserService,items) {
        var vm = this;

        vm.stockInInfo = items;
        vm.stockInInfo.stockInDate =  new Date();

        vm.loginName1 = vm.stockInInfo.storeKeeper1;
        vm.loginName2 = vm.stockInInfo.storeKeeper2;
        //接收人
        vm.loginConfig = {
            valueField:'login',
            labelField:'userName',
            maxItems: 1

        };
        SampleUserService.query({},onReceiverSuccess, onError)//接收人
        function onReceiverSuccess(data) {
            vm.loginOptions = data;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
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
            }).then(onSaveSuccess,onError);
            function onSaveSuccess(data) {
                $uibModalInstance.close(true);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        };
    }
})();
