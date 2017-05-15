(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('UserLoginHistoryDialogController', UserLoginHistoryDialogController);

    UserLoginHistoryDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'UserLoginHistory'];

    function UserLoginHistoryDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, UserLoginHistory) {
        var vm = this;

        vm.userLoginHistory = entity;
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
            if (vm.userLoginHistory.id !== null) {
                UserLoginHistory.update(vm.userLoginHistory, onSaveSuccess, onSaveError);
            } else {
                UserLoginHistory.save(vm.userLoginHistory, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:userLoginHistoryUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.invalidDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
