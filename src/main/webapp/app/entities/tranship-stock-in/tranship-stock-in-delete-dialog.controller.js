(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipStockInDeleteController',TranshipStockInDeleteController);

    TranshipStockInDeleteController.$inject = ['$uibModalInstance', 'entity', 'TranshipStockIn'];

    function TranshipStockInDeleteController($uibModalInstance, entity, TranshipStockIn) {
        var vm = this;

        vm.transhipStockIn = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TranshipStockIn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
