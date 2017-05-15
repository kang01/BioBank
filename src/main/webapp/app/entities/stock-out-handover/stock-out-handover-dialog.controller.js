(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverDialogController', StockOutHandoverDialogController);

    StockOutHandoverDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutHandover', 'StockOutTask'];

    function StockOutHandoverDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutHandover, StockOutTask) {
        var vm = this;

        vm.stockOutHandover = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.stockouttasks = StockOutTask.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutHandover.id !== null) {
                StockOutHandover.update(vm.stockOutHandover, onSaveSuccess, onSaveError);
            } else {
                StockOutHandover.save(vm.stockOutHandover, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutHandoverUpdate', result);
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
