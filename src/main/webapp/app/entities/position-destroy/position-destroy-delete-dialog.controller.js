(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionDestroyDeleteController',PositionDestroyDeleteController);

    PositionDestroyDeleteController.$inject = ['$uibModalInstance', 'entity', 'PositionDestroy'];

    function PositionDestroyDeleteController($uibModalInstance, entity, PositionDestroy) {
        var vm = this;

        vm.positionDestroy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PositionDestroy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
