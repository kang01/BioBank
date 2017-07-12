(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionMoveDialogController', PositionMoveDialogController);

    PositionMoveDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'PositionMove'];

    function PositionMoveDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, PositionMove) {
        var vm = this;

        vm.positionMove = entity;
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
            if (vm.positionMove.id !== null) {
                PositionMove.update(vm.positionMove, onSaveSuccess, onSaveError);
            } else {
                PositionMove.save(vm.positionMove, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:positionMoveUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
