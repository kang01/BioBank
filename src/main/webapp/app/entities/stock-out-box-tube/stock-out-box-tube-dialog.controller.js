(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutBoxTubeDialogController', StockOutBoxTubeDialogController);

    StockOutBoxTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutBoxTube', 'StockOutFrozenBox', 'FrozenTube', 'StockOutTaskFrozenTube'];

    function StockOutBoxTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutBoxTube, StockOutFrozenBox, FrozenTube, StockOutTaskFrozenTube) {
        var vm = this;

        vm.stockOutBoxTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutfrozenboxes = StockOutFrozenBox.query();
        vm.frozentubes = FrozenTube.query();
        vm.stockouttaskfrozentubes = StockOutTaskFrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutBoxTube.id !== null) {
                StockOutBoxTube.update(vm.stockOutBoxTube, onSaveSuccess, onSaveError);
            } else {
                StockOutBoxTube.save(vm.stockOutBoxTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutBoxTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
