(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFrozenBoxDialogController', StockOutFrozenBoxDialogController);

    StockOutFrozenBoxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutFrozenBox', 'FrozenBox', 'StockOutBoxPosition', 'StockOutTask'];

    function StockOutFrozenBoxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutFrozenBox, FrozenBox, StockOutBoxPosition, StockOutTask) {
        var vm = this;

        vm.stockOutFrozenBox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozenboxes = FrozenBox.query();
        vm.stockoutboxpositions = StockOutBoxPosition.query();
        vm.stockouttasks = StockOutTask.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutFrozenBox.id !== null) {
                StockOutFrozenBox.update(vm.stockOutFrozenBox, onSaveSuccess, onSaveError);
            } else {
                StockOutFrozenBox.save(vm.stockOutFrozenBox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutFrozenBoxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
