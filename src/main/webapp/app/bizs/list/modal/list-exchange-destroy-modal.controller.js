/**
 * Created by gaokangkang on 2017/8/4.
 * 清单交换操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ListExchangeDestroyModalController', ListExchangeDestroyModalController);

    ListExchangeDestroyModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function ListExchangeDestroyModalController($uibModalInstance,$uibModal,items) {

        var vm = this;
        //1.交换 2.销毁
        vm.operateStatus = items.operateStatus;
        vm.ok = function () {
            $uibModalInstance.close(vm.reason);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
