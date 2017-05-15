(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFrozenTubeDeleteController',StockOutFrozenTubeDeleteController);

    StockOutFrozenTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutFrozenTube'];

    function StockOutFrozenTubeDeleteController($uibModalInstance, entity, StockOutFrozenTube) {
        var vm = this;

        vm.stockOutFrozenTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutFrozenTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
