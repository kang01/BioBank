(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverDeleteController',StockOutHandoverDeleteController);

    StockOutHandoverDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutHandover'];

    function StockOutHandoverDeleteController($uibModalInstance, entity, StockOutHandover) {
        var vm = this;

        vm.stockOutHandover = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutHandover.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
