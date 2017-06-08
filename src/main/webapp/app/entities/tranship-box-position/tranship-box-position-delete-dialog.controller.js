(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipBoxPositionDeleteController',TranshipBoxPositionDeleteController);

    TranshipBoxPositionDeleteController.$inject = ['$uibModalInstance', 'entity', 'TranshipBoxPosition'];

    function TranshipBoxPositionDeleteController($uibModalInstance, entity, TranshipBoxPosition) {
        var vm = this;

        vm.transhipBoxPosition = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TranshipBoxPosition.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
