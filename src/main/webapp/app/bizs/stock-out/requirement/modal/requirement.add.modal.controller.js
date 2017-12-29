/**
 * Created by gaokangkang on 2017/12/29.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementAddModalController', RequirementAddModalController);

    RequirementAddModalController.$inject = ['$uibModalInstance','toastr','MasterMethod','GiveBackService','items'];

    function RequirementAddModalController($uibModalInstance,toastr,MasterMethod,GiveBackService,items) {
        var vm = this;
        vm.entity = {};

        _loadInitializeDataFromServer();

        function _loadInitializeDataFromServer() {
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
        }

        vm.ok = function () {
            $uibModalInstance.close(vm.entity);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }
})();
