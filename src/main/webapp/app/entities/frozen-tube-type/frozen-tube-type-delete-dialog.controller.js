(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeTypeDeleteController',FrozenTubeTypeDeleteController);

    FrozenTubeTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'FrozenTubeType'];

    function FrozenTubeTypeDeleteController($uibModalInstance, entity, FrozenTubeType) {
        var vm = this;

        vm.frozenTubeType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FrozenTubeType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
