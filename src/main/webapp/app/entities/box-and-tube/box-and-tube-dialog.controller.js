(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxAndTubeDialogController', BoxAndTubeDialogController);

    BoxAndTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'BoxAndTube', 'FrozenBox', 'FrozenTube'];

    function BoxAndTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, BoxAndTube, FrozenBox, FrozenTube) {
        var vm = this;

        vm.boxAndTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozenboxes = FrozenBox.query();
        vm.frozentubes = FrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.boxAndTube.id !== null) {
                BoxAndTube.update(vm.boxAndTube, onSaveSuccess, onSaveError);
            } else {
                BoxAndTube.save(vm.boxAndTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:boxAndTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
