(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxDeleteController',FrozenBoxDeleteController);

    FrozenBoxDeleteController.$inject = ['$uibModalInstance', 'entity', 'FrozenBox'];

    function FrozenBoxDeleteController($uibModalInstance, entity, FrozenBox) {
        var vm = this;

        vm.frozenBox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FrozenBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
