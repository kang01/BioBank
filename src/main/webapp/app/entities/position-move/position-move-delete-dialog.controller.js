(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionMoveDeleteController',PositionMoveDeleteController);

    PositionMoveDeleteController.$inject = ['$uibModalInstance', 'entity', 'PositionMove'];

    function PositionMoveDeleteController($uibModalInstance, entity, PositionMove) {
        var vm = this;

        vm.positionMove = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PositionMove.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
