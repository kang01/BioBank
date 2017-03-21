(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentDeleteController',EquipmentDeleteController);

    EquipmentDeleteController.$inject = ['$uibModalInstance', 'entity', 'Equipment'];

    function EquipmentDeleteController($uibModalInstance, entity, Equipment) {
        var vm = this;

        vm.equipment = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Equipment.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
