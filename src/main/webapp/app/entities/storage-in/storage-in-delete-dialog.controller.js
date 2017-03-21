(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StorageInDeleteController',StorageInDeleteController);

    StorageInDeleteController.$inject = ['$uibModalInstance', 'entity', 'StorageIn'];

    function StorageInDeleteController($uibModalInstance, entity, StorageIn) {
        var vm = this;

        vm.storageIn = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StorageIn.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
