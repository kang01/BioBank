(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RelationsDeleteController',RelationsDeleteController);

    RelationsDeleteController.$inject = ['$uibModalInstance', 'entity', 'Relations'];

    function RelationsDeleteController($uibModalInstance, entity, Relations) {
        var vm = this;

        vm.relations = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Relations.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
