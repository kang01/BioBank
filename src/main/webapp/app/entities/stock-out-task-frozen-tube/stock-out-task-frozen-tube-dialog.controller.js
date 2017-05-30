(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutTaskFrozenTubeDialogController', StockOutTaskFrozenTubeDialogController);

    StockOutTaskFrozenTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutTaskFrozenTube', 'StockOutTask', 'StockOutPlanFrozenTube'];

    function StockOutTaskFrozenTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutTaskFrozenTube, StockOutTask, StockOutPlanFrozenTube) {
        var vm = this;

        vm.stockOutTaskFrozenTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockouttasks = StockOutTask.query();
        vm.stockoutplanfrozentubes = StockOutPlanFrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutTaskFrozenTube.id !== null) {
                StockOutTaskFrozenTube.update(vm.stockOutTaskFrozenTube, onSaveSuccess, onSaveError);
            } else {
                StockOutTaskFrozenTube.save(vm.stockOutTaskFrozenTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutTaskFrozenTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
