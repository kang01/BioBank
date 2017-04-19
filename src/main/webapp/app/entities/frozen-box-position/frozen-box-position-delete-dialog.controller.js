(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxPositionDeleteController',FrozenBoxPositionDeleteController);

    FrozenBoxPositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'FrozenBoxPosition'];

    function FrozenBoxPositionDeleteController($uibModalInstance, entity, FrozenBoxPosition) {
        var vm = this;

        vm.frozenBoxPosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FrozenBoxPosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
