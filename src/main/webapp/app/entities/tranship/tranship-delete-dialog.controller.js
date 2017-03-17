(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipDeleteController',TranshipDeleteController);

    TranshipDeleteController.$inject = ['$uibModalInstance', 'entity', 'Tranship'];

    function TranshipDeleteController($uibModalInstance, entity, Tranship) {
        var vm = this;

        vm.tranship = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Tranship.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
