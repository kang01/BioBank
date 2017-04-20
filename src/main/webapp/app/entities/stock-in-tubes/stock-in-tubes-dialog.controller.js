(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTubesDialogController', StockInTubesDialogController);

    StockInTubesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockInTubes', 'FrozenTube', 'FrozenBoxPosition', 'TranshipBox', 'StockInBox'];

    function StockInTubesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockInTubes, FrozenTube, FrozenBoxPosition, TranshipBox, StockInBox) {
        var vm = this;

        vm.stockInTubes = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozentubes = FrozenTube.query();
        vm.frozenboxpositions = FrozenBoxPosition.query();
        vm.transhipboxes = TranshipBox.query();
        vm.stockinboxes = StockInBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockInTubes.id !== null) {
                StockInTubes.update(vm.stockInTubes, onSaveSuccess, onSaveError);
            } else {
                StockInTubes.save(vm.stockInTubes, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockInTubesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
