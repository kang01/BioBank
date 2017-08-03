(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionChangeRecordDeleteController',PositionChangeRecordDeleteController);

    PositionChangeRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'PositionChangeRecord'];

    function PositionChangeRecordDeleteController($uibModalInstance, entity, PositionChangeRecord) {
        var vm = this;

        vm.positionChangeRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PositionChangeRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
