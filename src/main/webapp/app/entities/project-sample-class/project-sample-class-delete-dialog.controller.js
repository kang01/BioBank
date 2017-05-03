(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectSampleClassDeleteController',ProjectSampleClassDeleteController);

    ProjectSampleClassDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectSampleClass'];

    function ProjectSampleClassDeleteController($uibModalInstance, entity, ProjectSampleClass) {
        var vm = this;

        vm.projectSampleClass = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectSampleClass.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
