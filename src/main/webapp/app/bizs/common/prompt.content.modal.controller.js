/**
 * Created by gaokangkang on 2017/3/21.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PromptContentModalController', PromptContentModalController);

    PromptContentModalController.$inject = ['$scope','$uibModalInstance','$uibModal','items'];

    function PromptContentModalController($scope,$uibModalInstance,$uibModal,items) {
        var vm = this;


        vm.ok = function () {
            $uibModalInstance.close(vm.text);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
