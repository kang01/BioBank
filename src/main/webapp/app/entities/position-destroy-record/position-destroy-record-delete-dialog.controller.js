(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionDestroyRecordDeleteController',PositionDestroyRecordDeleteController);

    PositionDestroyRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'PositionDestroyRecord'];

    function PositionDestroyRecordDeleteController($uibModalInstance, entity, PositionDestroyRecord) {
        var vm = this;

        vm.positionDestroyRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            PositionDestroyRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
