(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverDetailsDeleteController',StockOutHandoverDetailsDeleteController);

    StockOutHandoverDetailsDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutHandoverDetails'];

    function StockOutHandoverDetailsDeleteController($uibModalInstance, entity, StockOutHandoverDetails) {
        var vm = this;

        vm.stockOutHandoverDetails = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutHandoverDetails.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
