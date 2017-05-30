(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutTaskFrozenTubeDeleteController',StockOutTaskFrozenTubeDeleteController);

    StockOutTaskFrozenTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutTaskFrozenTube'];

    function StockOutTaskFrozenTubeDeleteController($uibModalInstance, entity, StockOutTaskFrozenTube) {
        var vm = this;

        vm.stockOutTaskFrozenTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutTaskFrozenTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
