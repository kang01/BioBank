(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxTypeDeleteController',FrozenBoxTypeDeleteController);

    FrozenBoxTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'FrozenBoxType'];

    function FrozenBoxTypeDeleteController($uibModalInstance, entity, FrozenBoxType) {
        var vm = this;

        vm.frozenBoxType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            FrozenBoxType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
