(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutTaskDeleteController',StockOutTaskDeleteController);

    StockOutTaskDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutTask'];

    function StockOutTaskDeleteController($uibModalInstance, entity, StockOutTask) {
        var vm = this;

        vm.stockOutTask = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutTask.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
