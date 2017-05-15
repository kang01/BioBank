(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutPlanDialogController', StockOutPlanDialogController);

    StockOutPlanDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutPlan', 'StockOutApply'];

    function StockOutPlanDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutPlan, StockOutApply) {
        var vm = this;

        vm.stockOutPlan = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutapplies = StockOutApply.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutPlan.id !== null) {
                StockOutPlan.update(vm.stockOutPlan, onSaveSuccess, onSaveError);
            } else {
                StockOutPlan.save(vm.stockOutPlan, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutPlanUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
