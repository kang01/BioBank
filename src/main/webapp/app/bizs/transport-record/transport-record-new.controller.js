/**
 * Created by gaokangkang on 2017/3/14.
 * status 3001：正常，3002：空管，3003：空孔；3004：异常 样本状态
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController)
        .controller('BoxInstanceCtrl',BoxInstanceCtrl);

    TransportRecordNewController.$inject = ['$scope','blockUI','MasterData','hotRegisterer','SampleService','TranshipInvalidService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','$stateParams','toastr','entity','frozenBoxByCodeService','TransportRecordService','TranshipSaveService','TranshipBoxService',
        'SampleTypeService','FrozenBoxTypesService','StockInInputService','EquipmentAllService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService','TranshipBoxByCodeService','TranshipStockInService','FrozenBoxDelService','SampleUserService','TrackNumberService',
    'BioBankBlockUi','Principal'];
    BoxInstanceCtrl.$inject = ['$uibModalInstance'];
    function TransportRecordNewController($scope,blockUI,MasterData,hotRegisterer,SampleService,TranshipInvalidService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,$stateParams,toastr,entity,frozenBoxByCodeService,TransportRecordService,TranshipSaveService,TranshipBoxService,
                                          SampleTypeService,FrozenBoxTypesService,StockInInputService,EquipmentAllService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService,TranshipBoxByCodeService,TranshipStockInService,FrozenBoxDelService,SampleUserService,TrackNumberService,
                                          BioBankBlockUi,Principal) {

        var modalInstance;
        var vm = this;
        vm.transportRecord = entity; //转运记录
        vm.box = {};
        //生成的icon配置
        vm.btnSettings = {
            icon:"fa-plus-circle",
            makeNewBoxCode:_fnMakeNewBoxCode
        };
        //生成新的冻存盒号
        vm.makeNewBoxCode = _fnMakeNewBoxCode;
        //
        vm.makeNewBoxCodeFlag = false;
        function _fnMakeNewBoxCode() {
            if(!vm.box.sampleTypeId || !vm.box.sampleClassificationId){
                return;
            }
            StockInInputService.makeNewBoxCode(vm.transportRecord.projectId,vm.box.sampleTypeId,vm.box.sampleClassificationId).success(function (data) {
                vm.box.frozenBoxCode = data.code;
                vm.makeNewBoxCodeFlag = true;
            }).error(function (data) {
                toastr.error(data.message);
            })
        }

        _initTransportRecordPage();
        _initFrozenBoxesTable();
        _initFrozenBoxPanel();
        //样本数统计
        function _sampleCount(tubeList) {
            //正常
            vm.normalCount =  _.filter(tubeList,{'status':'3001'}).length;
            //空管
            vm.emptyPipeCount =  _.filter(tubeList,{'status':'3002'}).length;
            //空孔
            vm.emptyHoleCount =  _.filter(tubeList,{'status':'3003'}).length;
            //异常
            vm.abnormalCount =  _.filter(tubeList,{'status':'3004'}).length;

            // vm.transportRecord.emptyTubeNumber = vm.emptyPipeCount;
            // vm.transportRecord.emptyHoleNumber = vm.emptyHoleCount;
            if(!$scope.$$phase) {
                $scope.$apply();
            }
        }
        function _fnQueryUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
                if(vm.account.login != "admin" || vm.account.login != "user"){
                    vm.transportRecord.receiverId = vm.account.id;
                }
            });
        }
        function _initTransportRecordPage(){
            if($stateParams.transhipId){
                vm.transportRecord.id = $stateParams.transhipId;
            }

            if($stateParams.transhipCode){
                vm.transportRecord.transhipCode = $stateParams.transhipCode;
            }
            //项目点 项目编码
            if($stateParams.projectId){
                vm.transportRecord.projectId = $stateParams.projectId;
            }
            if($stateParams.projectSiteId){
                vm.transportRecord.projectSiteId = $stateParams.projectSiteId;
            }
            // 设置默认值
            if(vm.transportRecord.transhipDate){
                vm.transportRecord.transhipDate = new Date(entity.transhipDate);
            }else{
                vm.transportRecord.transhipDate = new Date();
            }
            if(vm.transportRecord.receiveDate){
                vm.transportRecord.receiveDate = new Date(entity.receiveDate);
            }else{
                vm.transportRecord.receiveDate = new Date();
            }
            if(!vm.transportRecord.receiverId){
                _fnQueryUser();
            }
            if(!vm.transportRecord.sampleSatisfaction){
                vm.transportRecord.sampleSatisfaction = 8;
            }
            vm.transportRecord.transhipBatch = +vm.transportRecord.transhipBatch;
            _fnQuerySampleType();

            if(vm.transportRecord.projectId){
                ProjectSitesByProjectIdService.query({id:vm.transportRecord.projectId},onProjectSitesSuccess,onError);
            }
            if(vm.transportRecord.tempEquipmentId){
                AreasByEquipmentIdService.query({id:vm.transportRecord.tempEquipmentId},onTransportRecordAreaSuccess, onError);
            }
            //盒子类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            //项目
            ProjectService.query({},onProjectSuccess, onError);
            //接收人
            SampleUserService.query({},onReceiverSuccess, onError);

            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                maxItems: 1,
                searchField:'projectName',
                onChange:function(value){
                    for(var i = 0; i < vm.projectOptions.length;i++){
                        if(value == vm.projectOptions[i].id){
                            vm.transportRecord.projectCode = vm.projectOptions[i].projectCode;
                            vm.transportRecord.projectName = vm.projectOptions[i].projectName;
                        }
                    }
                    vm.transportRecord.projectSiteId = "";
                    if(value){
                        ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError);
                    }
                }
            };
            //项目点
            vm.projectSitesConfig = {
                valueField:'id',
                labelField:'projectSiteName',
                searchField:'projectSiteName',
                maxItems: 1,
                onChange:function (value) {
                    for(var i = 0; i < vm.projectSitesOptions.length;i++){
                        if(value == vm.projectSitesOptions[i].id){
                            vm.transportRecord.projectSiteCode = vm.projectSitesOptions[i].projectSiteCode;
                            vm.transportRecord.projectSiteName = vm.projectSitesOptions[i].projectSiteName;
                        }
                    }
                }
            };
            // 满意度1-10
            vm.satisfactionOptions = [
                {id:"10",name:"非常满意"},
                {id:"9",name:"较满意"},
                {id:"8",name:"满意"},
                {id:"7",name:"有少量空管"},
                {id:"6",name:"有许多空管"},
                {id:"5",name:"有大量空管"},
                {id:"4",name:"有少量空孔"},
                {id:"3",name:"有少量错位"},
                {id:"2",name:"有大量错位"},
                {id:"1",name:"非常不满意"}
            ];
            vm.satisfactionConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1
            };

            //转运状态
            vm.statusOptions = MasterData.transportStatus;
            vm.statusConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1

            };
            //接收人
            vm.receiverConfig = {
                valueField:'id',
                labelField:'userName',
                maxItems: 1

            };

            vm.datePickerOpenStatus = {};
            vm.openCalendar = openCalendar; //时间
            vm.importFrozenStorageBox = importFrozenStorageBox; //导入冻存盒
            vm.fnQueryProjectSampleClass = _fnQueryProjectSampleClass;
            //作废
            vm.invalid = function () {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/common/prompt-content-modal.html',
                    size: 'md',
                    controller: 'PromptContentModalController',
                    controllerAs: 'vm',
                    backdrop:'static',
                    resolve: {
                        items: function () {
                            return {
                                status:'2'
                            };
                        }
                    }
                });
                modalInstance.result.then(function (invalidReason) {
                    var obj = {
                        invalidReason:invalidReason
                    };
                    TranshipInvalidService.invalid(vm.transportRecord.transhipCode,obj).success(function () {
                        toastr.success("已作废！");
                        $state.go('transport-record');
                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }, function () {
                });

            };
            //为提示框的判断
            vm.saveStockInFlag = false;
            vm.saveRecordFlag = false;
            //保存记录
            vm.saveRecord = saveRecord;
            //转运完成
            vm.transferFinish = function () {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/modal/stock-in-affirm-modal.html',
                    controller: 'StockInAffirmModalController',
                    backdrop:'static',
                    controllerAs: 'vm',
                    resolve:{
                        items:function () {
                            return{
                                box:vm.box || {},
                                receiverId:vm.transportRecord.receiverId,
                                receiveDate: vm.transportRecord.receiveDate
                            };
                        }
                    }
                });
                modalInstance.result.then(function (transportRecord) {
                    vm.saveStockInFlag = true;
                    importBoxFlag = false;
                    vm.saveRecord(transportRecord);
                    var  array = _.flattenDeep(vm.frozenTubeArray);
                    var array1 = [];
                    for(var i = 0; i < array.length; i++){
                        if(array[i]){
                            array1.push(array[i]);
                        }
                    }
                    if(array1.length){
                        vm.saveBox();
                    }

                });

            };
            //为true时，导入冻存盒
            var importBoxFlag = false;
            function importFrozenStorageBox() {
                importBoxFlag = true;
                saveRecord();

            }
            //保存记录
            function saveRecord(transportRecord) {
                vm.saveRecordFlag = true;
                //导入冻存盒
                if(!importBoxFlag){
                    BioBankBlockUi.blockUiStart();
                }

                TranshipSaveService.update(vm.transportRecord,onSaveTranshipRecordSuccess,onError);
                function onSaveTranshipRecordSuccess(data) {
                    //导入冻存盒
                    if(importBoxFlag){
                        modalInstance = $uibModal.open({
                            animation: true,
                            templateUrl: 'app/bizs/transport-record/modal/frozen-storage-box-modal.html',
                            controller: 'FrozenStorageBoxModalController',
                            controllerAs:'vm',
                            size:'lg w-1200',
                            backdrop:'static',
                            resolve: {
                                items: function () {
                                    return {
                                        transhipId :vm.transportRecord.id,
                                        projectId :vm.transportRecord.projectId,
                                        projectCode:vm.transportRecord.projectCode,
                                        frozenBoxTypeOptions:vm.frozenBoxTypeOptions,
                                        sampleTypeOptions:vm.sampleTypeOptions,
                                        frozenBoxPlaceOptions:vm.frozenBoxPlaceOptions
                                    };
                                }
                            }

                        });
                        modalInstance.result.then(function (data) {
                            vm.queryTransportRecord();
                            vm.dtInstance.rerender();
                            importBoxFlag = false;
                        },function () {
                            importBoxFlag = false;
                        })
                    }
                    vm.saveRecordFlag = false;
                    BioBankBlockUi.blockUiStop();
                    if(vm.saveStockInFlag){
                        TranshipStockInService.saveTransferFinish(vm.transportRecord.transhipCode,transportRecord).success(function (data) {
                            BioBankBlockUi.blockUiStop();
                            toastr.success("转运成功！");
                            vm.saveStockInFlag = false;
                            $state.go('transport-record');
                        }).error(function (data) {
                            BioBankBlockUi.blockUiStop();
                            toastr.error(data.message+"转运失败！");
                        });
                    }
                    if(!vm.saveStockInFlag && !importBoxFlag){
                        toastr.success("保存转运记录成功");
                        vm.queryTransportRecord();
                    }
                }
                // vm.saveBox(function(){
                //
                // });
            }

            function openCalendar (date) {
                vm.datePickerOpenStatus[date] = true;
            }

            //盒子类型
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['esc']);
            }
            //项目编码
            function onProjectSuccess(data)  {
                vm.projectOptions = data;
                if(vm.projectOptions.length){
                    if(!vm.transportRecord.projectId){
                        vm.transportRecord.projectId = data[0].id;
                    }
                    ProjectSitesByProjectIdService.query({id:vm.transportRecord.projectId},onProjectSitesSuccess,onError);
                }
                }
            //获取样本类型
            function _fnQuerySampleType() {
                SampleTypeService.querySampleType().success(function (data) {
                    vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeId'], ['asc']);
                    _.remove(vm.sampleTypeOptions,{sampleTypeName:"98"});
                    _.remove(vm.sampleTypeOptions,{sampleTypeName:"97"});
                    // _.forEach(vm.sampleTypeOptions,function (value) {
                    //     if(value.sampleTypeCode == 'RNA'){
                    //         vm.isMixedFlag = true;
                    //     }
                    // });

                });
            }
            //不同项目下的样本分类
            //样本分类为混合类型时，要分装
            vm.isMixedFlag = false;
            function _fnQueryProjectSampleClass(projectId,sampleTypeId,isMixed) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    vm.box.sampleClassificationId = null;
                    vm.box.sampleClassificationCode = null;
                    vm.box.sampleClassificationName = null;
                    //是否混合类型 1：是混合类型
                    if(isMixed == 1){
                        vm.box.isSplit = 1;
                        vm.isMixedFlag = true;
                        //类型下有无分类
                        if(data.length){
                            for(var k = 0; k < data.length; k++){
                                for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                                    for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                        if(data[k].columnsNumber == j+1){
                                            vm.frozenTubeArray[i][j].sampleClassificationId = data[k].sampleClassificationId;
                                            vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                            vm.frozenTubeArray[i][j].frontColor = vm.box.frontColor;
                                        }
                                    }
                                }
                                for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                                    if(vm.box.frozenTubeDTOS[m].tubeColumns == data[k].columnsNumber){
                                        vm.box.frozenTubeDTOS[m].sampleClassificationId = data[k].sampleClassificationId;
                                        vm.box.frozenTubeDTOS[m].frontColor = vm.box.frontColor;
                                    }
                                }
                            }

                        }else{
                            //混合型无分类
                            for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                                for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                        vm.frozenTubeArray[i][j].sampleClassificationId = "";
                                        vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                }

                            }
                        }

                    }else{
                        //无分类时取第一个

                        if(vm.projectSampleTypeOptions.length){
                            vm.box.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                            vm.box.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                            vm.box.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                            vm.box.sampleClassification = vm.projectSampleTypeOptions[0];
                        }
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(vm.box.sampleClassification){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassificationId;
                                    vm.frozenTubeArray[i][j].frontColor = vm.box.sampleClassification.frontColor;
                                }
                                if(vm.frozenTubeArray[i][j]){
                                    vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                    vm.frozenTubeArray[i][j].frontColor = vm.box.frontColor;
                                }
                            }
                        }
                        for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                            if(vm.box.frozenTubeDTOS[m].sampleClassification){
                                vm.box.frozenTubeDTOS[m].sampleClassification.id = vm.box.sampleClassificationId;
                                vm.box.frozenTubeDTOS[m].frontColor = vm.box.sampleClassification.frontColor;
                            }else{
                                vm.box.frozenTubeDTOS[m].sampleClassificationId = vm.box.sampleClassificationId;
                                vm.box.frozenTubeDTOS[m].frontColor = vm.box.frontColor;
                            }
                            if(vm.box.frozenTubeDTOS[m].sampleType){
                                vm.box.frozenTubeDTOS[m].sampleType.id = vm.box.sampleTypeId;
                                vm.box.frozenTubeDTOS[m].frontColor = vm.box.frozenTubeDTOS[m].sampleType.frontColor;

                            }else{
                                vm.box.frozenTubeDTOS[m].sampleTypeId = vm.box.sampleTypeId;
                                vm.box.frozenTubeDTOS[m].frontColor = vm.box.frontColor;
                            }
                        }
                    }

                    hotRegisterer.getInstance('my-handsontable').render();
                });
            }
            // 项目点
            function onProjectSitesSuccess(data) {
                vm.projectSitesOptions = data;
                if(!vm.transportRecord.projectSiteId){
                    if(data.length){
                        vm.transportRecord.projectSiteId = data[0].id;
                    }
                }

            }
            function onReceiverSuccess(data) {
                vm.receiverOptions = data;
            }
        }
        //左侧冻存盒
        vm.dtInstance = {};
        function _initFrozenBoxesTable(){
            // vm.loadBox = loadBox;


            vm.dtColumns = [
                DTColumnBuilder.newColumn('frozenBoxId').withOption("width", "50").notSortable().withOption('searchable',false).withTitle('#'),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号').renderWith(_fnRowRender)
            ];
            vm.dtOptions = DTOptionsBuilder.newOptions()
                // .withOption('order', [[1,'asc']])
                .withOption('info', false)
                .withOption('paging', false)
                .withScroller()
                .withOption('scrollY', 400)
                .withOption('rowCallback', rowCallback)
                .withOption('serverSide',true)
                .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                    var data = {};
                    for(var i=0; aoData && i<aoData.length; ++i){
                        var oData = aoData[i];
                        data[oData.name] = oData.value;
                    }
                    var jqDt = this;
                    if(vm.transportRecord.transhipCode){
                        TranshipBoxByCodeService.queryByCodes(vm.transportRecord.transhipCode,data).then(function (res){
                            var json = res.data;
                            vm.boxLength = res.data.data.length;
                            var error = json.error || json.sError;
                            if ( error ) {
                                jqDt._fnLog( oSettings, 0, error );
                            }
                            oSettings.json = json;
                            fnCallback( json );
                        }).catch(function(res){
                            console.log(res);

                            var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );

                            if ( $.inArray( true, ret ) === -1 ) {
                                if ( error == "parsererror" ) {
                                    jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
                                }
                                else if ( res.readyState === 4 ) {
                                    jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
                                }
                            }

                            jqDt._fnProcessingDisplay( oSettings, false );
                        });

                    }
                });

            function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
                $('td:first', nRow).html(iDisplayIndex+1);
                $('td', nRow).unbind('click');
                $(nRow).bind('click', function() {
                    var tr = this;
                    $scope.$apply(function () {
                        someClickHandler(tr,oData);
                    });
                });
                if (vm.rowBoxCode == oData.frozenBoxCode){
                    $(nRow).addClass('rowLight');
                }
                return nRow;
            }

            //点击冻存盒行
            function someClickHandler(tr,boxInfo) {
                vm.makeNewBoxCodeFlag = false;
                vm.flagStatus = false;
                vm.rowBoxCode = boxInfo.frozenBoxCode;
                vm.strbox = JSON.stringify(vm.createBoxDataFromTubesTable());
                if(!vm.boxStr || vm.strbox === vm.boxStr){
                    $(tr).closest('table').find('.rowLight').removeClass("rowLight");
                    $(tr).addClass('rowLight');
                    frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},vm.onFrozenSuccess,onError);

                }else{
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'boxModal.html',
                        size: 'sm',
                        controller: 'BoxInstanceCtrl',
                        controllerAs: 'ctrl'
                    });
                    modalInstance.result.then(function (flag) {
                        vm.repeatSampleArray = [];
                        $(tr).closest('table').find('.rowLight').removeClass("rowLight");
                        $(tr).addClass('rowLight');
                        //true:保存 false:不保存
                        if(flag){
                            vm.saveBox(function(res){
                                frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},vm.onFrozenSuccess,onError);
                            });
                        } else {
                            frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},vm.onFrozenSuccess,onError);
                        }
                    }, function () {
                    });
                }

            }

            function _fnRowRender(data, type, full, meta) {
                var frozenBoxCode = '';
                if(full.frozenBoxCode1D){
                    frozenBoxCode = "1D:"+full.frozenBoxCode1D;
                }else{
                    frozenBoxCode = "2D:"+full.frozenBoxCode;
                }
                return frozenBoxCode;
            }

        }
        //初始化冻存管
        function _initFrozenBoxPanel(){
            vm.frozenTubeArray = [];//初始管子数据二位数组
            //重复的样本编码
            vm.repeatSampleArray = [];
            var remarkArray;//批注
            var domArray = [];//单元格操作的数据
            var operateColor;//单元格颜色

            EquipmentAllService.query({},onEquipmentSuccess, onError);//设备

            initFrozenTube(10,10);
            vm.nextFlag = true;
            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                data:vm.frozenTubeArray,
                renderer: myCustomRenderer,
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 30,
                rowHeaderWidth: 30,
                editor: 'tube',
                multiSelect: true,
                comments: true,
                manualColumnResize:false,
                outsideClickDeselects:true,
                onAfterSelectionEnd:function (row, col, row2, col2) {
                    vm.remarkFlag = true;
                    var td = this;
                    remarkArray = this.getData(row,col,row2,col2);
                    var selectTubeArrayIndex = this.getSelected();
                    var array = _.flatten(remarkArray);

                    if(window.event && window.event.ctrlKey){
                        //换位
                        vm.exchangeFlag = true;
                        domArray.push(vm.frozenTubeArray[row][col]);

                        //备注
                        _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex);
                    }else{
                        if(array.length == 2){
                            vm.exchangeFlag = true;
                            domArray = [];
                            for(var i = 0; i < array.length; i++){
                                domArray.push(array[i])
                            }
                        }
                        if(array.length == 1){
                            domArray = [];
                            domArray.push(array[0])
                        }
                        //备注
                        $(".temp").remove();
                        aRemarkArray = [];
                        _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex);


                    }
                    //管子
                    if(vm.flagStatus){
                        var tubeStatus = $(this.getCell(row, col)).find(".tube-status").text();
                        var cellData = vm.frozenTubeArray[row][col];

                        if(tubeStatus == 3001){
                            cellData.status = 3002;
                        }
                        if(tubeStatus == 3002){
                            cellData.status = 3003;
                        }
                        if(tubeStatus == 3003){
                            cellData.status = 3004;
                        }
                        if(tubeStatus == 3004){
                            cellData.status = 3001;
                        }
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
                },
                enterMoves:function () {
                    if(vm.nextFlag){
                        if(vm.isMixedFlag){
                            return{row:1,col:0};
                        }else{
                            var hotMoves = hotRegisterer.getInstance('my-handsontable');
                            var selectedCol = hotMoves.getSelected()[1];
                            if(selectedCol + 1 < hotMoves.countCols()){
                                return{row:0,col:1};
                            } else{
                                return{row:1,col:-selectedCol};
                            }
                        }
                    }else{
                        return{row:0,col:0};
                    }



                },
                afterChange:function (change,source) {
                    if(source == 'edit'){
                        var rowCount = +vm.box.frozenBoxType.frozenBoxTypeRows;
                        var colCount = +vm.box.frozenBoxType.frozenBoxTypeColumns;
                        if(vm.box.sampleTypeCode == "99"){
                            var rowIndex = change[0][0];
                            var oldTubeObject = change[0][2];
                            if(oldTubeObject.sampleClassificationId){
                                for(var m = 0; m < vm.frozenTubeArray.length; m++){
                                    for(var n = 0; n < vm.frozenTubeArray[m].length ; n++){
                                        if(oldTubeObject.sampleCode){
                                            vm.frozenTubeArray[rowIndex][n].sampleCode = oldTubeObject.sampleCode;
                                        }
                                    }

                                }
                                hotRegisterer.getInstance('my-handsontable').render();
                            }else{
                                vm.nextFlag = true;
                                vm.isMixedFlag = false;
                            }

                        }

                        for (var i=0; i<change.length; ++i){
                            var item = change[i];
                            var row = item[0];
                            var col = item[1];
                            var oldTube = item[2];
                            var newTube = item[3];
                            if (oldTube.id
                                || (oldTube.sampleCode && oldTube.sampleCode.length > 1)
                                || (oldTube.sampleTempCode && oldTube.sampleTempCode.length > 1)){

                                // When delete a tube.
                                if (newTube === ""){
                                    newTube = angular.copy(oldTube);
                                    newTube.id = null;
                                    newTube.sampleCode = "";
                                    newTube.sampleTempCode = "";
                                    hotRegisterer.getInstance('my-handsontable').setDataAtCell(row, col, newTube);
                                }
                            }



                        }

                        return;
                    }
                },
                beforeKeyDown:function (event) {
                    if(vm.flagStatus){
                        if(event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){
                            Handsontable.Dom.stopImmediatePropagation(event);
                        }
                    }

                },
                beforeChange: function(changes, source) {
                    var newTubeObject = changes[0][3];
                    var oldTubeObject = changes[0][2];
                    var rowIndex = changes[0][0];
                    var colIndex = changes[0][1];
                    if(newTubeObject && newTubeObject.sampleTempCode){
                        for(var m = 0; m < vm.frozenTubeArray.length; m++){
                            for(var n = 0; n < vm.frozenTubeArray[m].length ; n++){
                                if(m == rowIndex && n == colIndex){
                                    continue;
                                }
                                if(vm.frozenTubeArray[m][n].sampleCode){
                                    if(vm.frozenTubeArray[m][n].sampleCode == oldTubeObject.sampleCode){
                                        toastr.error("冻存管编码不能重复!");
                                        vm.nextFlag = false;
                                        return false;
                                    }else{
                                        vm.nextFlag = true;
                                    }
                                }

                            }

                        }
                    }

                },
                beforeOnCellMouseDown: function (event, coords, element) {
                    var self = this;
                    if(coords.row == "-1" && coords.col == "-1" && $(element).is("th")){
                        var row2 = this.countRows()-1;
                        var col2 = this.countCols()-1;
                        setTimeout(function(){
                            self.selectCell(0,0,row2,col2,true,true);
                        },200);
                    }
                }

            };

            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = 'relative';
                td.style.color = tube.frontColor;
                cellProperties.comment = {};
                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = {value:tube.memo};
                }
                //样本类型
                if(tube.sampleClassificationId){
                    SampleService.changeSampleType(tube.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                }else{
                    if(vm.sampleTypeOptions){
                        SampleService.changeSampleType(tube.sampleTypeId,td,vm.sampleTypeOptions,2);
                    }
                }

                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(tube.status){
                    changeSampleStatus(tube.status,row,col,td,cellProperties);
                }

                var code = tube.sampleCode && tube.sampleCode != " " ? tube.sampleCode : tube.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word'
                }).appendTo(td);
                $div = $("<div  class='tube-status'/>").html(tube.status).appendTo(td);
                if(vm.repeatSampleArray.length){
                    var len;
                    if(tube.sampleCode){
                        len = _.filter(vm.repeatSampleArray,{sampleCode:tube.sampleCode}).length;
                    }else{
                        len = _.filter(vm.repeatSampleArray,{sampleCode:tube.sampleTempCode}).length;
                    }
                    if(len){
                        $div = $("<div class='repeat-sample-class stockInAnimation'/>").appendTo(td);
                    }
                }
            }
            //修改样本状态正常、空管、空孔、异常
            function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {

                operateColor = td.style.backgroundColor;
                //正常
                if(sampleStatus == 3001){
                    $(td).removeClass("error-tube-color");
                }
                //空管
                if(sampleStatus == 3002){
                    $(td).addClass("empty-tube-color");
                    // td.style.background = 'linear-gradient(to right,'+operateColor+',50%,black';
                }
                //空孔
                if(sampleStatus == 3003){
                    $(td).removeClass("empty-tube-color");
                    $(td).addClass("empty-hole-color");
                    // td.style.background = '';
                    // td.style.backgroundColor = '#ffffff';
                    // td.style.color = '#ffffff';
                }
                //异常
                if(sampleStatus == 3004){
                    $(td).removeClass("empty-hole-color");
                    $(td).addClass("error-tube-color");
                    // td.style.backgroundColor = 'red';
                    // td.style.border = '3px solid red;margin:-3px';
                }
            }
            var aRemarkArray = [];
            //备注 选择单元格数据
            function _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex) {
                var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px dashed #5292F7;background-color: rgba(82,146,247,0.2)"></div>';
                for(var m = 0; m < remarkArray.length; m++){
                    for (var n = 0; n < remarkArray[m].length; n++){
                        if ((remarkArray[m][n].sampleCode && remarkArray[m][n].sampleCode.length > 1)
                            || (remarkArray[m][n].sampleTempCode && remarkArray[m][n].sampleTempCode.length > 1)){
                            aRemarkArray.push(remarkArray[m][n]);
                        }
                    }
                }
                var start1,end1,start2,end2;
                if(selectTubeArrayIndex[0] > selectTubeArrayIndex[2]){
                    start1 = selectTubeArrayIndex[2];
                    end1 = selectTubeArrayIndex[0];
                }else{
                    start1 = selectTubeArrayIndex[0];
                    end1 = selectTubeArrayIndex[2];
                }
                if(selectTubeArrayIndex[1] > selectTubeArrayIndex[3]){
                    start2 = selectTubeArrayIndex[3];
                    end2 = selectTubeArrayIndex[1];
                }else{
                    start2 = selectTubeArrayIndex[1];
                    end2 = selectTubeArrayIndex[3];
                }
                for(var i = start1;i <= end1; i++){
                    for(var j = start2;  j <= end2;j++)
                        if($(td.getCell(i,j))[0].childElementCount !=3){
                            $(td.getCell(i,j)).append(txt);
                        }

                }
            }
            //修改样本状态
            vm.flagStatus = false;
            vm.editStatus = function () {
                var settings = {
                    editor: vm.flagStatus ? false : 'tube'
                };
                hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
            };
            //换位
            vm.exchangeFlag = false;
            vm.exchange = function () {
                // toastr.success("两个空冻存盒不能被交换!");

                if(vm.exchangeFlag && domArray.length == 2){
                    var v1 = (!domArray[0].sampleCode && !domArray[1].sampleCode);
                    var v2 = (!domArray[0].sampleTempCode && !domArray[1].sampleTempCode);
                    if((!domArray[0].sampleCode && !domArray[1].sampleCode)
                        && (!domArray[0].sampleTempCode && !domArray[1].sampleTempCode)){
                        toastr.error("两个空冻存盒不能被交换!");
                        aRemarkArray = [];
                        return;
                    }
                    var row = getTubeRowIndex(domArray[0].tubeRows);
                    var col = getTubeColumnIndex(domArray[0].tubeColumns);
                    var row1 = getTubeRowIndex(domArray[1].tubeRows);
                    var col1 = getTubeColumnIndex(domArray[1].tubeColumns);
                    if(row > 8 ){
                        vm.frozenTubeArray[row-1][col] = domArray[1];
                        vm.frozenTubeArray[row-1][col].tubeRows = getTubeRows(row);
                        vm.frozenTubeArray[row-1][col].tubeColumns = getTubeColumns(col);

                    }else{
                        vm.frozenTubeArray[row][col] = domArray[1];
                        vm.frozenTubeArray[row][col].tubeRows = getTubeRows(row);
                        vm.frozenTubeArray[row][col].tubeColumns = getTubeColumns(col);
                    }
                    if(row1 > 8){
                        vm.frozenTubeArray[row1-1][col1] = domArray[0];
                        vm.frozenTubeArray[row1-1][col1].tubeRows = getTubeRows(row1);
                        vm.frozenTubeArray[row1-1][col1].tubeColumns = getTubeColumns(col1);
                    }else{
                        vm.frozenTubeArray[row1][col1] = domArray[0];
                        vm.frozenTubeArray[row1][col1].tubeRows = getTubeRows(row1);
                        vm.frozenTubeArray[row1][col1].tubeColumns = getTubeColumns(col1);
                    }


                    domArray = [];
                    aRemarkArray = [];
                    vm.exchangeFlag = false;

                }else{
                    toastr.error("只能选择两个进行交换！",{},'center');
                    domArray = [];
                    aRemarkArray = [];
                }
                hotRegisterer.getInstance('my-handsontable').render();
            };
            //批注
            vm.remarkFlag = false;
            vm.tubeRemark = function () {
                if(aRemarkArray.length > 0){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/transport-record/modal/tubes-remark-modal.html',
                        controller: 'TubesRemarkModalController',
                        backdrop:'static',
                        controllerAs: 'vm',
                        resolve: {
                            items: function () {
                                return {
                                    remarkArray :aRemarkArray
                                };
                            }
                        }

                    });
                    modalInstance.result.then(function (memo) {
                        for(var i = 0; i < aRemarkArray.length; i++){
                            if(aRemarkArray[i].sampleCode || aRemarkArray[i].sampleTempCode){
                                aRemarkArray[i].memo = memo;
                            }
                        }
                        aRemarkArray = [];
                        hotRegisterer.getInstance('my-handsontable').render();
                    });
                }else{
                    toastr.error("请选择要备注的冻存管!");
                }
            };

            function initFrozenTube(row,col) {
                for(var i = 0; i < row; i++){
                    vm.frozenTubeArray[i] = [];
                    for(var j = 0;j < col; j++){
                        vm.frozenTubeArray[i][j] = "";
                    }
                }
            }
            function getTubeRows(row) {
                return String.fromCharCode(row + 65);
            }
            function getTubeColumns(col) {
                return col + 1;
            }
            function getTubeRowIndex(row) {
                return row.charCodeAt(0) -65;
            }
            function getTubeColumnIndex(col) {
                return +col -1;
            }

            // 创建一个对象用于管子Table的控件
            function _createTubeForTableCell(tubeInBox, box, rowNO, colNO, pos){
                var tube = {
                    id: null,
                    sampleCode: "",
                    sampleTempCode: "",
                    sampleTypeId: box.sampleType.id,
                    frozenBoxId: "",
                    frozenBoxCode: box.frozenBoxCode,
                    status: "",
                    memo: "",
                    tubeRows: pos.tubeRows,
                    tubeColumns: pos.tubeColumns,
                    frontColor:null,
                    rowNO: rowNO,
                    colNO: colNO
                };
                if(box.sampleClassificationId){
                    tube.sampleClassificationId = box.sampleClassificationId;
                }
                if (tubeInBox){
                    tube.id = tubeInBox.id;
                    tube.sampleCode = tubeInBox.sampleCode;
                    tube.sampleTempCode = tubeInBox.sampleTempCode;
                    tube.sampleTypeId = box.sampleTypeId;
                    tube.sampleTypeCode = tubeInBox.sampleTypeCode;
                    tube.status = tubeInBox.status;
                    tube.memo = tubeInBox.memo;
                    tube.frozenBoxId = tubeInBox.frozenBoxId;
                    tube.frontColor = tubeInBox.frontColor;
                    if(tubeInBox.sampleClassification){
                        tube.sampleClassificationId = tubeInBox.sampleClassification.id;
                    }
                }

                return tube;
            }
            // 重新加载管子表控件
            function _reloadTubesForTable(box){
                var tableCtrl = hotRegisterer.getInstance('my-handsontable');
                var tableWidth = $(tableCtrl.container).width();
                var settings = {
                    minCols: +box.frozenBoxType.frozenBoxTypeColumns,
                    minRows: +box.frozenBoxType.frozenBoxTypeRows
                };

                var rowHeaderWidth = 30;
                // 架子定位列表每列的宽度
                var colWidth = (tableWidth - rowHeaderWidth) / settings.minCols;

                // colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                // rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],

                var tubesInTable = [];
                var colHeaders = [];
                var rowHeaders = [];
                for (var i=0; i<settings.minRows; ++i){
                    var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                    if(i > 7){
                        pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
                    }
                    var tubes = [];
                    rowHeaders.push(pos.tubeRows);
                    for (var j = 0; j < settings.minCols; ++j){
                        pos.tubeColumns = j + 1 + "";
                        if (colHeaders.length < settings.minCols){
                            colHeaders.push(pos.tubeColumns);
                        }
                        var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                        var tube = _createTubeForTableCell(tubeInBox, box, i, j + 1, pos);
                        //混合类型
                        if(isMixed == "1"){
                            for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                                if (vm.projectSampleTypeOptions[l].columnsNumber == pos.tubeColumns) {
                                    if(!tube.sampleClassificationId){
                                        tube.sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                                    }

                                }
                            }
                        }
                        tubes.push(tube);
                    }
                    tubesInTable.push(tubes);
                }

                vm.frozenTubeArray = tubesInTable;

                settings.rowHeaders = rowHeaders;
                settings.colHeaders = colHeaders;
                settings.colWidths = colWidth;
                settings.manualColumnResize = colWidth;
                tableCtrl.updateSettings(settings);
                tableCtrl.loadData(tubesInTable);
                var tubeList = [];
                for(var m = 0; m < tubesInTable.length;m++){
                    for(var n = 0; n < tubesInTable[m].length;n++){
                        tubeList.push(tubesInTable[m][n]);
                    }
                }

                _sampleCount(tubeList);

            }
            vm.createBoxDataFromTubesTable = _createBoxDataFromTubesTable;
            function _createBoxDataFromTubesTable(){
                if (!vm.box){
                    return null;
                }
                var box = angular.copy(vm.box)||{};
                delete box.frozenBoxType;
                delete box.sampleClassification;
                delete box.sampleType;
                var tubes = hotRegisterer.getInstance('my-handsontable').getData();
                box.frozenTubeDTOS = [];

                for(var i=0; i<tubes.length; ++i){
                    var rowTubes = tubes[i];
                    for (var j=0; j<rowTubes.length; ++j){
                        var tube = angular.copy(rowTubes[j]);
                        delete tube.rowNO;
                        delete tube.colNO;

                        if (tube.id
                            || (tube.sampleCode && tube.sampleCode.length > 1)
                            || (tube.sampleTempCode && tube.sampleTempCode.length > 1)){
                            box.frozenTubeDTOS.push(tube);
                        }
                    }
                }

                return box;
            }


            //盒子类型 17:10*10 18:8*8
            vm.boxTypeInstance = {};
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onInitialize: function(selectize){
                    vm.boxTypeInstance = selectize;
                },
                onChange:function(value) {
                    var boxType = _.filter(vm.frozenBoxTypeOptions, {id: +value})[0];
                    if (!boxType) {
                        return;
                    }
                    vm.box.frozenBoxTypeId = value;
                    vm.box.frozenBoxType.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                    vm.box.frozenBoxType.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;
                    var frozenTubeDTOS = angular.copy(vm.box.frozenTubeDTOS);
                    vm.box.frozenTubeDTOS = [];
                    var tubeList = [];
                    for (var j = 0; j < vm.box.frozenBoxType.frozenBoxTypeRows; j++) {
                        tubeList[j] = [];
                        var rowNO = j > 7 ? j + 1 : j;
                        rowNO = String.fromCharCode(rowNO + 65);
                        for (var k = 0; k < vm.box.frozenBoxType.frozenBoxTypeColumns; k++) {
                            tubeList[j][k] = {
                                frozenBoxCode: vm.box.frozenBoxCode,
                                sampleCode: "",
                                sampleTempCode: vm.box.frozenBoxCode + "-" + rowNO + (k + 1),
                                sampleTypeId: vm.box.sampleTypeId,
                                sampleClassificationId: vm.box.sampleClassificationId,
                                frontColor:vm.box.frontColor,
                                status: "3001",
                                tubeRows: rowNO,
                                tubeColumns: k + 1+""
                            };
                            if (isMixed == 1) {
                                for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                                    if (vm.projectSampleTypeOptions[l].columnsNumber == k + 1) {
                                        tubeList[j][k].sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                                    }
                                }
                            }

                            vm.box.frozenTubeDTOS.push(tubeList[j][k]);
                            for (var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                                var row1 = vm.box.frozenTubeDTOS[m].tubeRows;
                                var col1 = vm.box.frozenTubeDTOS[m].tubeColumns;
                                for(var n = 0; n < frozenTubeDTOS.length; n++){
                                    var row2 = frozenTubeDTOS[n].tubeRows;
                                    var col2 = frozenTubeDTOS[n].tubeColumns;
                                    if(row1 == row2 && col1 == col2 ){
                                        vm.box.frozenTubeDTOS[m] = frozenTubeDTOS[n];
                                    }
                                }
                            }
                        }
                    }

                    var box = vm.box;
                    _reloadTubesForTable(box);
                    hotRegisterer.getInstance('my-handsontable').render();
                }
            };
            //样本类型
            vm.sampleClassFlag = false;
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    if(!value){
                        return;
                    }
                    isMixed = _.find(vm.sampleTypeOptions,{'id':+value}).isMixed;
                    var sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;

                    vm.box.sampleTypeId = value;
                    vm.box.sampleTypeCode = sampleTypeCode;
                    vm.box.sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeName;
                    vm.box.frontColor = _.find(vm.sampleTypeOptions,{'id':+value}).frontColor;

                    vm.fnQueryProjectSampleClass(vm.transportRecord.projectId,value,isMixed);
                    if(isMixed == 1){
                        vm.box.isSplit = 1;
                        vm.isMixedFlag = true;
                        vm.sampleClassFlag = true;
                        delete vm.box.sampleClassificationId;
                    }else{
                        vm.box.isSplit = 0;
                        vm.isMixedFlag = false;
                        vm.sampleClassFlag = false;
                    }

                    if (sampleTypeCode == "RNA"){
                        vm.box.isSplit = 1;
                    }

                    // Added by Zhuyu 2017/10/09 For: 选中RNA时自动切换冻存盒为大橘盒，选中99时切换为10x10
                    var sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                    var boxType = _.filter(vm.frozenBoxTypeOptions, {frozenBoxTypeCode: SampleTypeService.getBoxTypeCode(sampleTypeCode)})[0];
                    if (boxType) {
                        setTimeout(function(){
                            vm.boxTypeInstance.setValue(boxType.id);
                        }, 100);
                    }
                    // end added
                }
            };
            vm.projectSampleTypeConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.sampleClassificationId = value;
                    vm.box.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{"sampleClassificationId":+value}).sampleClassificationCode;
                    vm.box.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{"sampleClassificationId":+value}).sampleClassificationName;
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassificationId;
                        }
                    }
                    for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                        vm.box.frozenTubeDTOS[m].sampleClassificationId = vm.box.sampleClassificationId;
                    }
                    hotRegisterer.getInstance('my-handsontable').render();
                }
            };

            //设备
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);
                vm.transportRecordPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);
            }
            //盒子位置
            vm.frozenBoxPlaceConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.areaId = "";
                    vm.box.supportRackId = "";
                    vm.boxRowCol = "";
                    vm.box.columnsInShelf = "";
                    vm.box.rowsInShelf = "";
                    if(value){
                        AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                    }else{
                        vm.frozenBoxAreaOptions = [];
                        vm.frozenBoxAreaOptions.push({id:"",areaCode:""});

                        vm.frozenBoxShelfOptions = [];
                        vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});


                        $scope.$apply();
                    }
                }
            };
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.supportRackId = "";
                    vm.boxRowCol = "";
                    vm.box.columnsInShelf = "";
                    vm.box.rowsInShelf = "";
                    if(value){
                        for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                            if(value == vm.frozenBoxAreaOptions[i].id){
                                vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode;
                            }
                        }
                        SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);
                    }else{
                        vm.frozenBoxShelfOptions = [];
                        vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});
                        $scope.$apply();
                    }


                }
            };

            vm.transportRecordPlaceConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.transportRecord.tempAreaId = "";
                    if(value){
                        AreasByEquipmentIdService.query({id:value},onTransportRecordAreaSuccess, onError);
                    }else{
                        vm.transportRecordAreaOptions = [];
                        vm.transportRecordAreaOptions.push({id:"",areaCode:""});
                        $scope.$apply();
                    }
                }
            };
            vm.transportRecordAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            //架子
            function onShelfSuccess(data) {
                vm.frozenBoxShelfOptions = data;
                vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});
            }
            vm.frozenBoxShelfConfig = {
                valueField:'id',
                labelField:'supportRackCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.boxRowCol = "";
                    vm.box.columnsInShelf = "";
                    vm.box.rowsInShelf = "";
                    if(value){
                        for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                            if(value == vm.frozenBoxShelfOptions[i].id){
                                vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode;
                            }
                        }
                    }
                    $scope.$apply();
                }
            };
            //盒子位置
            vm.splitPlace = function () {
                if(vm.boxRowCol){
                    vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                    vm.box.rowsInShelf = vm.boxRowCol.substring(1);
                }else{
                    vm.box.columnsInShelf = "";
                    vm.box.rowsInShelf = "";
                }
            };
            vm.saveBox = saveBox;//保存盒子
            function saveBox(callback){
                if(vm.box.frozenBoxCode == vm.box.frozenBoxCode1D){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/common/prompt-modal.html',
                        size: 'sm',
                        controller: 'PromptModalController',
                        controllerAs: 'vm',
                        backdrop:'static',
                        resolve: {
                            items: function () {
                                return {
                                    status:'7'
                                };
                            }
                        }
                    });
                    modalInstance.result.then(function () {
                        BioBankBlockUi.blockUiStart();
                        var obox = {
                            transhipId:vm.transportRecord.id,
                            frozenBoxDTOList:[]
                        };

                        if(vm.box) {
                            obox.frozenBoxDTOList = [];
                            obox.frozenBoxDTOList.push(vm.createBoxDataFromTubesTable());
                        }
                        if(obox.frozenBoxDTOList.length){
                            TranshipBoxService.update(obox,onSaveBoxSuccess,onError);
                        }
                    }, function () {

                    });
                }else{
                    BioBankBlockUi.blockUiStart();
                    var obox = {
                        transhipId:vm.transportRecord.id,
                        frozenBoxDTOList:[]
                    };

                    if(vm.box) {
                        obox.frozenBoxDTOList = [];
                        obox.frozenBoxDTOList.push(vm.createBoxDataFromTubesTable());
                    }
                    if(obox.frozenBoxDTOList.length){
                        TranshipBoxService.update(obox,onSaveBoxSuccess,onError);
                    }
                }

                function onSaveBoxSuccess(res) {
                    if(!vm.saveStockInFlag && !vm.saveRecordFlag){
                        toastr.success("保存盒子成功！");
                        vm.dtInstance.rerender();
                        if(vm.makeNewBoxCodeFlag){
                            vm.rowBoxCode = vm.box.frozenBoxCode;
                        }
                        if(vm.rowBoxCode){
                            frozenBoxByCodeService.get({code:vm.rowBoxCode},vm.onFrozenSuccess,onError);
                        }
                        vm.queryTransportRecord();


                    }
                    BioBankBlockUi.blockUiStop();
                    if (typeof callback === "function"){
                        callback.call(this, res);
                    }

                }
            }


            vm.reImportFrozenBoxData = _fnReImportFrozenBoxData;//重新导入
            //重新导入
            function _fnReImportFrozenBoxData(){
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'reImportDataModal.html',
                    size: 'sm',
                    controller: 'BoxInstanceCtrl',
                    controllerAs: 'ctrl'
                });
                modalInstance.result.then(function (flag) {
                    if(flag){
                        frozenBoxByCodeService.get({code:vm.box.frozenBoxCode},vm.onFrozenSuccess,onError);
                    }
                }, function () {
                });
            }

            vm.delBox = _fnDelBox;
            //删除盒子
            function _fnDelBox() {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/modal/frozen-box-delete-modal.html',
                    controller: 'FrozenBoxDeleteController',
                    backdrop:'static',
                    controllerAs: 'vm'

                });
                modalInstance.result.then(function (flag) {
                    if (flag){
                        FrozenBoxDelService.delete({code:vm.box.frozenBoxCode},onDelBoxSuccess,onError);
                    }
                    function onDelBoxSuccess() {
                        toastr.success("删除成功!");
                        vm.dtInstance.rerender();
                        vm.box = null;
                        vm.boxStr = null;
                        initFrozenTube(10,10);
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
                });
            }
            var isMixed;
            vm.onFrozenSuccess = onFrozenSuccess;
            function onFrozenSuccess(data) {
                vm.flagStatus = false;
                vm.box = data;
                vm.box.frozenBoxTypeId = vm.box.frozenBoxType.id;
                vm.box.sampleTypeId = vm.box.sampleType.id;
                vm.box.sampleTypeCode = vm.box.sampleType.sampleTypeCode;
                isMixed = vm.box.sampleType.isMixed;
                if(vm.box.sampleTypeCode == 'RNA'){
                    vm.box.isSplit = 1;
                }
                if(isMixed == 1){
                    vm.box.isSplit = 1;
                    vm.isMixedFlag = true;
                }else{
                    vm.isMixedFlag = false;
                }
                SampleTypeService.queryProjectSampleClasses(vm.transportRecord.projectId,vm.box.sampleTypeId).success(function (data1) {
                    vm.projectSampleTypeOptions = data1;
                    vm.boxRowCol = "";
                    if(vm.box.sampleClassification){
                        vm.box.sampleClassificationId = vm.box.sampleClassification.id;
                    }
                    if(isMixed == 1){
                        vm.sampleClassFlag = true;
                    }else{
                        vm.sampleClassFlag = false;
                    }
                    if(vm.box.equipmentId){
                        AreasByEquipmentIdService.query({id:vm.box.equipmentId},onAreaSuccess, onError);
                    }
                    if(vm.box.areaId){
                        SupportacksByAreaIdService.query({id:vm.box.areaId},onShelfSuccess, onError);
                    }
                    if(vm.box.columnsInShelf && vm.box.rowsInShelf){
                        vm.boxRowCol =  vm.box.columnsInShelf + vm.box.rowsInShelf;
                    }
                    // initFrozenTube(vm.box.frozenBoxType.frozenBoxTypeRows,vm.box.frozenBoxType.frozenBoxTypeColumns);
                    _reloadTubesForTable(vm.box);
                    vm.boxStr = JSON.stringify(vm.createBoxDataFromTubesTable());
                    BioBankBlockUi.blockUiStop();
                });

                //统计样本数
                _sampleCount(vm.box.frozenTubeDTOS);
            }
        }
        //查询转运记录
        vm.queryTransportRecord = _fuQueryTransportRecord;
        //区域
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;
            vm.frozenBoxAreaOptions.push({id:"",areaCode:""});
        }
        function onTransportRecordAreaSuccess(data) {
            vm.transportRecordAreaOptions = data;
            vm.transportRecordAreaOptions.push({id:"",areaCode:""});
        }
        function _fuQueryTransportRecord() {
            TransportRecordService.get({id : vm.transportRecord.id},onRecordSuccess,onError);
        }
        function onRecordSuccess(data) {
            BioBankBlockUi.blockUiStop();
            //样本人份
            vm.transportRecord.sampleNumber = data.sampleNumber;
            //有效样本数
            vm.transportRecord.effectiveSampleNumber = data.effectiveSampleNumber;
            //盒数
            vm.transportRecord.frozenBoxNumber = data.frozenBoxNumber;
            //空管数
            vm.transportRecord.emptyTubeNumber = data.emptyTubeNumber;
            //空孔数
            vm.transportRecord.emptyHoleNumber = data.emptyHoleNumber;
        }
        function onError(error) {
            toastr.error(error.data.message);
            BioBankBlockUi.blockUiStop();
            vm.repeatSampleArray = JSON.parse(error.data.params[0]);
            hotRegisterer.getInstance('my-handsontable').render();
        }
        //滿意程度
        vm.rating = 0;
        vm.ratings = [{
            current: vm.transportRecord.sampleSatisfaction,
            max: 10
        }];
        vm.getSelectedRating = function (rating) {
            vm.transportRecord.sampleSatisfaction = rating;
        };

        //附件
        function _fnQueryTransportRecordFile() {
            TransportRecordService.queryTransportRecordFile(vm.transportRecord.transhipCode).success(function (data) {
                vm.transportRecordUploadInfo = data;
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
        _fnQueryTransportRecordFile();

        vm.uploadFile = _fnUploadFile;
        //删除附件
        vm.delUploadInfo = _fnDelUploadInfo;
        //上传
        function _fnUploadFile(status,imgData) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/modal/transport-record-upload-image-modal.html',
                controller: 'transportUploadImageModalCtrl',
                backdrop:'static',
                controllerAs: 'vm',
                resolve:{
                    items:function () {
                        return{
                            transportId:vm.transportRecord.id,
                            status:status,
                            imgData:imgData
                        };
                    }
                }
            });
            modalInstance.result.then(function () {
                _fnQueryTransportRecordFile();
            },function (data) {
                _fnQueryTransportRecordFile();
            });
        }
        //删除附件
        function _fnDelUploadInfo(imgId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'removeImgModal.html',
                controller: 'BoxInstanceCtrl',
                backdrop:'static',
                controllerAs: 'ctrl'
            });
            modalInstance.result.then(function () {
                //删除
                TransportRecordService.delTransportRecordFile(imgId).success(function (data) {
                    toastr.success("删除成功!");
                    _fnQueryTransportRecordFile();
                }).error(function (data) {
                    toastr.error(data.message);
                });

            },function (data) {
                _fnQueryTransportRecordFile();
            });
        }
    }
    function BoxInstanceCtrl($uibModalInstance) {
        var ctrl = this;
        ctrl.ok = function () {
            $uibModalInstance.close(true);
        };
        ctrl.unSave = function () {
            $uibModalInstance.close(false);
        };
        ctrl.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
