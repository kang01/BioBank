(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutBoxPositionDialogController', StockOutBoxPositionDialogController);

    StockOutBoxPositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutBoxPosition', 'Equipment', 'Area', 'SupportRack', 'FrozenBox'];

    function StockOutBoxPositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutBoxPosition, Equipment, Area, SupportRack, FrozenBox) {
        var vm = this;

        vm.stockOutBoxPosition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();
        vm.frozenboxes = FrozenBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutBoxPosition.id !== null) {
                StockOutBoxPosition.update(vm.stockOutBoxPosition, onSaveSuccess, onSaveError);
            } else {
                StockOutBoxPosition.save(vm.stockOutBoxPosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutBoxPositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
