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
                console.log(JSON.stringify(vm.boxCode));
                if(vm.boxCode != ''){
                    codeList = vm.boxCode.split("\n");
                    codeList = codeList.reverse();
                    vm.boxCodeList.length = codeList.length;
                    for(var i = 0; i < codeList.length; i++){
                        vm.boxCodeList[i] = {
                            frozenBoxCode:codeList[i],
                            projectSiteCode:'',
                            frozenBoxTypeId:17,//冻存盒类型ID
                            dislocationNumber:'',
                            emptyHoleNumber:'',
                            emptyTubeNumber:'',
                            frozenBoxColumns:'',
                            frozenBoxRows:'',
                            projectCode:'',
                            projectId:1,
                            projectName:'',
                            projectSiteId:'',
                            projectSiteName:'',
                            frozenBoxTypeCode:'',
                            sampleTypeName:'',
                            sampleTypeCode:'',
                            isRealData:'',
                            isSplit: "",//是否分装:'4001',
                            memo:'',
                            sampleNumber: '',//样本数量
                            sampleTypeId: '',//样本类型ID
                            status: "1",//状态
                            columnsInShelf:"",//所在架子行数
                            rowsInShelf:"",//所在架子列数,
                            frozenTubeDTOS:[]

                        }

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
