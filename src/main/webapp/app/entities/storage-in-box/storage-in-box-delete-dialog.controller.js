(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StorageInBoxDeleteController',StorageInBoxDeleteController);

    StorageInBoxDeleteController.$inject = ['$uibModalInstance', 'entity', 'StorageInBox'];

    function StorageInBoxDeleteController($uibModalInstance, entity, StorageInBox) {
        var vm = this;

        vm.storageInBox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            StorageInBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
