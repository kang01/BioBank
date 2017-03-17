(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SupportRackDialogController', SupportRackDialogController);

    SupportRackDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SupportRack', 'SupportRackType', 'Area'];

    function SupportRackDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SupportRack, SupportRackType, Area) {
        var vm = this;

        vm.supportRack = entity;
        vm.clear = clear;
        vm.save = save;
        vm.supportracktypes = SupportRackType.query();
        vm.areas = Area.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.supportRack.id !== null) {
                SupportRack.update(vm.supportRack, onSaveSuccess, onSaveError);
            } else {
                SupportRack.save(vm.supportRack, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:supportRackUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
