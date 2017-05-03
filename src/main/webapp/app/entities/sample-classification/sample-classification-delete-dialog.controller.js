(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleClassificationDeleteController',SampleClassificationDeleteController);

    SampleClassificationDeleteController.$inject = ['$uibModalInstance', 'entity', 'SampleClassification'];

    function SampleClassificationDeleteController($uibModalInstance, entity, SampleClassification) {
        var vm = this;

        vm.sampleClassification = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SampleClassification.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
