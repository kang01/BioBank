(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AttachmentDialogController', AttachmentDialogController);

    AttachmentDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Attachment'];

    function AttachmentDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Attachment) {
        var vm = this;

        vm.attachment = entity;
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
            if (vm.attachment.id !== null) {
                Attachment.update(vm.attachment, onSaveSuccess, onSaveError);
            } else {
                Attachment.save(vm.attachment, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('bioBankApp:attachmentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
