(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeRecordDialogController', FrozenTubeRecordDialogController);

    FrozenTubeRecordDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FrozenTubeRecord', 'SampleType', 'FrozenTubeType'];

    function FrozenTubeRecordDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FrozenTubeRecord, SampleType, FrozenTubeType) {
        var vm = this;

        vm.frozenTubeRecord = entity;
        vm.clear = clear;
        vm.save = save;
        vm.sampletypes = SampleType.query();
        vm.frozentubetypes = FrozenTubeType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.frozenTubeRecord.id !== null) {
                FrozenTubeRecord.update(vm.frozenTubeRecord, onSaveSuccess, onSaveError);
            } else {
                FrozenTubeRecord.save(vm.frozenTubeRecord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:frozenTubeRecordUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
