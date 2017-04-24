/**
 * Created by gaokangkang on 2017/4/24.
 * 分装错误提示
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SplittingBoxMessageController', SplittingBoxMessageController);

    SplittingBoxMessageController.$inject = ['$uibModalInstance'];

    function SplittingBoxMessageController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
