(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipTubeDialogController', TranshipTubeDialogController);

    TranshipTubeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'TranshipTube', 'TranshipBox', 'FrozenTube'];

    function TranshipTubeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, TranshipTube, TranshipBox, FrozenTube) {
        var vm = this;

        vm.transhipTube = entity;
        vm.clear = clear;
        vm.save = save;
        vm.transhipboxes = TranshipBox.query();
        vm.frozentubes = FrozenTube.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.transhipTube.id !== null) {
                TranshipTube.update(vm.transhipTube, onSaveSuccess, onSaveError);
            } else {
                TranshipTube.save(vm.transhipTube, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:transhipTubeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
