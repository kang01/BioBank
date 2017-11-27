/**
 * Created by gaokangkang on 2017/4/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CloseSplittingBoxController', CloseSplittingBoxController);

    CloseSplittingBoxController.$inject = ['$uibModalInstance','items'];

    function CloseSplittingBoxController($uibModalInstance,items) {
        var vm = this;
        vm.status = items.status;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
    }
})();
