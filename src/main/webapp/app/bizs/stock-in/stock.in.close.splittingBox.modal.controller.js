/**
 * Created by gaokangkang on 2017/4/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CloseSplittingBoxController', CloseSplittingBoxController);

    CloseSplittingBoxController.$inject = ['$uibModalInstance'];

    function CloseSplittingBoxController($uibModalInstance) {
        var vm = this;

        vm.cancel = function () {
            $uibModalInstance.close(false);
        };
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
    }
})();
