(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFrozenTubeDialogController', StockOutFrozenTubeDialogController);

    StockOutFrozenTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutFrozenTube', 'StockOutFrozenBox', 'FrozenTube'];

    function StockOutFrozenTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutFrozenTube, StockOutFrozenBox, FrozenTube) {
        var vm = this;

        vm.stockOutFrozenTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutfrozenboxes = StockOutFrozenBox.query();
        vm.frozentubes = FrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutFrozenTube.id !== null) {
                StockOutFrozenTube.update(vm.stockOutFrozenTube, onSaveSuccess, onSaveError);
            } else {
                StockOutFrozenTube.save(vm.stockOutFrozenTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutFrozenTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
