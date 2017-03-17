(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentGroupDialogController', EquipmentGroupDialogController);

    EquipmentGroupDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EquipmentGroup'];

    function EquipmentGroupDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EquipmentGroup) {
        var vm = this;

        vm.equipmentGroup = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.equipmentGroup.id !== null) {
                EquipmentGroup.update(vm.equipmentGroup, onSaveSuccess, onSaveError);
            } else {
                EquipmentGroup.save(vm.equipmentGroup, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:equipmentGroupUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
