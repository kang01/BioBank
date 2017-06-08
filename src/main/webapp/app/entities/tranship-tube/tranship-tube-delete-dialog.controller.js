(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipTubeDeleteController',TranshipTubeDeleteController);

    TranshipTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'TranshipTube'];

    function TranshipTubeDeleteController($uibModalInstance, entity, TranshipTube) {
        var vm = this;

        vm.transhipTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            TranshipTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
