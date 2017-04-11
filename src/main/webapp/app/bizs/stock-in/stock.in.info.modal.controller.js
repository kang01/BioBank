/**
 * Created by gaokangkang on 2017/4/11.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInInfoModalController', StockInInfoModalController);

    StockInInfoModalController.$inject = ['$uibModalInstance','$uibModal','items','AlertService'];

    function StockInInfoModalController($uibModalInstance,$uibModal,items,AlertService) {
        var vm = this;
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(vm.box);
        };
    }
})();
