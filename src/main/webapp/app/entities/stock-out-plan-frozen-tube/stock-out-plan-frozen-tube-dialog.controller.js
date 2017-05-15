(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutPlanFrozenTubeDialogController', StockOutPlanFrozenTubeDialogController);

    StockOutPlanFrozenTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutPlanFrozenTube', 'StockOutPlan', 'FrozenBox', 'FrozenTube'];

    function StockOutPlanFrozenTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutPlanFrozenTube, StockOutPlan, FrozenBox, FrozenTube) {
        var vm = this;

        vm.stockOutPlanFrozenTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutplans = StockOutPlan.query();
        vm.frozenboxes = FrozenBox.query();
        vm.frozentubes = FrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutPlanFrozenTube.id !== null) {
                StockOutPlanFrozenTube.update(vm.stockOutPlanFrozenTube, onSaveSuccess, onSaveError);
            } else {
                StockOutPlanFrozenTube.save(vm.stockOutPlanFrozenTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutPlanFrozenTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
