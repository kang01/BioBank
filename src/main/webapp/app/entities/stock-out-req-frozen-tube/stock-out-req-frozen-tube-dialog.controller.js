(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutReqFrozenTubeDialogController', StockOutReqFrozenTubeDialogController);

    StockOutReqFrozenTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutReqFrozenTube', 'FrozenBox', 'FrozenTube', 'StockOutRequirement'];

    function StockOutReqFrozenTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutReqFrozenTube, FrozenBox, FrozenTube, StockOutRequirement) {
        var vm = this;

        vm.stockOutReqFrozenTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.frozenboxes = FrozenBox.query();
        vm.frozentubes = FrozenTube.query();
        vm.stockoutrequirements = StockOutRequirement.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutReqFrozenTube.id !== null) {
                StockOutReqFrozenTube.update(vm.stockOutReqFrozenTube, onSaveSuccess, onSaveError);
            } else {
                StockOutReqFrozenTube.save(vm.stockOutReqFrozenTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutReqFrozenTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
