(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleClassificationDialogController', SampleClassificationDialogController);

    SampleClassificationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SampleClassification'];

    function SampleClassificationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SampleClassification) {
        var vm = this;

        vm.sampleClassification = entity;
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
            if (vm.sampleClassification.id !== null) {
                SampleClassification.update(vm.sampleClassification, onSaveSuccess, onSaveError);
            } else {
                SampleClassification.save(vm.sampleClassification, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:sampleClassificationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
