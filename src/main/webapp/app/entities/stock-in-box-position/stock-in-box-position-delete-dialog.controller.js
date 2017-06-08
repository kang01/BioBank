(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInBoxPositionDeleteController',StockInBoxPositionDeleteController);

    StockInBoxPositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockInBoxPosition'];

    function StockInBoxPositionDeleteController($uibModalInstance, entity, StockInBoxPosition) {
        var vm = this;

        vm.stockInBoxPosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockInBoxPosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
