/**
 * Created by gaokangkang on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TransportRecordNewController', TransportRecordNewController)
        .controller('BoxInstanceCtrl',BoxInstanceCtrl);

    TransportRecordNewController.$inject = ['$scope','hotRegisterer','SampleService','TransportRecordService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','$stateParams','entity','frozenBoxByCodeService','TranshipNewEmptyService','TranshipSaveService','TranshipBoxService',
        'SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService','TranshipBoxByCodeService','TranshipStockInService','FrozenBoxDelService'];
    BoxInstanceCtrl.$inject = ['$uibModalInstance'];
    function TransportRecordNewController($scope,hotRegisterer,SampleService,TransportRecordService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,$stateParams,entity,frozenBoxByCodeService,TranshipNewEmptyService,TranshipSaveService,TranshipBoxService,
                                          SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService,TranshipBoxByCodeService,TranshipStockInService,FrozenBoxDelService) {

        var modalInstance;
        var vm = this;
        vm.transportRecord = entity; //转运记录



        _initTransportRecordPage();
        _initFrozenBoxesTable();
        _initFrozenBoxPanel();

        function _initTransportRecordPage(){
            if($stateParams.transhipId){
                vm.transportRecord.id = $stateParams.transhipId;
            }

            if($stateParams.transhipCode){
                vm.transportRecord.transhipCode = $stateParams.transhipCode;
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
            if(vm.transportRecord.transhipBatch == null || vm.transportRecord.transhipBatch == " "){
                vm.transportRecord.transhipBatch = 1;
            }else{
                vm.transportRecord.transhipBatch = +vm.transportRecord.transhipBatch
            }
            if(vm.transportRecord.sampleSatisfaction == 0){
                vm.transportRecord.sampleSatisfaction = 10;
            }

            if(vm.transportRecord.projectId){
                ProjectSitesByProjectIdService.query({id:vm.transportRecord.projectId},onProjectSitesSuccess,onError)
            }

            SampleTypeService.query({},onSampleTypeSuccess, onError);//样本类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);//盒子类型
            ProjectService.query({},onProjectSuccess, onError)//项目

            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                maxItems: 1,
                onChange:function(value){
                    for(var i = 0; i < vm.projectOptions.length;i++){
                        if(value == vm.projectOptions[i].id){
                            vm.transportRecord.projectCode = vm.projectOptions[i].projectCode;
                            vm.transportRecord.projectName = vm.projectOptions[i].projectName
                        }
                    }

                    ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError)
                }
            };
            //项目点
            vm.projectSitesConfig = {
                valueField:'id',
                labelField:'projectSiteName',
                maxItems: 1,
                onChange:function (value) {
                    for(var i = 0; i < vm.projectSitesOptions.length;i++){
                        if(value == vm.projectSitesOptions[i].id){
                            vm.transportRecord.projectSiteCode = vm.projectSitesOptions[i].projectSiteCode
                            vm.transportRecord.projectSiteName = vm.projectSitesOptions[i].projectSiteName
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
            vm.statusOptions = [
                {id:"1001",name:"进行中"},
                {id:"1002",name:"待入库"},
                {id:"1003",name:"已入库"},
                {id:"1004",name:"已作废"}
            ];
            vm.statusConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1

            };

            vm.datePickerOpenStatus = {};
            vm.openCalendar = openCalendar; //时间
            vm.importFrozenStorageBox = importFrozenStorageBox; //导入冻存盒

            //保存完整
            vm.saveFlag = false;
            vm.saveRecord = saveRecord; //保存记录
            //转运入库
            vm.stockIn = function () {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/stock-in-affirm-modal.html',
                    controller: 'StockInAffirmModalController',
                    backdrop:'static',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function (flage) {
                    if (flage){
                        //保存完整
                        vm.saveFlag = true;
                        vm.saveRecord();
                    }
                });
            };
            //导入冻存盒
            function importFrozenStorageBox() {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/frozen-storage-box-modal.html',
                    controller: 'FrozenStorageBoxModalController',
                    controllerAs:'vm',
                    size:'lg',
                    resolve: {
                        items: function () {
                            return {
                                transhipId :vm.transportRecord.id
                            }
                        }
                    }

                });
                modalInstance.result.then(function (data) {
                    vm.loadBox();
                });
            }
            //保存保存记录
            function saveRecord() {
                vm.saveBox(function(){
                    AlertService.success("保存冻存盒成功！");
                    TranshipSaveService.update(vm.transportRecord,onSaveTranshipRecordSuccess,onError);
                    function onSaveTranshipRecordSuccess(data) {
                        AlertService.success("保存转运记录成功");
                        if(vm.saveFlag){
                            TranshipStockInService.saveStockIn(vm.transportRecord.transhipCode).then(function (data) {
                                AlertService.success("入库成功！");
                                //保存完整
                                vm.saveFlag = false;
                                $state.go('stock-in-edit',{id:data.data.id});
                                // loadAll();
                            })
                        }
                    }
                });
            }

            function openCalendar (date) {
                vm.datePickerOpenStatus[date] = true;
            }

            //盒子类型
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = data;
            }

            //样本类型
            function onSampleTypeSuccess(data) {
                vm.sampleTypeOptions = data;
            }

            //项目编码
            function onProjectSuccess(data) {
                vm.projectOptions = data;
                if(data.length){
                    vm.transportRecord.projectId = data[0].id;
                }
                if(vm.transportRecord.projectId){
                    ProjectSitesByProjectIdService.query({id:vm.transportRecord.projectId},onProjectSitesSuccess,onError)
                }
            }

            // 项目点
            function onProjectSitesSuccess(data) {
                vm.projectSitesOptions = data;
                vm.transportRecord.projectSiteId = data[0].id;
            }
        }


        function _initFrozenBoxesTable(){
            vm.loadBox = loadBox;
            loadAll();

            vm.dtInstance = {};
            vm.dtColumns = [
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号')
            ];
            vm.dtOptions = DTOptionsBuilder.newOptions()
                // .withOption('data', data)
                .withOption('info', false)
                .withOption('paging', false)
                .withOption('sorting', false)
                .withScroller()
                .withOption('scrollY', 500)
                .withOption('rowCallback', rowCallback);

            function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
                $('td', nRow).unbind('click');
                $('td', nRow).bind('click', function() {
                    var td = this;
                    $scope.$apply(function() {
                        someClickHandler(td,oData);
                    });
                });
                if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
                    $('td', nRow).addClass('rowLight');
                }
                return nRow;
            }

            function loadAll() {
                loadBox();
            };
            function loadBox() {
                if(vm.transportRecord.transhipCode){
                    TranshipBoxByCodeService.query({code:vm.transportRecord.transhipCode},onBoxSuccess,onError)
                }
                function onBoxSuccess(data) {
                    vm.boxLength = data.length;
                    // vm.boxList = data[0];
                    vm.dtOptions.withOption('data', data);
                    // vm.dtInstance.DataTable.data(data);
                }
            }

            //点击冻存盒行
            function someClickHandler(td,boxInfo) {
                vm.strbox = JSON.stringify(vm.createBoxDataFromTubesTable());
                if(!vm.boxStr || vm.strbox === vm.boxStr){
                    $(td).closest('table').find('.rowLight').removeClass("rowLight");
                    $(td).addClass('rowLight');
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
                        $(td).closest('table').find('.rowLight').removeClass("rowLight");
                        $(td).addClass('rowLight');
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

        }

        function _initFrozenBoxPanel(){
            vm.frozenTubeArray = [];//初始管子数据二位数组
            var remarkArray;//批注
            var domArray = [];//单元格操作的数据
            var operateColor;//单元格颜色

            EquipmentService.query({},onEquipmentSuccess, onError);//设备

            initFrozenTube(10);

            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
                data:vm.frozenTubeArray,
                renderer: myCustomRenderer,
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 94,
                rowHeaderWidth: 30,
                editor: 'tube',
                multiSelect: true,
                comments: true,
                onAfterSelectionEnd:function (row, col, row2, col2) {
                    vm.remarkFlag = true;
                    var td = this;
                    remarkArray = this.getData(row,col,row2,col2);
                    var selectTubeArray = this.getSelected();

                    if(window.event && window.event.ctrlKey){
                        //换位
                        vm.exchangeFlag = true;
                        domArray.push(vm.frozenTubeArray[row][col]);

                        //备注
                        _fnRemarkSelectData(td,remarkArray,selectTubeArray)
                    }else{
                        domArray = [];
                        domArray.push(vm.frozenTubeArray[row][col]);
                        //备注
                        $(".temp").remove();
                        aRemarkArray = [];
                        _fnRemarkSelectData(td,remarkArray,selectTubeArray)


                    }
                    // console.log(vm.settings)

                    //管子
                    if(vm.flagStatus){
                        var tubeStatus = $(this.getCell(row, col)).find("#microtubesStatus").text();
                        if(tubeStatus == 3001){
                            vm.frozenTubeArray[row][col].status = 3002;
                        }
                        if(tubeStatus == 3002){
                            vm.frozenTubeArray[row][col].status = 3003;
                        }
                        if(tubeStatus == 3003){
                            vm.frozenTubeArray[row][col].status = 3004;
                        }
                        if(tubeStatus == 3004){
                            vm.frozenTubeArray[row][col].status = 3001;
                        }
                        // vm.settings.multiSelect =  false;
                        // console.log(vm.settings);
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
                },
                enterMoves:function () {
                    var hotMoves = hotRegisterer.getInstance('my-handsontable');
                    var selectedCol = hotMoves.getSelected()[1];
                    if(selectedCol + 1 < hotMoves.countCols()){
                        return{row:0,col:1}
                    } else{
                        return{row:1,col:-selectedCol}
                    }
                    // var hotMoves = hotRegisterer.getInstance('my-handsontable');
                    // var selectedRow = hotMoves.getSelected()[0];
                    // if (selectedRow + 1 < hotMoves.countRows()) {
                    //     return {row: 1, col: 0}
                    // }
                    // else {
                    //     return {row: -selectedRow, col: 1}
                    // }
                },
                afterChange:function (change,source) {
                    console.log(source)
                    if(source == 'edit'){
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

                        // hotRegisterer.getInstance('my-handsontable').render()
                        return;
                    }
                },
                afterBeginEditing:function (row,col) {
                    console.log(row)
                },
                afterSetDataAtCell:function (row,col,key,value) {
                    // console.log(row)
                    // console.log(col)
                },
                afterSetDataAtRowProp:function (row,col) {
                    // console.log(row)
                    // console.log(col)
                },
                beforeKeyDown:function (event) {
                    if(vm.flagStatus){
                        if(event.keyCode == 9 || event.keyCode == 37 || event.keyCode == 38 || event.keyCode == 39 || event.keyCode == 40){
                            Handsontable.Dom.stopImmediatePropagation(event);
                        }
                    }

                }

            };

            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = 'relative';

                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = tube.memo;
                }
                //样本类型
                if(tube.sampleTypeCode){
                    SampleService.changeSampleType(tube.sampleTypeCode,td);
                }
                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(tube.status){
                    changeSampleStatus(tube.status,row,col,td,cellProperties)
                }

                var code = tube.sampleCode && tube.sampleCode != " " ? tube.sampleCode : tube.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word',
                }).appendTo(td);
                $div = $("<div id='microtubesStatus'/>").html(tube.status).hide().appendTo(td);
            }

            //修改样本状态正常、空管、空孔、异常
            vm.normalCount = 0;
            vm.emptyCount = 0;
            function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {
                operateColor = td.style.backgroundColor;
                //正常
                if(sampleStatus == 3001){
                    vm.normalCount ++;
                }
                //空管
                if(sampleStatus == 3002){
                    td.style.background = 'linear-gradient(to right,'+operateColor+',50%,black';
                }
                //空孔
                if(sampleStatus == 3003){
                    vm.emptyCount++;
                    td.style.background = '';
                    td.style.backgroundColor = '#ffffff';
                    td.style.color = '#ffffff'
                }
                //异常
                if(sampleStatus == 3004){
                    // var dom = '<div class="abnormal" style="position:absolute;top:0;bottom:0;left:0;right:0;border:3px solid red;"></div>';
                    // $(td).append(dom);
                    td.style.backgroundColor = 'red';
                    td.style.border = '3px solid red;margin:-3px';
                }
            }


            function initFrozenTube(size) {
                for(var i = 0; i < size; i++){
                    vm.frozenTubeArray[i] = [];
                    for(var j = 0;j < size; j++){
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
                    sampleTypeId: box.sampleTypeId,
                    sampleTypeCode: box.sampleTypeCode,
                    frozenBoxId: box.id,
                    frozenBoxCode: box.frozenBoxCode,
                    status: "",
                    memo: "",
                    tubeRows: pos.tubeRows,
                    tubeColumns: pos.tubeColumns,
                    rowNO: rowNO,
                    colNO: colNO
                }
                if (tubeInBox){
                    tube.id = tubeInBox.id;
                    tube.sampleCode = tubeInBox.sampleCode;
                    tube.sampleTempCode = tubeInBox.sampleTempCode;
                    tube.sampleTypeId = tubeInBox.sampleTypeId;
                    tube.sampleTypeCode = tubeInBox.sampleTypeCode;
                    tube.status = tubeInBox.status;
                    tube.memo = tubeInBox.memo;
                }
                return tube;
            }
            // 重新加载管子表控件
            function _reloadTubesForTable(box){
                var settings = {
                    minCols: +box.frozenBoxColumns,
                    minRows: +box.frozenBoxRows
                };

                var tubesInTable = [];
                for (var i=0; i<settings.minRows; ++i){
                    var tubes = [];
                    for (var j=0; j<settings.minCols; ++j){
                        var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: j + 1 + ""};
                        var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                        var tube = _createTubeForTableCell(tubeInBox, box, i, j + 1, pos);
                        tubes.push(tube);
                    }
                    tubesInTable.push(tubes);
                }
                vm.frozenTubeArray = tubesInTable;

                hotRegisterer.getInstance('my-handsontable').loadData(tubesInTable);
                hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
            }
            vm.createBoxDataFromTubesTable = _createBoxDataFromTubesTable;
            function _createBoxDataFromTubesTable(){
                if (!vm.box){
                    return null;
                }

                var box = angular.copy(vm.box)||{};
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
                            box.frozenTubeDTOS.push(tube)
                        }
                    }
                }

                return box;
            }


            //盒子类型 17:10*10 18:8*8
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onChange:function(value){
                    var boxType = _.filter(vm.frozenBoxTypeOptions, {id:+value})[0];
                    if (!boxType) {
                        return;
                    }

                    vm.box.frozenBoxTypeId = value;
                    vm.box.frozenBoxRows = boxType.frozenBoxTypeRows;
                    vm.box.frozenBoxColumns = boxType.frozenBoxTypeColumns;

                    var box = vm.box;
                    _reloadTubesForTable(box);
                }
            };
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    if(value != 16){
                        for(var i =0; i <  vm.frozenTubeArray.length; i++){
                            for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                vm.frozenTubeArray[i][j].sampleTypeId = value;
                                for(var k = 0; k < vm.sampleTypeOptions.length; k++){
                                    if(vm.frozenTubeArray[i][j].sampleTypeId == vm.sampleTypeOptions[k].id){
                                        vm.frozenTubeArray[i][j].sampleTypeName = vm.sampleTypeOptions[k].sampleTypeName;
                                        vm.frozenTubeArray[i][j].sampleTypeCode = vm.sampleTypeOptions[k].sampleTypeCode;
                                    }
                                }
                            }
                        }
                    }else{
                        for(var i =0; i <  vm.frozenTubeArray.length; i++){
                            for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
                                switch(j){
                                    case 0:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00001';
                                        break;
                                    case 1:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00002';
                                        break;
                                    case 2:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00003';
                                        break;
                                    case 3:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00004';
                                        break;
                                    case 4:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00005';
                                        break;
                                    case 5:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00006';
                                        break;
                                    case 6:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00007';
                                        break;
                                    case 7:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00008';
                                        break;
                                    case 8:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00009';
                                        break;
                                    case 9:
                                        vm.frozenTubeArray[i][j].sampleTypeCode = 'S_TYPE_00010';
                                        break;
                                }
                            }}
                    }

                    hotRegisterer.getInstance('my-handsontable').render()
                }
            };

            //设备
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            vm.frozenBoxPlaceConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                    for(var i = 0; i < vm.frozenBoxPlaceOptions.length; i++){
                        if(value == vm.frozenBoxPlaceOptions[i].id){
                            vm.box.equipmentCode = vm.frozenBoxPlaceOptions[i].equipmentCode
                        }
                    }
                }
            };
            //区域
            function onAreaSuccess(data) {
                vm.frozenBoxAreaOptions = data;

            }
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                        if(value == vm.frozenBoxAreaOptions[i].id){
                            vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode
                        }
                    }
                    SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError)

                }
            };
            //架子
            function onShelfSuccess(data) {
                vm.frozenBoxShelfOptions = data;
            }
            vm.frozenBoxShelfConfig = {
                valueField:'id',
                labelField:'supportRackCode',
                maxItems: 1,
                onChange:function (value) {
                    for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                        if(value == vm.frozenBoxShelfOptions[i].id){
                            vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode
                        }
                    }
                }
            };

            vm.saveBox = saveBox;//保存盒子
            function saveBox(callback){
                var obox = {
                    transhipId:vm.transportRecord.id,
                    frozenBoxDTOList:[]
                };

                if(vm.box) {
                    if(vm.boxRowCol){
                        vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                        vm.box.rowsInShelf = vm.boxRowCol.substring(1);
                    }
                    obox.frozenBoxDTOList = [];
                    obox.frozenBoxDTOList.push(vm.createBoxDataFromTubesTable());
                }
                TranshipBoxService.update(obox,onSaveBoxSuccess,onError);
                function onSaveBoxSuccess(res) {
                    if (typeof callback === "function"){
                        callback.call(this, res);
                    }
                }
            }

            var aRemarkArray = [];
            //备注 选择单元格数据
            function _fnRemarkSelectData(td,remarkArray,selectTubeArray) {
                var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px dashed #5292F7;"></div>';
                for(var m = 0; m < remarkArray.length; m++){
                    for (var n = 0; n < remarkArray[m].length; n++){
                        aRemarkArray.push(remarkArray[m][n])
                    }
                }
                for(var i = selectTubeArray[0];i <= selectTubeArray[2]; i++){
                    for(var j = selectTubeArray[1];  j <= selectTubeArray[3];j++)
                        $(td.getCell(i,j)).append(txt);
                }
            }
            //修改样本状态
            vm.flagStatus = false;
            vm.editStatus = function () {

                var settings = {
                    editor: vm.flagStatus ? false : 'tube',
                    // multiSelect: !vm.flagStatus
                };
                hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
            };
            //换位
            vm.exchangeFlag = false;
            vm.exchange = function () {
                if(vm.exchangeFlag && domArray.length == 2){
                    var row = getTubeRowIndex(domArray[0].tubeRows);
                    var col = getTubeColumnIndex(domArray[0].tubeColumns);
                    var row1 = getTubeRowIndex(domArray[1].tubeRows);
                    var col1 = getTubeColumnIndex(domArray[1].tubeColumns);

                    vm.frozenTubeArray[row][col] = domArray[1];
                    vm.frozenTubeArray[row][col].tubeRows = getTubeRows(row);
                    vm.frozenTubeArray[row][col].tubeColumns = getTubeColumns(col);

                    vm.frozenTubeArray[row1][col1] = domArray[0];
                    vm.frozenTubeArray[row1][col1].tubeRows = getTubeRows(row1);
                    vm.frozenTubeArray[row1][col1].tubeColumns = getTubeColumns(col1);

                    domArray = [];
                    vm.exchangeFlag = false;

                }else{
                    // console.log("只能选择两个进行交换！");
                    AlertService.error("只能选择两个进行交换！",{},'center');
                    domArray = [];
                }
                hotRegisterer.getInstance('my-handsontable').render();
            };
            //批注
            vm.remarkFlag = false;
            vm.tubeRemark = function () {
                if(aRemarkArray.length > 0){
                    modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'app/bizs/transport-record/microtubes-remark-modal.html',
                        controller: 'microtubesRemarkModalController',
                        backdrop:'static',
                        controllerAs: 'vm',
                        resolve: {
                            items: function () {
                                return {
                                    remarkArray :aRemarkArray
                                }
                            }
                        }

                    });
                    modalInstance.result.then(function (memo) {
                        for(var i = 0; i < aRemarkArray.length; i++){

                            if(aRemarkArray[i].sampleCode){
                                aRemarkArray[i].memo = memo;
                            }
                        }

                        aRemarkArray = [];
                        hotRegisterer.getInstance('my-handsontable').render();
                    });
                }
            };
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

            vm.delBox = _fnDelBox;//删除盒子
            //删除盒子
            function _fnDelBox() {
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/transport-record/frozen-box-delete-modal.html',
                    controller: 'FrozenBoxDeleteController',
                    backdrop:'static',
                    controllerAs: 'vm'

                });
                modalInstance.result.then(function (flage) {
                    if (flage){
                        FrozenBoxDelService.delete({code:vm.box.frozenBoxCode},onDelBoxSuccess,onError)
                    }
                    function onDelBoxSuccess() {
                        AlertService.success("删除成功!");
                        vm.loadBox();
                        vm.box = null;
                        vm.boxStr = null;
                        initFrozenTube(10);
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
                });
            }

            vm.onFrozenSuccess = onFrozenSuccess;
            function onFrozenSuccess(data) {
                vm.box = data;
                if(vm.box.equipmentId){
                    AreasByEquipmentIdService.query({id:vm.box.equipmentId},onAreaSuccess, onError);
                }
                if(vm.box.areaId){
                    SupportacksByAreaIdService.query({id:vm.box.areaId},onShelfSuccess, onError)
                }
                if(vm.box.supportRackId){
                    vm.boxRowCol =  vm.box.columnsInShelf + vm.box.rowsInShelf;
                }
                initFrozenTube(vm.box.frozenBoxColumns);
                _reloadTubesForTable(vm.box);
                vm.boxStr = JSON.stringify(vm.createBoxDataFromTubesTable());
            }
        }

        function onError(error) {
            AlertService.error(error.data.message);
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
