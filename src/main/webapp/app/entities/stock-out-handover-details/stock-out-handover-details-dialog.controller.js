(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverDetailsDialogController', StockOutHandoverDetailsDialogController);

    StockOutHandoverDetailsDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutHandoverDetails', 'StockOutHandover', 'StockOutFrozenTube'];

    function StockOutHandoverDetailsDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutHandoverDetails, StockOutHandover, StockOutFrozenTube) {
        var vm = this;

        vm.stockOutHandoverDetails = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockouthandovers = StockOutHandover.query();
        vm.stockoutfrozentubes = StockOutFrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutHandoverDetails.id !== null) {
                StockOutHandoverDetails.update(vm.stockOutHandoverDetails, onSaveSuccess, onSaveError);
            } else {
                StockOutHandoverDetails.save(vm.stockOutHandoverDetails, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutHandoverDetailsUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
