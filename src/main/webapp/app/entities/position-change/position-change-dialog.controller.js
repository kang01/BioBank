(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionChangeDialogController', PositionChangeDialogController);

    PositionChangeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PositionChange'];

    function PositionChangeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PositionChange) {
        var vm = this;

        vm.positionChange = entity;
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
            if (vm.positionChange.id !== null) {
                PositionChange.update(vm.positionChange, onSaveSuccess, onSaveError);
            } else {
                PositionChange.save(vm.positionChange, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:positionChangeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
