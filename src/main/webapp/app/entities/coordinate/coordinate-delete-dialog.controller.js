(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CoordinateDeleteController',CoordinateDeleteController);

    CoordinateDeleteController.$inject = ['$uibModalInstance', 'entity', 'Coordinate'];

    function CoordinateDeleteController($uibModalInstance, entity, Coordinate) {
        var vm = this;

        vm.coordinate = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Coordinate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
