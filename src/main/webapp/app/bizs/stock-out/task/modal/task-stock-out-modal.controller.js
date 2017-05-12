/**
 * Created by gaokangkang on 2017/5/12.
 * 计划中出库任务列表详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanTaskDescModalController', PlanTaskDescModalController);

    PlanTaskDescModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function PlanTaskDescModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
