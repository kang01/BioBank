(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CheckTypeDeleteController',CheckTypeDeleteController);

    CheckTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CheckType'];

    function CheckTypeDeleteController($uibModalInstance, entity, CheckType) {
        var vm = this;

        vm.checkType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CheckType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
