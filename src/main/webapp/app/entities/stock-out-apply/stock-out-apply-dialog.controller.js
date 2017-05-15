(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutApplyDialogController', StockOutApplyDialogController);

    StockOutApplyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutApply', 'Delegate'];

    function StockOutApplyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutApply, Delegate) {
        var vm = this;

        vm.stockOutApply = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.delegates = Delegate.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutApply.id !== null) {
                StockOutApply.update(vm.stockOutApply, onSaveSuccess, onSaveError);
            } else {
                StockOutApply.save(vm.stockOutApply, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutApplyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.startTime = false;
        vm.datePickerOpenStatus.endTime = false;
        vm.datePickerOpenStatus.recordTime = false;
        vm.datePickerOpenStatus.approveTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
