(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipBoxDeleteController',TranshipBoxDeleteController);

    TranshipBoxDeleteController.$inject = ['$uibModalInstance', 'entity', 'TranshipBox'];

    function TranshipBoxDeleteController($uibModalInstance, entity, TranshipBox) {
        var vm = this;

        vm.transhipBox = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TranshipBox.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
