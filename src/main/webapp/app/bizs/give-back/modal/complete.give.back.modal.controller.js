/**
 * Created by gaokangkang on 2017/12/19.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('CompleteGiveBackModalController', CompleteGiveBackModalController);

    CompleteGiveBackModalController.$inject = ['$uibModalInstance','items','SampleUserService','toastr','GiveBackService'];

    function CompleteGiveBackModalController($uibModalInstance,items,SampleUserService,toastr,GiveBackService) {

        var vm = this;
        vm.receiverOptions = items.receiverOptions;

        vm.giveBackRecord = {
            receiveDate:items.receiveDate,
            receiverId:items.receiverId,
            password:null
        };
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
            $uibModalInstance.close(vm.giveBackRecord);
        };
    }
})();
