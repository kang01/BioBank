(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutBoxTubeDeleteController',StockOutBoxTubeDeleteController);

    StockOutBoxTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutBoxTube'];

    function StockOutBoxTubeDeleteController($uibModalInstance, entity, StockOutBoxTube) {
        var vm = this;

        vm.stockOutBoxTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutBoxTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
