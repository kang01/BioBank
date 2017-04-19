(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxPositionDialogController', FrozenBoxPositionDialogController);

    FrozenBoxPositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FrozenBoxPosition', 'Equipment', 'Area', 'SupportRack', 'FrozenBox'];

    function FrozenBoxPositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FrozenBoxPosition, Equipment, Area, SupportRack, FrozenBox) {
        var vm = this;

        vm.frozenBoxPosition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();
        vm.frozenboxes = FrozenBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.frozenBoxPosition.id !== null) {
                FrozenBoxPosition.update(vm.frozenBoxPosition, onSaveSuccess, onSaveError);
            } else {
                FrozenBoxPosition.save(vm.frozenBoxPosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:frozenBoxPositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
