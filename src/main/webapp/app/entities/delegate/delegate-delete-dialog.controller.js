(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DelegateDeleteController',DelegateDeleteController);

    DelegateDeleteController.$inject = ['$uibModalInstance', 'entity', 'Delegate'];

    function DelegateDeleteController($uibModalInstance, entity, Delegate) {
        var vm = this;

        vm.delegate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Delegate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
