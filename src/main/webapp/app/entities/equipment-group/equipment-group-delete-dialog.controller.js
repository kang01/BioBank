(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentGroupDeleteController',EquipmentGroupDeleteController);

    EquipmentGroupDeleteController.$inject = ['$uibModalInstance', 'entity', 'EquipmentGroup'];

    function EquipmentGroupDeleteController($uibModalInstance, entity, EquipmentGroup) {
        var vm = this;

        vm.equipmentGroup = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EquipmentGroup.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
