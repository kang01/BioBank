(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeDeleteController',FrozenTubeDeleteController);

    FrozenTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'FrozenTube'];

    function FrozenTubeDeleteController($uibModalInstance, entity, FrozenTube) {
        var vm = this;

        vm.frozenTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FrozenTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
