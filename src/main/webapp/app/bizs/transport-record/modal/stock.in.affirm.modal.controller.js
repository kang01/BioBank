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
        vm.transportRecord.receiveDate = items.receiveDate;
        vm.transportRecord.login = items.receiver;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.stockInFlag = false;
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        //接收人
        vm.receiverConfig = {
            valueField:'login',
            labelField:'userName',
            maxItems: 1

        };
        //接收人
        SampleUserService.query({},onReceiverSuccess, onError);
        function onReceiverSuccess(data) {
            vm.receiverOptions = data;
        }
        function onError(data) {
            toastr.error(data.message);
        }

        vm.yes = function () {
          vm.stockInFlag  = true;
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(vm.transportRecord);
        };
    }
})();
