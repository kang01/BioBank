(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DelegateDialogController', DelegateDialogController);

    DelegateDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Delegate'];

    function DelegateDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Delegate) {
        var vm = this;

        vm.delegate = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.delegate.id !== null) {
                Delegate.update(vm.delegate, onSaveSuccess, onSaveError);
            } else {
                Delegate.save(vm.delegate, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:delegateUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
