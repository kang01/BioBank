/**
 * Created by gaokangkang on 2017/5/12.
 * 样本交接
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverModalController', TakeOverModalController);

    TakeOverModalController.$inject = ['$uibModalInstance','$uibModal','items','TakeOverService','toastr','SampleUserService'];

    function TakeOverModalController($uibModalInstance,$uibModal,items,TakeOverService,toastr,SampleUserService) {
        var vm = this;
        vm.takeOver = {};
        vm.datePickerOpenStatus = {};
        vm.openCalendar = function (date) {
            vm.datePickerOpenStatus[date] = true;
        };
        vm.stockOutTakeOver = items.stockOutTakeOver;
        vm.stockOutTakeOver.handoverTime = moment(items.stockOutTakeOver.handoverTime).format("YYYY-MM-DD");
        vm.countOfSamples = 0;
        var boxIdsStr = items.boxIdsStr;
        vm.takeOver.handoverPersonName = items.stockOutTakeOver.handoverPersonName;
        vm.takeOver.handoverPersonId = items.stockOutTakeOver.handoverPersonId;
        vm.takeOver.id = items.stockOutTakeOver.id;
        vm.takeOver.handoverTime = new Date();

        //交付人
        SampleUserService.query({},onReceiverSuccess, onError);
        function onReceiverSuccess(data) {
            vm.loginOptions = data;
        }
        //交付人
        vm.loginConfig = {
            valueField:'id',
            labelField:'userName',
            maxItems: 1,
            onChange:function (value) {
                vm.takeOver.handoverPersonId = value;
                // vm.dto.handoverPersonName = _.filter(vm.loginOptions,{id:+value})[0].userName;
            }
        };
        // _.each(stockOutBoxes, function(b){
        //     vm.countOfSamples += b.countOfSample;
        // });




        vm.ok = function () {
            // console.log(JSON.stringify(vm.takeOver));
            delete vm.takeOver.handoverPersonName;
            TakeOverService.saveTakeOverComplete(boxIdsStr,vm.takeOver).success(function (data) {
                toastr.success("交接成功!");
                $uibModalInstance.close();
            });

        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };

        function onError(error) {
            toastr.error(error.message);
        }


    }
})();
