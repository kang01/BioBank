(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInBoxPositionDialogController', StockInBoxPositionDialogController);

    StockInBoxPositionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockInBoxPosition', 'Equipment', 'Area', 'SupportRack', 'StockInBox'];

    function StockInBoxPositionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockInBoxPosition, Equipment, Area, SupportRack, StockInBox) {
        var vm = this;

        vm.stockInBoxPosition = entity;
        vm.clear = clear;
        vm.save = save;
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();
        vm.stockinboxes = StockInBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockInBoxPosition.id !== null) {
                StockInBoxPosition.update(vm.stockInBoxPosition, onSaveSuccess, onSaveError);
            } else {
                StockInBoxPosition.save(vm.stockInBoxPosition, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockInBoxPositionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
