(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTubesDeleteController',StockInTubesDeleteController);

    StockInTubesDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockInTubes'];

    function StockInTubesDeleteController($uibModalInstance, entity, StockInTubes) {
        var vm = this;

        vm.stockInTubes = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockInTubes.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
