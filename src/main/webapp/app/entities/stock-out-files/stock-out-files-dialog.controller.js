(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFilesDialogController', StockOutFilesDialogController);

    StockOutFilesDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'DataUtils', 'entity', 'StockOutFiles'];

    function StockOutFilesDialogController ($timeout, $scope, $stateParams, $uibModalInstance, DataUtils, entity, StockOutFiles) {
        var vm = this;

        vm.stockOutFiles = entity;
        vm.clear = clear;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutFiles.id !== null) {
                StockOutFiles.update(vm.stockOutFiles, onSaveSuccess, onSaveError);
            } else {
                StockOutFiles.save(vm.stockOutFiles, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutFilesUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


        vm.setFiles = function ($file, stockOutFiles) {
            if ($file && $file.$error === 'pattern') {
                return;
            }
            if ($file) {
                DataUtils.toBase64($file, function(base64Data) {
                    $scope.$apply(function() {
                        stockOutFiles.files = base64Data;
                        stockOutFiles.filesContentType = $file.type;
                    });
                });
            }
        };

    }
})();
