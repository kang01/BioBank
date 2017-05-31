(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandOverDeleteController',StockOutHandOverDeleteController);

    StockOutHandOverDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutHandOver'];

    function StockOutHandOverDeleteController($uibModalInstance, entity, StockOutHandOver) {
        var vm = this;

        vm.stockOutHandOver = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutHandOver.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
