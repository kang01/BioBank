/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    FrozenStorageBoxModalController.$inject = ['$scope','toastr','$timeout','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','items','TranshipBoxService','blockUI','AreasByEquipmentIdService','EquipmentService','SampleTypeService'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController($scope,toastr,$timeout,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,items,TranshipBoxService,blockUI,AreasByEquipmentIdService,EquipmentService,SampleTypeService) {

        var vm = this;
        vm.items = items;
        vm.sampleTypeFlag = false;
        // vm.importSample = importSample;//导入样本数据
        vm.codeList = [];//扫码录入的盒号
        vm.obox = {
            transhipId:vm.items.transhipId,
            frozenBoxDTOList:[]
        };
        vm.sampleTypeOptions = items.sampleTypeOptions;
        vm.frozenBoxTypeOptions = items.frozenBoxTypeOptions;
        vm.frozenBox = {};
        vm.frozenBox.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
        vm.frozenBox.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
        vm.frozenBox.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;

        if(vm.sampleTypeOptions.length){
            vm.frozenBox.sampleTypeId = vm.sampleTypeOptions[0].id;
            vm.isMixed = _.filter(vm.sampleTypeOptions,{'id':+vm.frozenBox.sampleTypeId})[0].isMixed;
            _fnQueryProjectSampleClass(vm.items.projectId,vm.frozenBox.sampleTypeId,vm.isMixed);
        }
        //设备
        EquipmentService.query({},onEquipmentTempSuccess, onError);
        //样本类型
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                vm.frozenBox.sampleTypeId = value;
                vm.isMixed = _.filter(vm.sampleTypeOptions,{'id':+value})[0].isMixed;
                _fnQueryProjectSampleClass(vm.items.projectId,value,vm.isMixed);

            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
                vm.frozenBox.sampleClassificationId = value;
                _fnInitBoxInfo();
            }
        };
        //盒类型
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                vm.frozenBox.frozenBoxTypeId  = value;
                for(var i = 0; i < vm.frozenBoxTypeOptions.length; i++){
                    if(vm.frozenBoxTypeOptions[i].id == value){
                        vm.frozenBox.frozenBoxTypeRows = vm.frozenBoxTypeOptions[i].frozenBoxTypeRows;
                        vm.frozenBox.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[i].frozenBoxTypeColumns;
                    }
                }
                _fnInitBoxInfo();
            }
        };
        //暂存区位置
        vm.frozenBoxPlaceConfigTemp = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
                if(value){
                    AreasByEquipmentIdService.query({id:value},onAreaTempSuccess, onError);
                }
            }
        };
        vm.frozenBoxAreaConfigTemp = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                vm.frozenBox.areaId  = value;
                _fnInitBoxInfo();
            }
        };

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('deferRender', true)
            .withOption('scrollY', 200);

        //录入冻存盒号
        var changeTableTimer = null;
        vm.boxCodeConfig = {
            create: true,
            persist:false,
            createOnBlur:false,
            onChange: function(value){
                clearTimeout(changeTableTimer);
                changeTableTimer = setTimeout(function () {
                    vm.obox.frozenBoxDTOList = [];
                    if(value.length){
                        vm.codeList = value.reverse();
                        _fnInitBoxInfo();
                    }
                    if ($scope.$digest()){
                        $scope.$apply();
                    }
                },500);
            }
        };
        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            blockUI.start("正在保存冻存盒中……");
            TranshipBoxService.save(vm.obox,onSaveBoxSuccess,onError);

        };
        //不同项目下的样本分类
        function _fnQueryProjectSampleClass(projectId,sampleTypeId,isMixed) {

            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
                if(vm.projectSampleTypeOptions.length){
                    vm.frozenBox.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                }
                if(vm.projectSampleTypeOptions.length){
                    if(isMixed == 1){
                        vm.sampleTypeFlag = false;
                    }else{
                        vm.sampleTypeFlag = true;
                    }
                }else{
                    vm.sampleTypeFlag = false;
                }
                // if(isMixed == 1){
                //     vm.sampleTypeFlag = true;
                // }else{
                //     vm.sampleTypeFlag = false;
                //
                // }
                _fnInitBoxInfo();


            });
        }
        function _fnCreateTempBox(code){
            var tubeList=[];
            var box = {
                frozenBoxCode:code,
                sampleTypeId:vm.frozenBox.sampleTypeId,
                isMixed:vm.isMixed,
                frozenBoxTypeId:vm.frozenBox.frozenBoxTypeId,
                equipmentId:vm.frozenBox.equipmentId,
                areaId:vm.frozenBox.areaId,
                sampleClassificationId: vm.frozenBox.sampleClassificationId || undefined,
                frozenTubeDTOS:[]
            };
            for(var j = 0; j < vm.frozenBox.frozenBoxTypeRows;j++){
                tubeList[j] = [];
                var rowNO = j > 7 ? j+1 : j;
                rowNO = String.fromCharCode(rowNO+65);
                for(var k = 0; k < vm.frozenBox.frozenBoxTypeColumns; k++){
                    tubeList[j][k] = {
                        frozenBoxCode: box.frozenBoxCode,
                        sampleCode: "",
                        sampleTempCode: box.frozenBoxCode+"-"+rowNO+(k+1),
                        sampleTypeId: box.sampleTypeId,
                        sampleClassificationId:box.sampleClassificationId,
                        status: "3001",
                        tubeRows:rowNO,
                        tubeColumns: k+1
                    };
                    if(box.isMixed == 1) {
                        for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                            if (vm.projectSampleTypeOptions[l].columnsNumber == k + 1) {
                                tubeList[j][k].sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                            }
                        }
                    }
                    box.frozenTubeDTOS.push(tubeList[j][k]);

                }
            }
            //是混合类型
            if(box.isMixed == 1){
                box.isSplit = 1;
                delete box.sampleClassificationId;
            }else{
                box.isSplit = 0;
            }
            return box;
        }
        function _fnInitBoxInfo() {
            vm.obox.frozenBoxDTOList = [];
            for(var i = 0; i < vm.codeList.length; i++){
                vm.obox.frozenBoxDTOList.push(_fnCreateTempBox(vm.codeList[i]));
            }
        }
        function onEquipmentTempSuccess(data) {
            vm.frozenBoxPlaceOptions = data;
            vm.frozenBox.equipmentId = vm.frozenBoxPlaceOptions[0].id;
            AreasByEquipmentIdService.query({id:vm.frozenBox.equipmentId},onAreaTempSuccess, onError);
        }
        function onAreaTempSuccess(data) {
            vm.frozenBoxHoldAreaOptions = data;
            vm.frozenBox.areaId = vm.frozenBoxHoldAreaOptions[0].id;
            _fnInitBoxInfo();
        }
        function onSaveBoxSuccess(data) {
            blockUI.stop();
            $uibModalInstance.close();

        }
        function onError(data) {
            toastr.error(data.data.message);
            $timeout(function () {
                blockUI.stop();
            },1000);

        }

    }
    function ModalInstanceCtrl($uibModalInstance,$uibModal) {
        var ctrl = this;
        ctrl.ok = function () {
            $uibModalInstance.close(true);
        };
        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();


