(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectSiteDialogController', ProjectSiteDialogController);

    ProjectSiteDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProjectSite', 'ProjectRelate'];

    function ProjectSiteDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProjectSite, ProjectRelate) {
        var vm = this;

        vm.projectSite = entity;
        vm.clear = clear;
        vm.save = save;
        vm.projectrelates = ProjectRelate.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.projectSite.id !== null) {
                ProjectSite.update(vm.projectSite, onSaveSuccess, onSaveError);
            } else {
                ProjectSite.save(vm.projectSite, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:projectSiteUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
