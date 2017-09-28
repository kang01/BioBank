(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverBoxDialogController', StockOutHandoverBoxDialogController);

    StockOutHandoverBoxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutHandoverBox', 'Equipment', 'Area', 'SupportRack', 'StockOutFrozenBox'];

    function StockOutHandoverBoxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutHandoverBox, Equipment, Area, SupportRack, StockOutFrozenBox) {
        var vm = this;

        vm.stockOutHandoverBox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.equipment = Equipment.query();
        vm.areas = Area.query();
        vm.supportracks = SupportRack.query();
        vm.stockoutfrozenboxes = StockOutFrozenBox.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutHandoverBox.id !== null) {
                StockOutHandoverBox.update(vm.stockOutHandoverBox, onSaveSuccess, onSaveError);
            } else {
                StockOutHandoverBox.save(vm.stockOutHandoverBox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutHandoverBoxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
