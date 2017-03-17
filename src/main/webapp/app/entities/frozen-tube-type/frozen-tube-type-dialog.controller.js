(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeTypeDialogController', FrozenTubeTypeDialogController);

    FrozenTubeTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FrozenTubeType'];

    function FrozenTubeTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FrozenTubeType) {
        var vm = this;

        vm.frozenTubeType = entity;
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
            if (vm.frozenTubeType.id !== null) {
                FrozenTubeType.update(vm.frozenTubeType, onSaveSuccess, onSaveError);
            } else {
                FrozenTubeType.save(vm.frozenTubeType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:frozenTubeTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
