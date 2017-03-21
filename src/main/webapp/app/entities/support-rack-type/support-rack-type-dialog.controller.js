(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SupportRackTypeDialogController', SupportRackTypeDialogController);

    SupportRackTypeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SupportRackType'];

    function SupportRackTypeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SupportRackType) {
        var vm = this;

        vm.supportRackType = entity;
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
            if (vm.supportRackType.id !== null) {
                SupportRackType.update(vm.supportRackType, onSaveSuccess, onSaveError);
            } else {
                SupportRackType.save(vm.supportRackType, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:supportRackTypeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
