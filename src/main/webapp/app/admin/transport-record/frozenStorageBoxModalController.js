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
        // vm.frozenTubeArray = [];//初始管子
        vm.addData = function (event) {
            if(window.event.keyCode == 13){
                if(vm.boxCode != ''){
                    codeList = _.uniq((vm.boxCode.split("\n")).reverse());
                    for(var i = 0; i < codeList.length; i++){
                        vm.boxCodeList[i] = {
                            frozenBoxCode:codeList[i],
                            frozenBoxTypeId:17,//冻存盒类型ID 17：10*10 18：8*8
                            frozenBoxTypeCode:'BOX_TYPE_0002',
                            isSplit: "0003",//是否分装:'否:0003 是：0002',
                            memo:'',
                            status: "3003",//状态
                            dislocationNumber:0,
                            emptyHoleNumber:0,
                            emptyTubeNumber:0,
                            frozenBoxColumns:10,
                            frozenBoxRows:10,
                            equipmentId:'',
                            equipmentCode:'',
                            areaId:'',
                            areaCode:'',
                            supportRackId:'',
                            supportRackCode:'',
                            columnsInShelf:"",//所在架子行数
                            rowsInShelf:"",//所在架子列数,
                            isRealData:'4002',
                            sampleNumber: 100,//样本数量
                            sampleTypeId: '',//样本类型ID
                            sampleTypeCode:'',//样本类型
                            frozenTubeDTOS:"",//管子
                            sampleTypeName:'',
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
