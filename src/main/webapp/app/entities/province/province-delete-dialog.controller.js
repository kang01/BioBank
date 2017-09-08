(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProvinceDeleteController',ProvinceDeleteController);

    ProvinceDeleteController.$inject = ['$uibModalInstance', 'entity', 'Province'];

    function ProvinceDeleteController($uibModalInstance, entity, Province) {
        var vm = this;

        vm.province = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Province.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
