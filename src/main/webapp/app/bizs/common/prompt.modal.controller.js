/**
 * Created by gaokangkang on 2017/12/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PromptModalController', PromptModalController);

    PromptModalController.$inject = ['$uibModalInstance','items'];

    function PromptModalController($uibModalInstance,items) {

        var vm = this;
        vm.status = items.status;

        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.unSave = function () {
            $uibModalInstance.close(false);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
