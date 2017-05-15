(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutPlanDeleteController',StockOutPlanDeleteController);

    StockOutPlanDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutPlan'];

    function StockOutPlanDeleteController($uibModalInstance, entity, StockOutPlan) {
        var vm = this;

        vm.stockOutPlan = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutPlan.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
