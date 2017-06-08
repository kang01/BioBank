(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTubeDialogController', StockInTubeDialogController);

    StockInTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockInTube', 'StockInBox', 'FrozenTube'];

    function StockInTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockInTube, StockInBox, FrozenTube) {
        var vm = this;

        vm.stockInTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockinboxes = StockInBox.query();
        vm.frozentubes = FrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockInTube.id !== null) {
                StockInTube.update(vm.stockInTube, onSaveSuccess, onSaveError);
            } else {
                StockInTube.save(vm.stockInTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockInTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
