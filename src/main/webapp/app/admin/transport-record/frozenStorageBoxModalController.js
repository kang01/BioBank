/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController);

    FrozenStorageBoxModalController.$inject = ['DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController(DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal) {
        var vm = this;
        vm.boxCodeList = [];
        var codeList = [];

        vm.addData = function (event) {

            if(window.event.keyCode == 13){
                if(vm.boxCode != ''){
                    vm.boxCode += ",";
                    codeList = vm.boxCode.split(",");
                    codeList = codeList.reverse();
                    codeList.shift();
                    vm.boxCodeList.length = codeList.length;
                    for(var i = 0; i < codeList.length; i++){
                        vm.boxCodeList[i] = {code:codeList[i]}

                    }
                }


            }

        };
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('deferRender', true)
            .withOption('scrollY', 300)
        // vm.dtColumns = [
        //     DTColumnBuilder.newColumn('code').withTitle('冻存盒号'),
        //     DTColumnBuilder.newColumn('firstName').withTitle('状态'),
        //     DTColumnBuilder.newColumn('lastName').withTitle('样本类型').notVisible(),
        //     DTColumnBuilder.newColumn('lastName').withTitle('样本数').notVisible(),
        //     DTColumnBuilder.newColumn('lastName').withTitle('是否分装').notVisible()
        // ];

        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            $uibModalInstance.close(vm.boxCodeList);
        };
    }
})();
