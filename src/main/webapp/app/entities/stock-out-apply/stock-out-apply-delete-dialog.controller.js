(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutApplyDeleteController',StockOutApplyDeleteController);

    StockOutApplyDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutApply'];

    function StockOutApplyDeleteController($uibModalInstance, entity, StockOutApply) {
        var vm = this;

        vm.stockOutApply = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutApply.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
