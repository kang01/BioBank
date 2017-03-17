(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectSiteDeleteController',ProjectSiteDeleteController);

    ProjectSiteDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProjectSite'];

    function ProjectSiteDeleteController($uibModalInstance, entity, ProjectSite) {
        var vm = this;

        vm.projectSite = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProjectSite.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
