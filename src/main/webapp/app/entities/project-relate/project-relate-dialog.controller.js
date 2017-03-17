(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectRelateDialogController', ProjectRelateDialogController);

    ProjectRelateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectRelate', 'Project', 'ProjectSite'];

    function ProjectRelateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectRelate, Project, ProjectSite) {
        var vm = this;

        vm.projectRelate = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projects = Project.query();
        vm.projectsites = ProjectSite.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectRelate.id !== null) {
                ProjectRelate.update(vm.projectRelate, onSaveSuccess, onSaveError);
            } else {
                ProjectRelate.save(vm.projectRelate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:projectRelateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
