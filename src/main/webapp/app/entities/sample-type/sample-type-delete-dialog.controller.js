(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleTypeDeleteController',SampleTypeDeleteController);

    SampleTypeDeleteController.$inject = ['$uibModalInstance', 'entity', 'SampleType'];

    function SampleTypeDeleteController($uibModalInstance, entity, SampleType) {
        var vm = this;

        vm.sampleType = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SampleType.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
