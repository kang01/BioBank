(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutRequiredSampleDeleteController',StockOutRequiredSampleDeleteController);

    StockOutRequiredSampleDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutRequiredSample'];

    function StockOutRequiredSampleDeleteController($uibModalInstance, entity, StockOutRequiredSample) {
        var vm = this;

        vm.stockOutRequiredSample = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutRequiredSample.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
