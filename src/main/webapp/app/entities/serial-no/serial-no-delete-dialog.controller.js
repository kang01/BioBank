(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SerialNoDeleteController',SerialNoDeleteController);

    SerialNoDeleteController.$inject = ['$uibModalInstance', 'entity', 'SerialNo'];

    function SerialNoDeleteController($uibModalInstance, entity, SerialNo) {
        var vm = this;

        vm.serialNo = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SerialNo.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
