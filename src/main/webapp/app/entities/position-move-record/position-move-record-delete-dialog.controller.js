(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionMoveRecordDeleteController',PositionMoveRecordDeleteController);

    PositionMoveRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'PositionMoveRecord'];

    function PositionMoveRecordDeleteController($uibModalInstance, entity, PositionMoveRecord) {
        var vm = this;

        vm.positionMoveRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PositionMoveRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
