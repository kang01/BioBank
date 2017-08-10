/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController)
        .controller('ProgressBarModalController', ProgressBarModalController);

    FrozenStorageBoxModalController.$inject = ['$scope','toastr','$timeout','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','items','TranshipBoxService','blockUI','AreasByEquipmentIdService','EquipmentService','SampleTypeService'];
    ProgressBarModalController.$inject = ['$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController($scope,toastr,$timeout,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,items,TranshipBoxService,blockUI,AreasByEquipmentIdService,EquipmentService,SampleTypeService) {

        var vm = this;
        vm.items = items;
        vm.sampleTypeFlag = false;
        var modalInstance;
        vm.codeList = [];//扫码录入的盒号
        vm.obox = {
            transhipId:vm.items.transhipId,
            frozenBoxDTOList:[]
        };
        //删除盒子
        vm.delBox = _fnDelBox;
        //导入样本数据
        vm.importSample = _fnImportSample;
        //停止
        vm.stop = _fnStop;



        vm.sampleTypeOptions = items.sampleTypeOptions;
        vm.frozenBoxTypeOptions = items.frozenBoxTypeOptions;
        vm.frozenBox = {};
        vm.frozenBox.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
        vm.frozenBox.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
        vm.frozenBox.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;

        if(vm.sampleTypeOptions.length){
            vm.frozenBox.sampleTypeId = vm.sampleTypeOptions[0].id;
            vm.frozenBox.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
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
                vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+value}).isMixed;
                vm.frozenBox.sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                vm.frozenBox.sampleTypeId = value;
                if(vm.frozenBox.sampleTypeCode == 'RNA'){
                    vm.box.isSplit = 1;
                }
                _fnEditBoxInfo();
                _fnQueryProjectSampleClass(vm.items.projectId,value,vm.isMixed);

            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
                vm.frozenBox.sampleClassificationId = value;
                // _fnInitBoxInfo();
            }
        };
        //盒类型
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                vm.frozenBox.frozenBoxTypeId  = value;
                vm.frozenBox.frozenBoxTypeRows = _.find(vm.frozenBoxTypeOptions,{id:+value}).frozenBoxTypeRows;
                vm.frozenBox.frozenBoxTypeColumns = _.find(vm.frozenBoxTypeOptions,{id:+value}).frozenBoxTypeColumns;
                // for(var i = 0; i < vm.frozenBoxTypeOptions.length; i++){
                //     if(vm.frozenBoxTypeOptions[i].id == value){
                //         vm.frozenBox.frozenBoxTypeRows = vm.frozenBoxTypeOptions[i].frozenBoxTypeRows;
                //         vm.frozenBox.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[i].frozenBoxTypeColumns;
                //     }
                // }
                _fnEditBoxInfo();
            }
        };
        //暂存区位置
        //设备
        vm.frozenBoxPlaceConfigTemp = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
                if(value){
                    AreasByEquipmentIdService.query({id:value},onAreaTempSuccess, onError);
                }else{
                    vm.frozenBoxHoldAreaOptions = [
                        {id:"",areaCode:""}
                    ];
                    vm.frozenBox.areaId = "";
                    $scope.$apply();
                }
            }
        };
        //区域
        vm.frozenBoxAreaConfigTemp = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                vm.frozenBox.areaId  = value;
                _fnEditBoxInfo();
            }
        };
        //不同项目下的样本分类
        function _fnQueryProjectSampleClass(projectId,sampleTypeId,isMixed) {

            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
                if(vm.projectSampleTypeOptions.length){
                    vm.frozenBox.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    vm.frozenBox.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    if(isMixed == 1){
                        vm.sampleTypeFlag = false;
                    }else{
                        vm.sampleTypeFlag = true;
                    }
                }else{
                    vm.sampleTypeFlag = false;
                }
                // _fnInitBoxInfo();


            });
        }

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('deferRender', true)
            .withOption('scrollY', 220);
        vm.dtColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "auto"),
            DTColumnBuilder.newColumn(1).withOption("width", "80"),
            DTColumnBuilder.newColumn(2).withOption("width", "auto"),
            DTColumnBuilder.newColumn(3).withOption("width", "80"),
            DTColumnBuilder.newColumn(4).withOption("width", "80"),
            DTColumnBuilder.newColumn(5).withOption("width", "60")
        ];
        //录入冻存盒号
        var changeTableTimer = null;
        vm.boxCodeConfig = {
            create: true,
            persist:false,
            createOnBlur:false,
            onInitialize: function(selectize){
                vm.boxCodeSelectize = selectize;
            },
            onChange: function(value){
                clearTimeout(changeTableTimer);
                changeTableTimer = setTimeout(function () {
                    // vm.obox.frozenBoxDTOList = [];
                    if(value.length){
                        vm.codeList = value;
                        _fnInitBoxInfo();
                    }
                    if ($scope.$digest()){
                        $scope.$apply();
                    }
                },500);
            }
        };

        //创建盒子
        function _fnCreateTempBox(code){
            var tubeList=[];
            var box = {
                frozenBoxCode:code,
                sampleTypeId:vm.frozenBox.sampleTypeId,
                sampleTypeCode:vm.frozenBox.sampleTypeCode,
                isMixed:vm.isMixed,
                frozenBoxTypeId:vm.frozenBox.frozenBoxTypeId,
                equipmentId:vm.frozenBox.equipmentId,
                areaId:vm.frozenBox.areaId,
                sampleClassificationId: vm.frozenBox.sampleClassificationId || undefined,
                sampleClassificationCode: vm.frozenBox.sampleClassificationCode || undefined,
                isRepeat:0,
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
                        sampleTypeCode: box.sampleTypeCode,
                        sampleClassificationId:box.sampleClassificationId,
                        sampleClassificationCode:box.sampleClassificationCode,
                        status: "3001",
                        tubeRows:rowNO,
                        tubeColumns: k+1
                    };
                    if(box.isMixed == 1) {
                        for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                            if (vm.projectSampleTypeOptions[l].columnsNumber == k + 1) {
                                tubeList[j][k].sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                                tubeList[j][k].sampleClassificationCode = vm.projectSampleTypeOptions[l].sampleClassificationCode;
                            }
                        }
                    }
                    box.frozenTubeDTOS.push(tubeList[j][k]);

                }
            }
            //是混合类型
            if(box.isMixed == 1 || box.sampleTypeCode == 'RNA'){
                box.isSplit = 1;
                delete box.sampleClassificationId;
            }else{
                box.isSplit = 0;
            }
            return box;
        }
        //删除盒子
        function _fnDelBox(item) {
            _.remove(vm.obox.frozenBoxDTOList,{frozenBoxCode:item.frozenBoxCode});
            _.pull(vm.arrayBoxCode,item.frozenBoxCode);
            vm.boxCodeSelectize.removeOption(item.frozenBoxCode);
        }
        //导入数据
        vm.progressFlag = false;
        function _fnImportSample() {
            vm.progressFlag = true;
            vm.count = 50;
            vm.obj = {
                width:vm.count+'%'
            };
            _fnEditBoxInfo();
            // setTimeout(function () {
            //
            //     console.log(vm.count)
            //     vm.obj.width = vm.count;
            // },1000);
            // modalInstance = $uibModal.open({
            //     animation: true,
            //     templateUrl: 'progressBar.html',
            //     controller: 'ProgressBarModalController',
            //     backdrop:'static',
            //     controllerAs: 'vm',
            //     size:'progress-bar'
            // });
            // modalInstance.result.then(function () {
            // });
        }

        function _fnInitBoxInfo() {
            // vm.obox.frozenBoxDTOList = [];
            for(var i = 0; i < vm.codeList.length; i++){
                if(!vm.obox.frozenBoxDTOList.length){
                    vm.obox.frozenBoxDTOList.push(_fnCreateTempBox(vm.codeList[i]));
                }
                var len = _.filter(vm.obox.frozenBoxDTOList,{frozenBoxCode:vm.codeList[i]}).length;
                if(!len){
                    vm.obox.frozenBoxDTOList.push(_fnCreateTempBox(vm.codeList[i]));
                }
                vm.obox.frozenBoxDTOList.reverse();
            }
        }
        function _fnEditBoxInfo() {
            var tubeList=[];
            for(var i = 0; i < vm.obox.frozenBoxDTOList.length; i++){
                var boxes = vm.obox.frozenBoxDTOList[i];
                boxes.sampleTypeId = vm.frozenBox.sampleTypeId;
                boxes.sampleTypeCode = vm.frozenBox.sampleTypeCode;
                boxes.frozenBoxTypeId = vm.frozenBox.frozenBoxTypeId;
                boxes.frozenBoxTypeRows = vm.frozenBox.frozenBoxTypeRows;
                boxes.frozenBoxTypeColumns = vm.frozenBox.frozenBoxTypeColumns;
                boxes.equipmentId = vm.frozenBox.equipmentId;
                boxes.areaId = vm.frozenBox.areaId;
                boxes.frozenTubeDTOS = [];
                for(var j = 0; j < vm.frozenBox.frozenBoxTypeRows;j++){
                    tubeList[j] = [];
                    var rowNO = j > 7 ? j+1 : j;
                    rowNO = String.fromCharCode(rowNO+65);
                    for(var k = 0; k < vm.frozenBox.frozenBoxTypeColumns; k++){
                        tubeList[j][k] = {
                            frozenBoxCode: boxes.frozenBoxCode,
                            sampleCode: "",
                            sampleTempCode: boxes.frozenBoxCode+"-"+rowNO+(k+1),
                            sampleTypeId: boxes.sampleTypeId,
                            sampleTypeCode: boxes.sampleTypeCode,
                            sampleClassificationId:boxes.sampleClassificationId,
                            sampleClassificationCode:boxes.sampleClassificationCode,
                            status: "3001",
                            tubeRows:rowNO,
                            tubeColumns: k+1
                        };
                        if(boxes.isMixed == 1) {
                            for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                                if (vm.projectSampleTypeOptions[l].columnsNumber == k + 1) {
                                    tubeList[j][k].sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                                    tubeList[j][k].sampleClassificationCode = vm.projectSampleTypeOptions[l].sampleClassificationCode;
                                }
                            }
                        }

                        boxes.frozenTubeDTOS.push(tubeList[j][k]);

                    }
                }
            }
            console.log(JSON.stringify(vm.obox.frozenBoxDTOList));
        }

        function _fnStop() {
            vm.progressFlag = false;
        }




        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            if(vm.frozenBox.sampleTypeCode == '99' &&  !vm.projectSampleTypeOptions.length){
                toastr.error("样本类型为99时，无样本分类，不能入库！");
                return;
            }
            blockUI.start("正在保存冻存盒中……");
            TranshipBoxService.save(vm.obox,onSaveBoxSuccess,onError);
        };
        function onEquipmentTempSuccess(data) {
            vm.frozenBoxPlaceOptions = data;
            // vm.frozenBox.equipmentId = vm.frozenBoxPlaceOptions[0].id;
            // AreasByEquipmentIdService.query({id:vm.frozenBox.equipmentId},onAreaTempSuccess, onError);
        }
        function onAreaTempSuccess(data) {
            vm.frozenBoxHoldAreaOptions = data;
            vm.frozenBox.areaId = vm.frozenBoxHoldAreaOptions[0].id;
            _fnEditBoxInfo();
        }
        function onSaveBoxSuccess(data) {
            blockUI.stop();
            $uibModalInstance.close();

        }
        function onError(data) {
            toastr.error(data.data.message);
            var boxCodes = _.split(data.data.params[0], ',');
            for(var i = 0; i < vm.obox.frozenBoxDTOList.length;i++){
                for(var j = 0; j < boxCodes.length;j++){
                    if(vm.obox.frozenBoxDTOList[i].frozenBoxCode == boxCodes[j]){
                        vm.obox.frozenBoxDTOList[i].isRepeat = 1;
                    }
                }
            }
            $timeout(function () {
                blockUI.stop();
            },1000);

        }

    }
    function ProgressBarModalController($uibModalInstance,$uibModal) {
        var vm = this;
        vm.stop = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();


