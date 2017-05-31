/**
 * Created by gaokangkang on 2017/5/12.
 * 样本交接
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverModalController', TakeOverModalController);

    TakeOverModalController.$inject = ['$uibModalInstance','$uibModal','stockOutTakeOver','stockOutApplication','stockOutBoxes'];

    function TakeOverModalController($uibModalInstance,$uibModal,stockOutTakeOver,stockOutApplication,stockOutBoxes) {
        var vm = this;

        vm.handoverPersonName = stockOutTakeOver.handoverPersonName;

        vm.countOfSamples = 0;
        _.each(stockOutBoxes, function(b){
            vm.countOfSamples += b.countOfSample;
        });

        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
