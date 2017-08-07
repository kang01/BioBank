(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionDestroyDialogController', PositionDestroyDialogController);

    PositionDestroyDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PositionDestroy'];

    function PositionDestroyDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PositionDestroy) {
        var vm = this;

        vm.positionDestroy = entity;
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
            if (vm.positionDestroy.id !== null) {
                PositionDestroy.update(vm.positionDestroy, onSaveSuccess, onSaveError);
            } else {
                PositionDestroy.save(vm.positionDestroy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:positionDestroyUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
