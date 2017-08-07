(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipStockInDialogController', TranshipStockInDialogController);

    TranshipStockInDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TranshipStockIn', 'Tranship', 'StockIn'];

    function TranshipStockInDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TranshipStockIn, Tranship, StockIn) {
        var vm = this;

        vm.transhipStockIn = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tranships = Tranship.query();
        vm.stockins = StockIn.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transhipStockIn.id !== null) {
                TranshipStockIn.update(vm.transhipStockIn, onSaveSuccess, onSaveError);
            } else {
                TranshipStockIn.save(vm.transhipStockIn, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:transhipStockInUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
