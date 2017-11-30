(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CheckTypeDialogController', CheckTypeDialogController);

    CheckTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CheckType'];

    function CheckTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CheckType) {
        var vm = this;

        vm.checkType = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.checkType.id !== null) {
                CheckType.update(vm.checkType, onSaveSuccess, onSaveError);
            } else {
                CheckType.save(vm.checkType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:checkTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
