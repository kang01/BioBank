/**
 * Created by gaokangkang on 2017/4/11.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInInfoModalController', StockInInfoModalController);

    StockInInfoModalController.$inject = ['$uibModalInstance','$uibModal','AlertService','StockInInfoService','items'];

    function StockInInfoModalController($uibModalInstance,$uibModal,AlertService,StockInInfoService,items) {
        var vm = this;

        vm.stockInInfo = items;
        vm.stockInInfo.stockInDate = new Date();

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            StockInInfoService.update(vm.stockInInfo,onSaveSuccess,onError);
            function onSaveSuccess(data) {
                $uibModalInstance.close(true);
            }
            function onError(error) {
                AlertService.error(error.data.message);
            }

        };
    }
})();
