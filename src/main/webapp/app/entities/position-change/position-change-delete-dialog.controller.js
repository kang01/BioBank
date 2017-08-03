(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionChangeDeleteController',PositionChangeDeleteController);

    PositionChangeDeleteController.$inject = ['$uibModalInstance', 'entity', 'PositionChange'];

    function PositionChangeDeleteController($uibModalInstance, entity, PositionChange) {
        var vm = this;

        vm.positionChange = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PositionChange.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
