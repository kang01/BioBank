(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutRequirementDeleteController',StockOutRequirementDeleteController);

    StockOutRequirementDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutRequirement'];

    function StockOutRequirementDeleteController($uibModalInstance, entity, StockOutRequirement) {
        var vm = this;

        vm.stockOutRequirement = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutRequirement.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
