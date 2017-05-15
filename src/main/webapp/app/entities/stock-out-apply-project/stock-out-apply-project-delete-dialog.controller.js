(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutApplyProjectDeleteController',StockOutApplyProjectDeleteController);

    StockOutApplyProjectDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutApplyProject'];

    function StockOutApplyProjectDeleteController($uibModalInstance, entity, StockOutApplyProject) {
        var vm = this;

        vm.stockOutApplyProject = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutApplyProject.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
