(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StorageInBoxDialogController', StorageInBoxDialogController);

    StorageInBoxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StorageInBox', 'StorageIn'];

    function StorageInBoxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StorageInBox, StorageIn) {
        var vm = this;

        vm.storageInBox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.storageins = StorageIn.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.storageInBox.id !== null) {
                StorageInBox.update(vm.storageInBox, onSaveSuccess, onSaveError);
            } else {
                StorageInBox.save(vm.storageInBox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:storageInBoxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
