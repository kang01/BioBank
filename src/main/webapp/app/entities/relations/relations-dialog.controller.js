(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RelationsDialogController', RelationsDialogController);

    RelationsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Relations', 'FrozenBoxType', 'FrozenTubeType', 'SampleType', 'Project'];

    function RelationsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Relations, FrozenBoxType, FrozenTubeType, SampleType, Project) {
        var vm = this;

        vm.relations = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozenboxtypes = FrozenBoxType.query();
        vm.frozentubetypes = FrozenTubeType.query();
        vm.sampletypes = SampleType.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.relations.id !== null) {
                Relations.update(vm.relations, onSaveSuccess, onSaveError);
            } else {
                Relations.save(vm.relations, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:relationsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
