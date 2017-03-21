(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipDialogController', TranshipDialogController);

    TranshipDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Tranship'];

    function TranshipDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Tranship) {
        var vm = this;

        vm.tranship = entity;
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
            if (vm.tranship.id !== null) {
                Tranship.update(vm.tranship, onSaveSuccess, onSaveError);
            } else {
                Tranship.save(vm.tranship, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:transhipUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.transhipDate = false;
        vm.datePickerOpenStatus.receiveDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
