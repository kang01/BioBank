(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutRequirementDialogController', StockOutRequirementDialogController);

    StockOutRequirementDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutRequirement', 'StockOutApply', 'SampleType', 'SampleClassification', 'FrozenTubeType'];

    function StockOutRequirementDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutRequirement, StockOutApply, SampleType, SampleClassification, FrozenTubeType) {
        var vm = this;

        vm.stockOutRequirement = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutapplies = StockOutApply.query();
        vm.sampletypes = SampleType.query();
        vm.sampleclassifications = SampleClassification.query();
        vm.frozentubetypes = FrozenTubeType.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutRequirement.id !== null) {
                StockOutRequirement.update(vm.stockOutRequirement, onSaveSuccess, onSaveError);
            } else {
                StockOutRequirement.save(vm.stockOutRequirement, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutRequirementUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
