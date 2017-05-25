/**
 * Created by gaokangkang on 2017/5/12.
 * 申请样本出库批准
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementApprovalModalController', RequirementApprovalModalController);

    RequirementApprovalModalController.$inject = ['$uibModalInstance','toastr','$uibModal','BioBankBlockUi','items','SampleUserService','RequirementService','moment'];

    function RequirementApprovalModalController($uibModalInstance,toastr,$uibModal,BioBankBlockUi,items,SampleUserService,RequirementService,moment) {
        var vm = this;
        vm.approve = {};
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.requirement = items.requirement;
        vm.approve.approverId = vm.requirement.recordId;
        vm.startTime = moment(vm.requirement.startTime).format("YYYY-MM-DD");
        vm.endTime = moment(vm.requirement.endTime).format("YYYY-MM-DD");
        vm.approve.recordTime = new Date();
        //批注人
        SampleUserService.query({},onApproverSuccess, onError);
        function onApproverSuccess(data) {
            vm.approverOptions = data;
        }
        vm.approverConfig = {
            valueField:'id',
            labelField:'userName',
            maxItems: 1

        };
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        vm.conditions = [
            {id:1,name:"已将样本出库申请单送达委托方",checked:false},
            {id:2,name:"委托方有使用样本的授权",checked:false},
            {id:3,name:"委托方已经核对过样本出库单申请",checked:false},
            {id:4,name:"委托方已经获知部分需求样本",checked:false}
        ];
        vm.checkCondition = function () {
            vm.conditionLen = _.filter(vm.conditions,{checked:true}).length;
        };
        vm.ok = function () {
            BioBankBlockUi.blockUiStart();
            RequirementService.approveSampleRequirement(vm.requirement.id,vm.approve).success(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.success("批准成功!");
            }).error(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.error("批准失败!");
            });
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }


    }
})();
