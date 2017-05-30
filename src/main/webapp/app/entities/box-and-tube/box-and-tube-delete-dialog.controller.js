(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxAndTubeDeleteController',BoxAndTubeDeleteController);

    BoxAndTubeDeleteController.$inject = ['$uibModalInstance', 'entity', 'BoxAndTube'];

    function BoxAndTubeDeleteController($uibModalInstance, entity, BoxAndTube) {
        var vm = this;

        vm.boxAndTube = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            BoxAndTube.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
