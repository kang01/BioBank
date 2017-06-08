(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipBoxDialogController', TranshipBoxDialogController);

    TranshipBoxDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TranshipBox', 'Tranship', 'FrozenBox', 'TranshipBoxPosition'];

    function TranshipBoxDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TranshipBox, Tranship, FrozenBox, TranshipBoxPosition) {
        var vm = this;

        vm.transhipBox = entity;
        vm.clear = clear;
        vm.save = save;
        vm.tranships = Tranship.query();
        vm.frozenboxes = FrozenBox.query();
        vm.transhipboxpositions = TranshipBoxPosition.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transhipBox.id !== null) {
                TranshipBox.update(vm.transhipBox, onSaveSuccess, onSaveError);
            } else {
                TranshipBox.save(vm.transhipBox, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:transhipBoxUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
