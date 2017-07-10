(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SerialNoDialogController', SerialNoDialogController);

    SerialNoDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SerialNo'];

    function SerialNoDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SerialNo) {
        var vm = this;

        vm.serialNo = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serialNo.id !== null) {
                SerialNo.update(vm.serialNo, onSaveSuccess, onSaveError);
            } else {
                SerialNo.save(vm.serialNo, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:serialNoUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.usedDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
