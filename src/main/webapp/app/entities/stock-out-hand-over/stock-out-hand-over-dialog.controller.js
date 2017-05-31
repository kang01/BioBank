(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandOverDialogController', StockOutHandOverDialogController);

    StockOutHandOverDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutHandOver', 'StockOutTask', 'StockOutApply', 'StockOutPlan'];

    function StockOutHandOverDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutHandOver, StockOutTask, StockOutApply, StockOutPlan) {
        var vm = this;

        vm.stockOutHandOver = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.stockouttasks = StockOutTask.query();
        vm.stockoutapplies = StockOutApply.query();
        vm.stockoutplans = StockOutPlan.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutHandOver.id !== null) {
                StockOutHandOver.update(vm.stockOutHandOver, onSaveSuccess, onSaveError);
            } else {
                StockOutHandOver.save(vm.stockOutHandOver, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutHandOverUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.handoverTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
