/**
 * Created by gaoyankang on 2017/4/13.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAffirmModalController', StockInAffirmModalController);

    StockInAffirmModalController.$inject = ['$uibModalInstance','$uibModal','items','SampleUserService','toastr'];

    function StockInAffirmModalController($uibModalInstance,$uibModal,items,SampleUserService,toastr) {

        var vm = this;
        vm.transportRecord = {};
        vm.box = items.box;
        vm.receiverOptions = items.receiverOptions
        vm.transportRecord.receiveDate = items.receiveDate;
        vm.transportRecord.receiverId = items.receiverId;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        //接收人
        vm.receiverConfig = {
            valueField:'id',
            labelField:'userName',
            maxItems: 1

        };
        //接收人
        // SampleUserService.query({},onReceiverSuccess, onError);
        // function onReceiverSuccess(data) {
        //     vm.receiverOptions = data;
        // }
        // function onError(data) {
        //     toastr.error(data.message);
        // }

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            // vm.transportRecord.login = _.find(vm.receiverOptions,{id:+items.receiverId}).login;
            $uibModalInstance.close(vm.transportRecord);
        };
    }
})();
