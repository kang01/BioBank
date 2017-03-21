(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxTypeDialogController', FrozenBoxTypeDialogController);

    FrozenBoxTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FrozenBoxType'];

    function FrozenBoxTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FrozenBoxType) {
        var vm = this;

        vm.frozenBoxType = entity;
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
            if (vm.frozenBoxType.id !== null) {
                FrozenBoxType.update(vm.frozenBoxType, onSaveSuccess, onSaveError);
            } else {
                FrozenBoxType.save(vm.frozenBoxType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:frozenBoxTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
