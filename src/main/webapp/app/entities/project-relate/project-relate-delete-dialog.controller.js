(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectRelateDeleteController',ProjectRelateDeleteController);

    ProjectRelateDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectRelate'];

    function ProjectRelateDeleteController($uibModalInstance, entity, ProjectRelate) {
        var vm = this;

        vm.projectRelate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectRelate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
