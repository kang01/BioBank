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
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
