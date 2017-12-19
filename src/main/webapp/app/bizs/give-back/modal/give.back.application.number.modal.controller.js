/**
 * Created by gaokangkang on 2017/12/5.
 * 根据申请单号获取归还单
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ApplicationNumberModalController', ApplicationNumberModalController);

    ApplicationNumberModalController.$inject = ['$uibModalInstance','toastr','GiveBackService'];

    function ApplicationNumberModalController($uibModalInstance,toastr,GiveBackService) {
        var vm = this;
        vm.entity = {
            applyCode:null,
            projectId:null,
            id:null
        };
        var _giveBackInfo = {
            projectId:null,
            applyId:null,
            applyCode:null
        };
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            maxItems: 1,
            searchField:'projectName',
            onChange:function(value){}
        };
        vm.queryGiveBackInfo = function () {
            if(vm.entity.applyCode){
                GiveBackService.queryApplyInfo(vm.entity.applyCode).success(function (data) {
                    _giveBackInfo.applyId = data.id;
                    vm.projectOptions = data.projectDTOS;
                }).error(function (data) {
                    toastr.error(data.message);
                });
            }

        };




        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            _giveBackInfo.projectId = vm.entity.projectId;
            _giveBackInfo.applyCode = vm.entity.applyCode;
            $uibModalInstance.close(_giveBackInfo);
        };
    }
})();
