(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeDialogController', FrozenTubeDialogController);

    FrozenTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FrozenTube', 'FrozenTubeType', 'SampleType', 'Project', 'FrozenBox'];

    function FrozenTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FrozenTube, FrozenTubeType, SampleType, Project, FrozenBox) {
        var vm = this;

        vm.frozenTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozentubetypes = FrozenTubeType.query();
        vm.sampletypes = SampleType.query();
        vm.projects = Project.query();
        vm.frozenboxes = FrozenBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.frozenTube.id !== null) {
                FrozenTube.update(vm.frozenTube, onSaveSuccess, onSaveError);
            } else {
                FrozenTube.save(vm.frozenTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:frozenTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
