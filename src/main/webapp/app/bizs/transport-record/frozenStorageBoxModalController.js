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
            _fnQueryProjectSampleClass(vm.items.projectId,vm.frozenBox.sampleTypeId)
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
                _fnQueryProjectSampleClass(vm.items.projectId,value)

            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassficationName',
            maxItems: 1,
            onChange:function (value) {

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
                        vm.frozenBox.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[i].frozenBoxTypeColumns
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
                AreasByEquipmentIdService.query({id:value},onAreaTempSuccess, onError);
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
            .withOption('scrollY', 300);
        //录入冻存盒号
        vm.boxCodeConfig = {
            create: true,
            persist:false,
            onChange: function(value){
                vm.obox.frozenBoxDTOList = [];
                if(value.length){
                    vm.codeList = value.reverse();
                    _fnInitBoxInfo();
                }
                $scope.$apply();
            }
        };
        // function importSample() {
        //     var modalInstance = $uibModal.open({
        //         animation: true,
        //         templateUrl: 'stackedModal.html',
        //         size: 'sm',
        //         controller: 'ModalInstanceCtrl',
        //         controllerAs: 'ctrl'
        //     });
        //     modalInstance.result.then(function (flag) {
        //
        //     }, function () {
        //     });
        // }
        this.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        this.ok = function () {
            // console.log(JSON.stringify(vm.obox));
            blockUI.start("正在保存冻存盒中……");
            TranshipBoxService.save(vm.obox,onSaveBoxSuccess,onError);

        };
        //不同项目下的样本分类
        vm.sampleTypeFlag = false;
        function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
            if(sampleTypeId == 5){
                vm.sampleTypeFlag = true;
            }else{
                vm.sampleTypeFlag = false;
            }
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
                vm.frozenBox.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                _fnInitBoxInfo();
            });
        }
        function _fnInitBoxInfo() {
            var tubeList=[];
            for(var i = 0; i < vm.codeList.length; i++){
                vm.obox.frozenBoxDTOList[i] = {
                    frozenBoxCode:vm.codeList[i],
                    sampleTypeId:vm.frozenBox.sampleTypeId,
                    frozenBoxTypeId:vm.frozenBox.frozenBoxTypeId,
                    equipmentId:vm.frozenBox.equipmentId,
                    areaId:vm.frozenBox.areaId,
                    frozenTubeDTOS:[]
                };
                if(vm.frozenBox.sampleClassificationId){
                    vm.obox.frozenBoxDTOList[i].sampleClassificationId = vm.frozenBox.sampleClassificationId;
                }
                for(var j = 0; j < vm.frozenBox.frozenBoxTypeRows;j++){
                    tubeList[j] = [];
                    for(var k = 0; k < vm.frozenBox.frozenBoxTypeColumns; k++){
                        tubeList[j][k] = {
                            frozenBoxCode: vm.codeList[i],
                            sampleCode: "",
                            sampleTempCode: vm.codeList[i]+"-"+String.fromCharCode(j+65)+(k+1),
                            sampleTypeId: vm.frozenBox.sampleTypeId,
                            sampleClassificationId:vm.frozenBox.sampleClassificationId,
                            status: "3001",
                            tubeRows:String.fromCharCode(j+65),
                            tubeColumns: k+1
                        };
                        if(j > 7){
                            tubeList[j][k].tubeRows = String.fromCharCode(j+1+65);
                            tubeList[j][k].sampleTempCode = vm.codeList[i]+"-"+String.fromCharCode(j+1+65)+(k+1)
                        }
                        vm.obox.frozenBoxDTOList[i].frozenTubeDTOS.push(tubeList[j][k])
                    }
                }

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
            },1000)

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


