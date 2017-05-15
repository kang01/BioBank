(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutApplyProjectDialogController', StockOutApplyProjectDialogController);

    StockOutApplyProjectDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'StockOutApplyProject', 'StockOutApply', 'Project'];

    function StockOutApplyProjectDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, StockOutApplyProject, StockOutApply, Project) {
        var vm = this;

        vm.stockOutApplyProject = entity;
        vm.clear = clear;
        vm.save = save;
        vm.stockoutapplies = StockOutApply.query();
        vm.projects = Project.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.stockOutApplyProject.id !== null) {
                StockOutApplyProject.update(vm.stockOutApplyProject, onSaveSuccess, onSaveError);
            } else {
                StockOutApplyProject.save(vm.stockOutApplyProject, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:stockOutApplyProjectUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
