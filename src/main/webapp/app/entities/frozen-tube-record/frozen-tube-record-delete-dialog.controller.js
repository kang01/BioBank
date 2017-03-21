(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeRecordDeleteController',FrozenTubeRecordDeleteController);

    FrozenTubeRecordDeleteController.$inject = ['$uibModalInstance', 'entity', 'FrozenTubeRecord'];

    function FrozenTubeRecordDeleteController($uibModalInstance, entity, FrozenTubeRecord) {
        var vm = this;

        vm.frozenTubeRecord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FrozenTubeRecord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
