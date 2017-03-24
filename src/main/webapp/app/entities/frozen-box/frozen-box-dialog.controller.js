(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxDialogController', FrozenBoxDialogController);

    FrozenBoxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'FrozenBox', 'FrozenBoxType', 'SampleType', 'Project', 'ProjectSite', 'Tranship', 'Equipment', 'Area', 'SupportRack'];

    function FrozenBoxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, FrozenBox, FrozenBoxType, SampleType, Project, ProjectSite, Tranship, Equipment, Area, SupportRack) {
        var vm = this;

        vm.frozenBox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozenboxtypes = FrozenBoxType.query();
        vm.sampletypes = SampleType.query();
        vm.projects = Project.query();
        vm.projectsites = ProjectSite.query();
        vm.tranships = Tranship.query();
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.frozenBox.id !== null) {
                FrozenBox.update(vm.frozenBox, onSaveSuccess, onSaveError);
            } else {
                FrozenBox.save(vm.frozenBox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:frozenBoxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
