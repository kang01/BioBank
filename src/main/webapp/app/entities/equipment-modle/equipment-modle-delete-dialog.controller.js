(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentModleDeleteController',EquipmentModleDeleteController);

    EquipmentModleDeleteController.$inject = ['$uibModalInstance', 'entity', 'EquipmentModle'];

    function EquipmentModleDeleteController($uibModalInstance, entity, EquipmentModle) {
        var vm = this;

        vm.equipmentModle = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            EquipmentModle.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
