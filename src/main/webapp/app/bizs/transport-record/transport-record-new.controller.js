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

        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.transportRecord = entity; //转运记录
        vm.frozenTubeArray = [];//初始管子数据二位数组
        var remarkArray;//批注
        vm.operateStatus;// 1.状态 2:换位 3.批注
        var domArray = [];//单元格操作的数据
        var operateColor;//单元格颜色
        var modalInstance;
        //保存完整
        vm.saveFlag = false;
        vm.openCalendar = openCalendar; //时间
        vm.importFrozenStorageBox = importFrozenStorageBox; //导入冻存盒
        vm.someClickHandler = someClickHandler; //点击冻存盒的表格行
        vm.reImportFrozenBoxData = _fnReImportFrozenBoxData;//重新导入
        vm.delBox = _fnDelBox;//删除盒子
        vm.saveRecord = saveRecord; //保存记录
        vm.saveBox = saveBox;//保存盒子
        vm.loadBox = loadBox;
        if($stateParams.transhipId){
            vm.transportRecord.id = $stateParams.transhipId;
        }

        if($stateParams.transhipCode){
            vm.transportRecord.transhipCode = $stateParams.transhipCode;
        }
        var obox = {
            transhipId:vm.transportRecord.id,
            frozenBoxDTOList:[]
        };
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
        function loadBox() {
            if(vm.transportRecord.transhipCode){
                TranshipBoxByCodeService.query({code:vm.transportRecord.transhipCode},onBoxSuccess,onError)
            }
        }
        var size = 10;
        function initFrozenTube(size) {
            for(var i = 0; i < size; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < size; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        initFrozenTube(size);

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

        var htm; //渲染管子表格
        var tube= {};
        vm.myCustomRenderer = function(hotInstance, td, row, col, prop, value, cellProperties) {
            td.style.position = 'relative';
            if(value == null){
                tube.sampleCode = "";
                tube.sampleTempCode = "";
                tube.sampleTypeCode = 'S_TYPE_00001';
                tube.tubeRows = getTubeRows(row);
                tube.tubeColumns = getTubeColumns(col);
                tube.memo = ""
            }
            if(typeof(value) == "string"){
                if(value == "" || value == null){
                    tube.sampleCode = "";
                    tube.sampleTempCode = "";
                    tube.sampleTypeCode = 'S_TYPE_00001';
                    tube.tubeRows = getTubeRows(row);
                    tube.tubeColumns = getTubeColumns(col);
                    tube.memo = ""
                }else{
                    tube.sampleCode = value;
                    tube.sampleTypeCode = vm.box.sampleTypeCode;
                    tube.status = vm.box.status
                }
            }else{
                if(value == ""){
                    tube.sampleTempCode = "";
                }else{
                    tube = value;
                }

            }
            if(tube.memo != " " && tube.memo != "" && tube.memo != null){
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


            htm = "<div ng-if='tube.sampleCode' style='line-height: 20px;word-wrap: break-word'>"+tube.sampleCode+"</div>"+
                "<div  ng-if='!tube.sampleCode' style='line-height: 20px;word-wrap: break-word'>"+tube.sampleTempCode+"</div>" +
                "<div  style='display: none'>"+tube.sampleTypeCode+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+tube.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+tube.memo+"</div>"+
                "<div id='microtubesRow' style='display: none'>"+tube.tubeRows+"</div>"+
                "<div id='microtubesCol' style='display: none'>"+tube.tubeColumns+"</div>"+
                "<div ng-if="+tube.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"

            td.innerHTML = htm;
        };
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
                td.style.background = 'linear-gradient(to right,'+operateColor+',50%,rgba(0,0,0,1)';
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
        var aRemarkArray = [];
        vm.settings = {
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
            data:vm.frozenTubeArray,
            renderer:vm.myCustomRenderer,
            minRows: 10,
            minCols: 10,
            fillHandle:false,
            stretchH: 'all',
            autoWrapCol:true,
            wordWrap:true,
            colWidths: 94,
            rowHeaderWidth: 30,
            editor: 'tube',
            onAfterSelectionEnd:function (row, col, row2, col2) {
                vm.remarkFlag = true;
                var td = this;
                remarkArray = this.getData(row,col,row2,col2);
                var selectTubeArray = this.getSelected();

                if(window.event.ctrlKey){
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


                //管子
                if(vm.operateStatus == 1) {
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
                        hotRegisterer.getInstance('my-handsontable').render();
                    }
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
                    console.log("编辑")
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
        //备注 选择单元格数据
        function _fnRemarkSelectData(td,remarkArray,selectTubeArray) {
            var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:2px dotted #5292F7;"></div>';
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
        function defaultValueRenderer(instance, td, row, col, prop, value, cellProperties) {
            var args = arguments;
            td.style.position = 'relative';
            if(typeof(value) == "string"){
                if(value == ""){
                    tube.sampleCode = "";
                    tube.sampleTempCode = "";
                    tube.sampleTypeCode = 'S_TYPE_00001';
                    // value.status = "3003";//冻存管状态3001：正常，3002：空管，3003：空孔；3004：异常
                    tube.tubeRows = getTubeRows(row);
                    tube.tubeColumns = getTubeColumns(col);
                    tube.memo = ""
                }else{
                    tube.sampleCode = value;
                    tube.sampleTypeCode = vm.box.sampleTypeCode;
                    tube.status = vm.box.status
                }
            }else{
                if(value == ""){
                    tube.sampleTempCode = "";
                }else{
                    tube = value;
                }

            }

            //样本类型
            if(tube.sampleTypeCode){
                SampleService.changeSampleType(tube.sampleTypeCode,td);
            }
            //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
            if(tube.status){
                changeSampleStatus(tube.status,row,col,td,cellProperties)
            }

            htm = "<div ng-if='value.sampleCode' style='line-height: 20px;word-wrap: break-word'>"+tube.sampleCode+"</div>"+
                "<div  ng-if='tube.sampleTempCode' style='line-height: 20px;word-wrap: break-word'>"+tube.sampleTempCode+"</div>" +
                "<div  style='display: none'>"+tube.sampleTypeCode+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+tube.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+tube.memo+"</div>"+
                "<div id='microtubesRow' style='display: none'>"+tube.tubeRows+"</div>"+
                "<div id='microtubesCol' style='display: none'>"+tube.tubeColumns+"</div>"+
                "<div ng-if="+tube.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"

            td.innerHTML = htm;



        }
        // var CustomTextEditor = Handsontable.editors.TextEditor.prototype.extend();
        // console.log(CustomTextEditor)
        // CustomTextEditor.prototype.init = function() {
        //     Handsontable.editors.TextEditor.prototype.createElements.apply(this, arguments);
        //
        //     // Create password input and update relevant properties
        //     this.TEXTAREA = document.createElement('input');
        //     this.TEXTAREA.setAttribute('type', 'text');
        //     this.TEXTAREA.className = 'handsontableInput';
        //     this.textareaStyle = this.TEXTAREA.style;
        //     this.textareaStyle.width = 0;
        //     this.textareaStyle.height = 0;
        //
        //     // Replace textarea with password input
        //     Handsontable.Dom.empty(this.TEXTAREA_PARENT);
        //     this.TEXTAREA_PARENT.appendChild(this.TEXTAREA);
        //     Handsontable.editors.registerEditor('input', CustomTextEditor);
        // };
        // CustomTextEditor.prototype.getValue = function() {
        //
        // };
        // CustomTextEditor.prototype.setValue  = function(newValue) {
        //     console.log(newValue)
        // };
        // CustomTextEditor.prototype.open = function() {
        //
        // };
        // CustomTextEditor.prototype.close = function() {
        //
        // };
        // CustomTextEditor.prototype.focus  = function() {
        //     this.editorInput.focus();
        // };

        //修改样本状态
        vm.flagStatus = false;
        vm.editStatus = function () {
            vm.operateStatus = 1;
            var cellProperties = hotRegisterer.getInstance('my-handsontable').getCellsMeta();
            for(var i = 0; i < cellProperties.length; i++){
                if(vm.flagStatus){
                    cellProperties[i].editor = false;
                }else{
                    cellProperties[i].editor = 'text';
                }
            }
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
                modalInstance.result.then(function (selectedItem) {
                    aRemarkArray = [];
                    hotRegisterer.getInstance('my-handsontable').render();
                });
            }
        };
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

                //true:保存 false:不保存
                if(flag){

                }
                frozenBoxByCodeService.get({code:vm.box.frozenBoxCode},onFrozenSuccess,onError);
            }, function () {
            });
        }
        //删除盒子
        function _fnDelBox() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/frozen-box-delete-modal.html',
                controller: 'FrozenBoxDeleteController',
                backdrop:'static',
                controllerAs: 'vm'

            });
            modalInstance.result.then(function () {
                FrozenBoxDelService.delete({code:vm.box.frozenBoxCode},onDelBoxSuccess,onError)
            });

        }
        function onDelBoxSuccess() {
            AlertService.success("删除成功!");
            loadBox();
            vm.box = null;
            initFrozenTube(10);
            hotRegisterer.getInstance('my-handsontable').render();
        }
        var loadAll = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);//样本类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);//盒子类型
            EquipmentService.query({},onEquipmentSuccess, onError);//设备
            ProjectService.query({},onProjectSuccess, onError)//项目
            loadBox()
        };
        loadAll();

        //盒子类型
        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
        }
        //盒子类型 17:10*10 18:8*8
        var arr1 = [];
        var arr2 = [];
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                    vm.frozenTubeArray = [];
                    if(value == 18){
                        initFrozenTube(8);
                        vm.settings.columns = 8;
                        vm.settings.minRows = 8;
                    }else{
                        initFrozenTube(10);
                    }
                    for(var i = 0; i < vm.box.frozenTubeDTOS.length; i++){
                        if(value == 18){
                            var indexRow = getTubeRowIndex(vm.box.frozenTubeDTOS[i].tubeRows);
                            var indexCol = getTubeColumnIndex(vm.box.frozenTubeDTOS[i].tubeColumns);
                            if(indexCol < 8){
                                if(indexRow < 8){
                                    var tube = vm.box.frozenTubeDTOS[i];
                                    vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
                                    vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)].frozenBoxCode = vm.box.frozenBoxCode
                                }
                            }
                        }else{
                            var tube = vm.box.frozenTubeDTOS[i];
                            vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
                            vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)].frozenBoxCode = vm.box.frozenBoxCode
                        }

                    }
                    console.log(JSON.stringify(vm.frozenTubeArray));

                vm.box.frozenBoxTypeId = value;

                // hotRegisterer.getInstance('my-handsontable').render();

                hotRegisterer.getInstance('my-handsontable').loadData(vm.frozenTubeArray)
            }
        };

        //样本类型
        function onSampleTypeSuccess(data) {
            vm.sampleTypeOptions = data;
        }
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
        if(vm.transportRecord.projectId){
            ProjectSitesByProjectIdService.query({id:vm.transportRecord.projectId},onProjectSitesSuccess,onError)
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
        function onProjectSitesSuccess(data) {
            vm.projectSitesOptions = data;
            vm.transportRecord.projectSiteId = data[0].id;
        }
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

        function onBoxSuccess(data) {
            vm.boxLength = data.length;
            // vm.boxList = data[0];
            vm.dtOptions = DTOptionsBuilder.newOptions()
                .withOption('data', data)
                .withOption('info', false)
                .withOption('paging', false)
                .withOption('sorting', false)
                .withScroller()
                .withOption('scrollY', 500)
                .withOption('rowCallback', rowCallback);
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        //盒子信息
        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('paging', false)
            .withOption('sorting', false);
        vm.dtColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号')
        ];


        vm.dtInstance = {};
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
                loadBox();
            });
        }
        //保存保存记录
        function saveRecord() {
            saveBox();
        }
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

        function saveBox(){
            if(vm.box == undefined){
                obox.frozenBoxDTOList = [];
            }else{
                if(vm.boxRowCol){
                    vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                    vm.box.rowsInShelf = vm.boxRowCol.charAt(vm.boxRowCol.length - 1);
                }
                obox.frozenBoxDTOList = [];
                obox.frozenBoxDTOList.push(vm.box);
            }



            TranshipBoxService.update(obox,onSaveBoxSuccess,onError);
        }
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
        function onSaveBoxSuccess() {
            AlertService.success("保存冻存盒成功！");
            TranshipSaveService.update(vm.transportRecord,onSaveTranshipRecordSuccess,onError)
        }
        function onSaveSuccess () {
            $state.go('transport-record');
        }
        function onSaveError () {

        }
        //转运入库
        vm.stockIn = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/transport-record/stock-in-affirm-modal.html',
                controller: 'StockInAffirmModalController',
                backdrop:'static',
                controllerAs: 'vm'

            });
            modalInstance.result.then(function () {
                //保存完整
                vm.saveFlag = true;
                vm.saveRecord();


            });

        };
        // function onStockInSuccess(data) {
        //     AlertService.success("入库成功！");
        // }
        //点击冻存盒行
        var count = 0;
        function someClickHandler(td,boxInfo) {
            if(count == 0){
                $(td).closest('table').find('.rowLight').removeClass("rowLight");
                $(td).addClass('rowLight');
                frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},onFrozenSuccess,onError);

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
                        saveBox()
                    }
                    frozenBoxByCodeService.get({code:boxInfo.frozenBoxCode},onFrozenSuccess,onError);
                }, function () {
                });
            }
            count++;
        }
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
            for(var k = 0; k < vm.box.frozenTubeDTOS.length; k++){
                var tube = vm.box.frozenTubeDTOS[k];
                vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
                vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)].frozenBoxCode = vm.box.frozenBoxCode
            }
            hotRegisterer.getInstance('my-handsontable').render();
        }
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $('td', nRow).bind('click', function() {
                var td = this;
                $scope.$apply(function() {
                    vm.someClickHandler(td,oData);
                });
            });
            if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
                $('td', nRow).addClass('rowLight');
            }
            return nRow;
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
