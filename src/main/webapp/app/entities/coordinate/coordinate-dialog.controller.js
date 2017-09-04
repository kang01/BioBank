(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CoordinateDialogController', CoordinateDialogController);

    CoordinateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Coordinate'];

    function CoordinateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Coordinate) {
        var vm = this;

        vm.coordinate = entity;
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
            if (vm.coordinate.id !== null) {
                Coordinate.update(vm.coordinate, onSaveSuccess, onSaveError);
            } else {
                Coordinate.save(vm.coordinate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:coordinateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
