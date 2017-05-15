(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFilesDeleteController',StockOutFilesDeleteController);

    StockOutFilesDeleteController.$inject = ['$uibModalInstance', 'entity', 'StockOutFiles'];

    function StockOutFilesDeleteController($uibModalInstance, entity, StockOutFiles) {
        var vm = this;

        vm.stockOutFiles = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StockOutFiles.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
