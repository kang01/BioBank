/**
 * Created by gaoyankang on 2017/4/13.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAffirmModalController', StockInAffirmModalController)

    StockInAffirmModalController.$inject = ['$uibModalInstance','$uibModal','items','SampleUserService','AlertService'];

    function StockInAffirmModalController($uibModalInstance,$uibModal,items,SampleUserService,AlertService) {

        var vm = this;
        vm.transportRecord = {};
        vm.box = items.box;
        vm.transportRecord.receiveDate = items.receiveDate;
        vm.transportRecord.login = items.receiver;
        // vm.boxRowCol = items.boxRowCol;

        // if(!vm.box.equipmentId || !vm.box.areaId ){
        //     vm.placeFlag = true
        // }else{
        //     vm.placeFlag = false;
        // }
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        //接收人
        vm.receiverConfig = {
            valueField:'login',
            labelField:'userName',
            maxItems: 1

        };
        SampleUserService.query({},onReceiverSuccess, onError)//接收人
        function onReceiverSuccess(data) {
            vm.receiverOptions = data;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close(vm.transportRecord);
        };
    }
})();
