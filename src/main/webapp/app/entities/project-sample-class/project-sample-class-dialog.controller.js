(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectSampleClassDialogController', ProjectSampleClassDialogController);

    ProjectSampleClassDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectSampleClass', 'Project', 'SampleType', 'SampleClassification'];

    function ProjectSampleClassDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectSampleClass, Project, SampleType, SampleClassification) {
        var vm = this;

        vm.projectSampleClass = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();
        vm.sampletypes = SampleType.query();
        vm.sampleclassifications = SampleClassification.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectSampleClass.id !== null) {
                ProjectSampleClass.update(vm.projectSampleClass, onSaveSuccess, onSaveError);
            } else {
                ProjectSampleClass.save(vm.projectSampleClass, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:projectSampleClassUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
