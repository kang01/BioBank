(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SupportRackDeleteController',SupportRackDeleteController);

    SupportRackDeleteController.$inject = ['$uibModalInstance', 'entity', 'SupportRack'];

    function SupportRackDeleteController($uibModalInstance, entity, SupportRack) {
        var vm = this;

        vm.supportRack = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SupportRack.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
