/**
 * Created by gengluying on 2017/11/29.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanTaskSelectModalController', PlanTaskSelectModalController);

    PlanTaskSelectModalController.$inject = ['$uibModalInstance','$uibModal','items','DTColumnBuilder','DTOptionsBuilder','PlanService','BioBankDataTable'];

    function PlanTaskSelectModalController($uibModalInstance,$uibModal,items,DTColumnBuilder,DTOptionsBuilder,PlanService,BioBankDataTable) {
        var vm = this;

        vm.ok = function () {
            $uibModalInstance.close(vm.paginationText);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
