(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFrozenBoxDeleteController',StockOutFrozenBoxDeleteController);

    StockOutFrozenBoxDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutFrozenBox'];

    function StockOutFrozenBoxDeleteController($uibModalInstance, entity, StockOutFrozenBox) {
        var vm = this;

        vm.stockOutFrozenBox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutFrozenBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
