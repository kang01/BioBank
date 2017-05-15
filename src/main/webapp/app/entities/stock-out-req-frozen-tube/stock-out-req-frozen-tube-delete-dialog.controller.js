(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutReqFrozenTubeDeleteController',StockOutReqFrozenTubeDeleteController);

    StockOutReqFrozenTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutReqFrozenTube'];

    function StockOutReqFrozenTubeDeleteController($uibModalInstance, entity, StockOutReqFrozenTube) {
        var vm = this;

        vm.stockOutReqFrozenTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutReqFrozenTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
