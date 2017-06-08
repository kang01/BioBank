(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTubeDeleteController',StockInTubeDeleteController);

    StockInTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockInTube'];

    function StockInTubeDeleteController($uibModalInstance, entity, StockInTube) {
        var vm = this;

        vm.stockInTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockInTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
