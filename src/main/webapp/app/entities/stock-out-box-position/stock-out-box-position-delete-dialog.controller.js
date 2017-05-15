(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutBoxPositionDeleteController',StockOutBoxPositionDeleteController);

    StockOutBoxPositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutBoxPosition'];

    function StockOutBoxPositionDeleteController($uibModalInstance, entity, StockOutBoxPosition) {
        var vm = this;

        vm.stockOutBoxPosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutBoxPosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
