(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTranshipBoxDialogController', StockInTranshipBoxDialogController);

    StockInTranshipBoxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockInTranshipBox', 'TranshipBox', 'StockIn'];

    function StockInTranshipBoxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockInTranshipBox, TranshipBox, StockIn) {
        var vm = this;

        vm.stockInTranshipBox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.transhipboxes = TranshipBox.query();
        vm.stockins = StockIn.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockInTranshipBox.id !== null) {
                StockInTranshipBox.update(vm.stockInTranshipBox, onSaveSuccess, onSaveError);
            } else {
                StockInTranshipBox.save(vm.stockInTranshipBox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockInTranshipBoxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
