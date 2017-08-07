(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionChangeRecordDialogController', PositionChangeRecordDialogController);

    PositionChangeRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PositionChangeRecord', 'PositionChange'];

    function PositionChangeRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PositionChangeRecord, PositionChange) {
        var vm = this;

        vm.positionChangeRecord = entity;
        vm.clear = clear;
        vm.save = save;
        vm.positionchanges = PositionChange.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.positionChangeRecord.id !== null) {
                PositionChangeRecord.update(vm.positionChangeRecord, onSaveSuccess, onSaveError);
            } else {
                PositionChangeRecord.save(vm.positionChangeRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:positionChangeRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
