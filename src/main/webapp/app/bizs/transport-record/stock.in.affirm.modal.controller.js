/**
 * Created by gaoyankang on 2017/4/13.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAffirmModalController', StockInAffirmModalController)

    StockInAffirmModalController.$inject = ['$uibModalInstance','$uibModal'];

    function StockInAffirmModalController($uibModalInstance,$uibModal) {

        var vm = this;

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
    }
})();
