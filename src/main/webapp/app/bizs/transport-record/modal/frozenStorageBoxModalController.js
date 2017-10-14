/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenStorageBoxModalController', FrozenStorageBoxModalController)
        .controller('ProgressBarModalController', ProgressBarModalController);

    FrozenStorageBoxModalController.$inject = ['$scope','$q','toastr','$timeout','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','items','TranshipBoxService','blockUI','blockUIConfig','AreasByEquipmentIdService','EquipmentAllService','SampleTypeService','TransportRecordService'];
    ProgressBarModalController.$inject = ['$uibModalInstance','$uibModal'];

    function FrozenStorageBoxModalController($scope,$q,toastr,$timeout,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,items,TranshipBoxService,blockUI,blockUIConfig,AreasByEquipmentIdService,EquipmentAllService,SampleTypeService,TransportRecordService) {

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
        //上传样本数据
        vm.uploadSample = _fnUploadSample;
        //导入单条数据
        vm.reloadImport = _fnReloadImport;
        //停止
        vm.stop = _fnStop;



        vm.sampleTypeOptions = items.sampleTypeOptions;
        vm.frozenBoxTypeOptions = items.frozenBoxTypeOptions;
        vm.frozenBoxPlaceOptions = items.frozenBoxPlaceOptions;
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
        // EquipmentAllService.query({},onEquipmentTempSuccess, onError);
        //样本类型
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+value}).isMixed;
                vm.frozenBox.sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                vm.frozenBox.sampleTypeId = value;
                _fnQueryProjectSampleClass(vm.items.projectId,value,vm.isMixed);


            }
        };
        vm.projectSampleTypeConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
                vm.frozenBox.sampleClassificationId = value;
                vm.frozenBox.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                // _fnInitBoxInfo();
                _fnEditBoxInfo();
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
                _fnEditBoxInfo();
            });
        }

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('searching', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('deferRender', true)
            .withOption('scrollY', 260);
        vm.dtColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "auto"),
            DTColumnBuilder.newColumn(1).withOption("width", "auto"),
            DTColumnBuilder.newColumn(2).withOption("width", "80"),
            DTColumnBuilder.newColumn(3).withOption("width", "80"),
            DTColumnBuilder.newColumn(4).withOption("width", "80"),
            DTColumnBuilder.newColumn(5).withOption("width", "60"),
            DTColumnBuilder.newColumn(6).withOption("width", "60")
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
                    if(value.length){
                        vm.codeList = value;
                        _fnInitBoxInfo();
                    }
                    if ($scope.$digest()){
                        $scope.$apply();
                    }
                },300);
            },
            onItemRemove:function (value) {
                _.remove(vm.obox.frozenBoxDTOList,{frozenBoxCode:value});
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
                frozenTubeDTOS:[],
                createDate:new Date().getTime()
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
        //上传
        function _fnUploadSample(file) {
            if (file){
                TransportRecordService.uploadBox(file,vm.items.projectCode).success(function (data) {
                    _.forEach(data,function (box) {
                       box.status = 200;
                       box.createDate = new Date().getTime();
                    });
                    _fnFormatBoxData(data);
                }).error(function (data) {
                    toastr.error(data.message);
                })
            }
        }
        function _fnFormatBoxData(boxes) {
            vm.obox.frozenBoxDTOList = boxes;
            //只取盒子编码
            var frozenBoxCodes = _.map(boxes, 'frozenBoxCode');
            var boxCodeOption = [];
            _.forEach(frozenBoxCodes,function (boxCode) {
                var obj = {};
                obj.text = boxCode;
                obj.value = boxCode;
                boxCodeOption.push(obj)
            });
            vm.boxCodeSelectize.addOption(boxCodeOption);
            vm.boxCodeSelectize.setValue(frozenBoxCodes)
        }
        //导入数据
        vm.progressFlag = false;
        var arrayPromise = [];
        var canceller = null;
        function _fnImportSample() {
            var boxCodes = angular.copy(vm.codeList);
            //导入成功的不再进行导入
            _.forEach(vm.obox.frozenBoxDTOList,function (box) {
               if(box.status == '200'){
                   _.pull(boxCodes,box.frozenBoxCode)
               }
            });
            //无冻存盒数据不能导入
            if(!boxCodes.length){
                return;
            }
            vm.obj = {
                width: 0
            };
            vm.progressFlag = true;
            vm.count = 0;
            vm.errorLen = 0;
            blockUIConfig.autoBlock = false;
            arrayPromise = [];
            canceller = $q.defer();
            _.forEach(boxCodes,function (code) {
                var importData = TransportRecordService.importData(code, vm.frozenBox.sampleTypeId, vm.frozenBox.frozenBoxTypeId, canceller);
                arrayPromise.push(
                    importData.then(function (response) {
                        _.forEach(vm.obox.frozenBoxDTOList,function (box) {
                           if(box.frozenBoxCode == code){
                               box.sampleTypeName = response.data[0].sampleTypeName;
                               box.countOfSample = response.data[0].countOfSample;
                               box.isMixed = response.data[0].isMixed;
                               box.frozenBoxCode1D = response.data[0].frozenBoxCode1D;
                               box.frozenTubeDTOS = response.data[0].frozenTubeDTOS;
                               box.status = response.status;
                           }
                        });
                    },function (data) {
                        var status = data.status;
                        _.forEach(vm.obox.frozenBoxDTOList,function (box) {
                            if(box.frozenBoxCode == code){
                                box.status = status;
                            }
                        });

                    })

                    .finally(function (data) {
                        vm.count++;
                        var percentage = parseInt((vm.count/boxCodes.length)*100);
                        vm.obj.width = percentage+'%';
                        if(vm.obj.width == '100%'){
                            setTimeout(function () {
                                vm.progressFlag = false;
                                blockUIConfig.autoBlock = true;
                            },500)
                        }
                        vm.errorLen = _.filter(vm.obox.frozenBoxDTOList,{status:400}).length;
                    }).$promise
                );

            });
            $q.all(arrayPromise).finally(function(data){

            });
        }
        //单个reload导入数据
        function _fnReloadImport(item) {
            canceller = $q.defer();
            TransportRecordService.importData(item.frozenBoxCode, vm.frozenBox.sampleTypeId, vm.frozenBox.frozenBoxTypeId,canceller).then(function (response) {
                _.forEach(vm.obox.frozenBoxDTOList,function (box) {
                    if(box.frozenBoxCode == item.frozenBoxCode){
                        box.sampleTypeName = response.data[0].sampleTypeName;
                        box.countOfSample = response.data[0].countOfSample;
                        box.isMixed = response.data[0].isMixed;
                        box.frozenBoxCode1D = response.data[0].frozenBoxCode1D;
                        box.frozenTubeDTOS = response.data[0].frozenTubeDTOS;
                        box.status = response.status;
                    }
                });
            },function (data) {
                var status = data.status;
                _.forEach(vm.obox.frozenBoxDTOList,function (box) {
                    if(box.frozenBoxCode == item.frozenBoxCode){
                        box.status = status;
                    }
                });
            })
        }
        //中止导入数据
        function _fnStop() {
            if (canceller){
                canceller.resolve();
            }
            vm.progressFlag = false;
        }
        //初始化冻存盒
        function _fnInitBoxInfo() {
            for(var i = 0; i < vm.codeList.length; i++){
                var box = _fnCreateTempBox(vm.codeList[i]);
                if(!vm.obox.frozenBoxDTOList.length){
                    vm.obox.frozenBoxDTOList.push(box);
                }
                var len = _.filter(vm.obox.frozenBoxDTOList,{frozenBoxCode:vm.codeList[i]}).length;
                if(!len){
                    vm.obox.frozenBoxDTOList.push(box);
                }
            }
            var boxList = angular.copy(vm.obox.frozenBoxDTOList);
            vm.obox.frozenBoxDTOList =  _.orderBy(boxList, ['createDate'], ['desc']);
        }
        //编辑冻存盒
        function _fnEditBoxInfo() {
            var tubeList=[];
            for(var i = 0; i < vm.obox.frozenBoxDTOList.length; i++){
                var box = vm.obox.frozenBoxDTOList[i];
                box.sampleTypeId = vm.frozenBox.sampleTypeId;
                box.sampleTypeCode = vm.frozenBox.sampleTypeCode;
                box.sampleClassificationId = vm.frozenBox.sampleClassificationId;
                box.sampleClassificationCode = vm.frozenBox.sampleClassificationCode;
                box.frozenBoxTypeId = vm.frozenBox.frozenBoxTypeId;
                box.frozenBoxTypeRows = vm.frozenBox.frozenBoxTypeRows;
                box.frozenBoxTypeColumns = vm.frozenBox.frozenBoxTypeColumns;
                box.equipmentId = vm.frozenBox.equipmentId;
                box.areaId = vm.frozenBox.areaId;
                box.isMixed = vm.isMixed;
                box.frozenTubeDTOS = [];
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
            }
        }

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            //全部为成功的数据
            var len = _.filter(vm.obox.frozenBoxDTOList,{status:200}).length;
            if(len != vm.obox.frozenBoxDTOList.length){
                if(vm.frozenBox.sampleTypeCode == '99' &&  !vm.projectSampleTypeOptions.length){
                    toastr.error("样本类型为99时，无样本分类，不能入库！");
                    return;
                }
            }
            blockUI.start("正在保存冻存盒中……");
            TranshipBoxService.save(vm.obox,onSaveBoxSuccess,onError);
        };

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
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();


