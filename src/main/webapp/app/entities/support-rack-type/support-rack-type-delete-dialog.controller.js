(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SupportRackTypeDeleteController',SupportRackTypeDeleteController);

    SupportRackTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'SupportRackType'];

    function SupportRackTypeDeleteController($uibModalInstance, entity, SupportRackType) {
        var vm = this;

        vm.supportRackType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SupportRackType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
