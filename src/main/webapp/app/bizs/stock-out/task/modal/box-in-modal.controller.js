/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中装盒操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskBoxInModalController', TaskBoxInModalController);

    TaskBoxInModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function TaskBoxInModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
