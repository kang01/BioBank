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
                        vm.boxCodeList[i] = {
                            frozenBoxCode:codeList[i],
                            projectSiteCode:codeList[i],
                            frozenTubeDTOS:[
                                {
                                    errorType: "6001",//错误类型
                                    frozenBoxTypeCode:"BOX_TYPE_0002",//冻存盒类型编码
                                    frozenTubeCode: "5435345",//冻存管编码
                                    frozenTubeTypeCode: "TUBE_TYPE_0001",//冻存管类型编码
                                    frozenTubeTypeId: 12,//冻存管类型ID
                                    frozenTubeTypeName: "血浆",//冻存管类型名称
                                    isModifyPostition: "1",//是否修改位置
                                    isModifyState: "0002",//是否修改状态
                                    memo: "",//备注
                                    projectCode: "P_00001",//项目编码
                                    projectId: 1,//项目ID
                                    sampleCode: "3434",//样本编码
                                    sampleTempCode: "54135151",//样本临时编码
                                    sampleTypeCode: "S_TYPE_00001",//样本类型编码
                                    sampleTypeId: 5,//样本类型ID
                                    sampleTypeName: "血浆-紫",//样本类型名称
                                    status: "1",//状态
                                    tubeColumns: "10",//列数
                                    tubeRows: "10",//行数
                                    frozenTubeVolumnsUnit:"ml",//冻存管容量单位
                                    frozenBoxCode: codeList[i],//冻存盒编码
                                    sampleUsedTimes:1,//冻存盒已使用次数
                                    sampleUsedTimesMost:10,//冻存盒最多使用次数
                                    frozenTubeVolumns:10,//冻存管容量
                                    frontColor:10,//前景色，默认黑色
                                    backColor:10//背景色，管子帽颜色
                                }
                            ],
                            frozenBoxTypeId:17,//冻存盒类型ID
                            dislocationNumber:2,
                            emptyHoleNumber:2,
                            emptyTubeNumber:2,
                            frozenBoxColumns:8,
                            frozenBoxRows:8,
                            projectCode:'P_00001',
                            projectId:1,
                            projectName:'心血管高危筛查项目',
                            projectSiteId:2,
                            projectSiteName:'心血管高危筛查项目天坛医院项目点',
                            frozenBoxTypeCode:'BOX_TYPE_0002',
                            sampleTypeName:'血浆-紫1',
                            sampleTypeCode:'S_TYPE_00001',
                            isRealData:'4001',
                            isSplit: "0002",//是否分装:'4001',
                            memo:'memo',
                            sampleNumber: 20,//样本数量
                            sampleTypeId: 5,//样本类型ID
                            status: "1",//状态
                            columnsInShelf:"2",//所在架子行数
                            rowsInShelf:"B"//所在架子列数

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
