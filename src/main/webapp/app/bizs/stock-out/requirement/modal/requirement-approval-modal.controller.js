/**
 * Created by gaokangkang on 2017/5/12.
 * 申请样本出库批准
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementApprovalModalController', RequirementApprovalModalController);

    RequirementApprovalModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function RequirementApprovalModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
