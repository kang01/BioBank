(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutTaskDialogController', StockOutTaskDialogController);

    StockOutTaskDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutTask', 'StockOutPlan'];

    function StockOutTaskDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutTask, StockOutPlan) {
        var vm = this;

        vm.stockOutTask = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.stockoutplans = StockOutPlan.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutTask.id !== null) {
                StockOutTask.update(vm.stockOutTask, onSaveSuccess, onSaveError);
            } else {
                StockOutTask.save(vm.stockOutTask, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutTaskUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.stockOutDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
