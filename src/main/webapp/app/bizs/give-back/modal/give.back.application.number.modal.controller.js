/**
 * Created by gaokangkang on 2017/12/5.
 * 根据申请单号获取归还单
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ApplicationNumberModalController', ApplicationNumberModalController);

    ApplicationNumberModalController.$inject = ['$uibModalInstance','ProjectService','ProjectSitesByProjectIdService'];

    function ApplicationNumberModalController($uibModalInstance,ProjectService,ProjectSitesByProjectIdService) {
        var vm = this;
        _init();
        function _init() {

        }





        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close();
        };
    }
})();
