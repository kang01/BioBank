(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipBoxPositionDialogController', TranshipBoxPositionDialogController);

    TranshipBoxPositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TranshipBoxPosition', 'Equipment', 'Area', 'SupportRack', 'TranshipBox'];

    function TranshipBoxPositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TranshipBoxPosition, Equipment, Area, SupportRack, TranshipBox) {
        var vm = this;

        vm.transhipBoxPosition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();
        vm.transhipboxes = TranshipBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transhipBoxPosition.id !== null) {
                TranshipBoxPosition.update(vm.transhipBoxPosition, onSaveSuccess, onSaveError);
            } else {
                TranshipBoxPosition.save(vm.transhipBoxPosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:transhipBoxPositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
