(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTranshipBoxDeleteController',StockInTranshipBoxDeleteController);

    StockInTranshipBoxDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockInTranshipBox'];

    function StockInTranshipBoxDeleteController($uibModalInstance, entity, StockInTranshipBox) {
        var vm = this;

        vm.stockInTranshipBox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockInTranshipBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
