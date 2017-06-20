/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAddSampleModal', StockInAddSampleModal);

    StockInAddSampleModal.$inject = ['$uibModalInstance','items'];

    function StockInAddSampleModal($uibModalInstance,items) {
        var vm = this;
        vm.status = items.status;

        vm.cancel = function () {
            $uibModalInstance.close(false);
        };
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
    }
})();
