/**
 * Created by gaokangkang on 2017/5/12.
 * 样本交接
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverModalController', TakeOverModalController);

    TakeOverModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function TakeOverModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
