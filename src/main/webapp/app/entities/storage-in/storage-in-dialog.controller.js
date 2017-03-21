(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StorageInDialogController', StorageInDialogController);

    StorageInDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StorageIn', 'Tranship', 'Project', 'ProjectSite'];

    function StorageInDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StorageIn, Tranship, Project, ProjectSite) {
        var vm = this;

        vm.storageIn = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.tranships = Tranship.query();
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
            if (vm.storageIn.id !== null) {
                StorageIn.update(vm.storageIn, onSaveSuccess, onSaveError);
            } else {
                StorageIn.save(vm.storageIn, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:storageInUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.receiveDate = false;
        vm.datePickerOpenStatus.storageInDate = false;
        vm.datePickerOpenStatus.signDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
