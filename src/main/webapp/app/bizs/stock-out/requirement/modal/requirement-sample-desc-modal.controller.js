/**
 * Created by gaokangkang on 2017/5/12.
 * 申请样本库存详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementSampleDescModalController', RequirementSampleDescModalController);

    RequirementSampleDescModalController.$inject = ['$uibModalInstance','$uibModal','items'];

    function RequirementSampleDescModalController($uibModalInstance,$uibModal,items) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
