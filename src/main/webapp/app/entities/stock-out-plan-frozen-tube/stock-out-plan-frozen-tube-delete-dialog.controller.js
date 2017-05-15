(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutPlanFrozenTubeDeleteController',StockOutPlanFrozenTubeDeleteController);

    StockOutPlanFrozenTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutPlanFrozenTube'];

    function StockOutPlanFrozenTubeDeleteController($uibModalInstance, entity, StockOutPlanFrozenTube) {
        var vm = this;

        vm.stockOutPlanFrozenTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutPlanFrozenTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
