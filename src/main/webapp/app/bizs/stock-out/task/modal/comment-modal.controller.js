/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中待出库样本或已出库样本批注
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskCommentModalController', TaskCommentModalController);

    TaskCommentModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function TaskCommentModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        //1：未出库样本批注、2：已出库样本批注
        vm.status = items.status;
        vm.memo = items.memo;

        vm.ok = function () {
            $uibModalInstance.close(vm.memo);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
