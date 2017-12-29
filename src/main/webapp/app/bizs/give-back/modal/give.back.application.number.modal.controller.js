/**
 * Created by gaokangkang on 2017/12/5.
 *
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ApplicationNumberModalController', ApplicationNumberModalController);

    ApplicationNumberModalController.$inject = ['$uibModalInstance','toastr','MasterMethod','GiveBackService','items'];

    function ApplicationNumberModalController($uibModalInstance,toastr,MasterMethod,GiveBackService,items) {
        var vm = this;
        vm.entity = {
            applyCode:null,
            projectId:null,
            id:null,
            checkTypeId:null
        };
        //交接
        vm.entity.applyCode = items.applyCode;
        // if(vm.entity.applyCode){
        //     _queryProject();
        // }
        var _giveBackInfo = {
            projectId:null,
            applyId:null,
            applyCode:null
        };
        MasterMethod.queryProject().then(function (data) {
            vm.projectOptions = data;
        });
        MasterMethod.queryCheckType().success(function (data) {
            vm.checkTypeOptions = data;
        });
        MasterMethod.queryDelegates().success(function (data) {
            vm.delegatesOptions = data;
        });



        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            maxItems: 1,
            searchField:'projectName',
            onChange:function(value){}
        };
        vm.checkTypeConfig = {
            valueField:'id',
            labelField:'checkTypeName',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        //委托方
        vm.delegatesConfig = {
            valueField:'id',
            labelField:'delegateName',
            maxItems: 1
        };
        // vm.queryGiveBackInfo = _queryProject;
        // function _queryProject() {
        //     if(vm.entity.applyCode){
        //         GiveBackService.queryApplyInfo(vm.entity.applyCode).success(function (data) {
        //             _giveBackInfo.applyId = data.id;
        //             vm.projectOptions = data.projectDTOS;
        //         }).error(function (data) {
        //             toastr.error(data.message);
        //         });
        //     }
        // }



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
