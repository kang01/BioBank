(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverBoxDeleteController',StockOutHandoverBoxDeleteController);

    StockOutHandoverBoxDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutHandoverBox'];

    function StockOutHandoverBoxDeleteController($uibModalInstance, entity, StockOutHandoverBox) {
        var vm = this;

        vm.stockOutHandoverBox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutHandoverBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
