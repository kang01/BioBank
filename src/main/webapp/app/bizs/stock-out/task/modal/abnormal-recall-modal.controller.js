/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中待出库样本的异常和撤销申请操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AbnormalRecallModalController', AbnormalRecallModalController);

    AbnormalRecallModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function AbnormalRecallModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        //1:异常 2:撤销申请
        vm.status = items.status;

        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
