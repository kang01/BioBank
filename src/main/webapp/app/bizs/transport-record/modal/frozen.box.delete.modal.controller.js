/**
 * Created by gaokangkang on 2017/4/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxDeleteController', FrozenBoxDeleteController);

    FrozenBoxDeleteController.$inject = ['$uibModalInstance'];

    function FrozenBoxDeleteController($uibModalInstance) {
        var vm = this;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close();
        };
    }
})();
