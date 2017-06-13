/**
 * Created by gaokangkang on 2017/3/21.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('microtubesRemarkModalController', microtubesRemarkModalController);

    microtubesRemarkModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function microtubesRemarkModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.items = items;

        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close(vm.memo);
        };
    }
})();
