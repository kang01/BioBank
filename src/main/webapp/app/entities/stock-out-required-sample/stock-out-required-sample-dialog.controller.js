(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutRequiredSampleDialogController', StockOutRequiredSampleDialogController);

    StockOutRequiredSampleDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutRequiredSample', 'StockOutRequirement'];

    function StockOutRequiredSampleDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutRequiredSample, StockOutRequirement) {
        var vm = this;

        vm.stockOutRequiredSample = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutrequirements = StockOutRequirement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutRequiredSample.id !== null) {
                StockOutRequiredSample.update(vm.stockOutRequiredSample, onSaveSuccess, onSaveError);
            } else {
                StockOutRequiredSample.save(vm.stockOutRequiredSample, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutRequiredSampleUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
