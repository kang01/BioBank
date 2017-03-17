(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentModleDialogController', EquipmentModleDialogController);

    EquipmentModleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'EquipmentModle'];

    function EquipmentModleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, EquipmentModle) {
        var vm = this;

        vm.equipmentModle = entity;
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
            if (vm.equipmentModle.id !== null) {
                EquipmentModle.update(vm.equipmentModle, onSaveSuccess, onSaveError);
            } else {
                EquipmentModle.save(vm.equipmentModle, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:equipmentModleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
