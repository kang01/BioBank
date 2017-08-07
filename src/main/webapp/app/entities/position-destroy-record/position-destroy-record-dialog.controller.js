(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionDestroyRecordDialogController', PositionDestroyRecordDialogController);

    PositionDestroyRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PositionDestroyRecord'];

    function PositionDestroyRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PositionDestroyRecord) {
        var vm = this;

        vm.positionDestroyRecord = entity;
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
            if (vm.positionDestroyRecord.id !== null) {
                PositionDestroyRecord.update(vm.positionDestroyRecord, onSaveSuccess, onSaveError);
            } else {
                PositionDestroyRecord.save(vm.positionDestroyRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:positionDestroyRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
