(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleTypeDialogController', SampleTypeDialogController);

    SampleTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SampleType'];

    function SampleTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SampleType) {
        var vm = this;

        vm.sampleType = entity;
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
            if (vm.sampleType.id !== null) {
                SampleType.update(vm.sampleType, onSaveSuccess, onSaveError);
            } else {
                SampleType.save(vm.sampleType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:sampleTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
