/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope','$state','$compile','$stateParams','$uibModal','hotRegisterer','$timeout','DTOptionsBuilder','DTColumnBuilder','TaskService','SampleUserService','MasterData','BioBankBlockUi','toastr','SampleService','BioBankDataTable'];

    function TaskDetailController($scope,$state,$compile,$stateParams,$uibModal,hotRegisterer,$timeout,DTOptionsBuilder,DTColumnBuilder,TaskService,SampleUserService,MasterData,BioBankBlockUi,toastr,SampleService,BioBankDataTable) {
        var vm = this;
        var modalInstance;
        vm.boxInstance = {};
        vm.stockOutSampleInstance = {};
        vm.task = {};

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }


        //保存任务
        vm.saveTask = _fnSaveTask;
        //样本交接
        vm.takeOver = _fnTakeOver;
        //打印取盒单
        vm.printBox = _fnPrintBox;
        //扫码取样
        vm.scanCode = _fnScanCode;
        //撤销
        vm.repealModal = _fnRepealModal;
        //异常
        vm.abnormal = _fnAbnormal;
        //1未出库样本、2已出库样本批注
        vm.commentModal = _fnCommentModal;
        //装盒
        vm.boxInModal = _fnBoxInModal;
        //出库
        vm.taskStockOutModal = _fnTaskStockOutModal;

        function _fnInitTask() {
            //编辑
            var taskId;
            if($stateParams.taskId){
                vm.taskId = $stateParams.taskId;
                TaskService.queryTaskDesc(vm.taskId).success(function (data) {
                    vm.task = data;
                    vm.task.stockOutDate = new Date(data.stockOutDate);
                    vm.usedTime = (data.usedTime/60).toFixed(1);
                    if(vm.usedTime < 1){
                        vm.usedTime = "小于1小时"
                    }else{
                        vm.usedTime = vm.usedTime + "小时"
                    }

                    taskId = data.id;
                }).then(function () {

                });
            }
            //出库负责人
            SampleUserService.query({}, onPersonSuccess, onError);
            function onPersonSuccess(data) {
                vm.personOptions = data;
            }

            _fnQueryTaskBoxes();
            _fnQueryStockOutList();
        }
        function _fnQueryTaskBoxes() {
            //获取冻存盒列表
            TaskService.queryTaskBox(vm.taskId).success(function (data) {
                vm.boxOptions.withOption('data', data);
                vm.boxInstance.rerender();
            });
        }
        function _fnQueryStockOutList() {
            //获取已出库列表
            TaskService.queryOutputList(vm.taskId).success(function (data) {
                vm.stockOutBoxList = data;
                //1702已出库
                vm.stockOutLen = _.filter(vm.stockOutBoxList,{status:"1702"}).length;
                vm.stockOutSampleOptions.withOption('data', vm.stockOutBoxList);
                vm.stockOutSampleInstance.rerender();
            });
        }
        _fnInitTask();
        //开始任务计时器
        var taskTimer;
        function startTimer() {
             taskTimer = setInterval(function(){
                TaskService.taskTimer(vm.taskId).then(function (res) {
                    vm.usedTime = (res.data.usedTime/60).toFixed(1);
                    if(vm.usedTime < 1){
                        vm.usedTime = "小于1小时"
                    }else{
                        vm.usedTime = vm.usedTime + "小时"
                    }
                });
            },90000);
        }
        startTimer();

        $scope.$on('$destroy',function(event,toState,toParams,fromState,fromParams){
            window.clearInterval(taskTimer);
        });
        vm.close = function () {
            $state.go('task-list');
        };
        //保存任务
        function _fnSaveTask() {
            BioBankBlockUi.blockUiStart();
            TaskService.saveTaskBox(vm.task).success(function (data) {
                BioBankBlockUi.blockUiStop();
                if(!stockOutFlag){
                    toastr.success("保存任务成功!");
                }
            }).error(function (data) {
                toastr.error("保存任务失败!");
                BioBankBlockUi.blockUiStop();
            });
        }
        function _fnPrintBox() {
            window.open ('/api/stock-out-frozen-boxes/task/' + vm.taskId +'/print');
        }
        //样本交接
        function _fnTakeOver() {
            var obj = {
                applyId:vm.task.stockOutApplyId,
                planId:vm.task.stockOutPlanId,
                taskId:vm.task.id
            };
            $state.go('take-over-new',obj);
            // BioBankBlockUi.blockUiStart();
            // TaskService.takeOver(vm.taskId).success(function (data) {
            //     BioBankBlockUi.blockUiStop();
            //     toastr.success("创建交接单成功!");
            // }).error(function (data) {
            //     toastr.error("创建交接单失败!");
            //     BioBankBlockUi.blockUiStop();
            // })
        }
        vm.personConfig1 = {
            valueField: 'id',
            labelField: 'userName',
            maxItems: 1,
            onChange:function (value) {
                vm.task.stockOutHeader1 = _.filter(vm.personOptions,{id:+value})[0].userName;
            }
        };
        vm.personConfig2 = {
            valueField: 'id',
            labelField: 'userName',
            maxItems: 1,
            onChange:function (value) {
                vm.task.stockOutHeader2 = _.filter(vm.personOptions,{id:+value})[0].userName;
            }

        };
        //出库状态
        vm.taskStatusOptions = MasterData.taskStatus;
        vm.taskStatusConfig = {
            valueField: 'id',
            labelField: 'name',
            maxItems: 1

        };


        //冻存盒列表
        vm.boxOptions = BioBankDataTable.buildDTOption("BASIC,SEARCHING", 371)
            .withOption('rowCallback', rowCallback);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "50").notSortable(),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('类型').withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置').withOption("width", "200").notSortable(),
            DTColumnBuilder.newColumn('countOfSample').withTitle('数量').withOption("width", "50").notSortable()
        ];
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $(nRow).bind('click', function() {
                var tr = this;
                $scope.$apply(function () {
                    rowClickHandler(tr,oData);
                });
            });
            if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
                $(nRow).addClass('rowLight');
                _fnLoadTubes();
            }
            return nRow;
        }
        var boxCode;
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            boxCode = data.frozenBoxCode;
            vm.sampleCode = boxCode+"-";
            vm.sampleCodeCopy = angular.copy(vm.sampleCode);
            vm.boxInTubes = [];
            _fnLoadTubes();
        }
        //加载管子
        var frozenBox;
        function _fnLoadTubes() {
            TaskService.queryTubes(boxCode,vm.taskId).success(function (data) {
                frozenBox = data;
                vm.frozenTubeDTOS = frozenBox.frozenTubeDTOS;
                _reloadTubesForTable(frozenBox);
            }).error(function (data) {

            });
        }
        var needScanTubesLen;
        //加载管子表控件
        function _reloadTubesForTable(box){
            var tableCtrl = _getSampleDetailsTableCtrl();
            var settings = {};
            var tubesInTable = [];
            var colHeaders = [];
            var rowHeaders = [];
            var row = +box.frozenBoxType.frozenBoxTypeRows;
            var col = +box.frozenBoxType.frozenBoxTypeColumns;
            for (var i=0; i < row; ++i){
                var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                if(i > 7){
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1);
                }
                var tubes = [];
                rowHeaders.push(pos.tubeRows);
                for (var j = 0; j < col; ++j){
                    pos.tubeColumns = j + 1 + "";
                    // if (colHeaders.length < settings.minCols){
                    colHeaders.push(pos.tubeColumns);
                    // }
                    var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                    var tube = _createTubeForTableCell(tubeInBox, i, j + 1, pos);
                    tubes.push(tube);
                }
                tubesInTable.push(tubes);

            }
            vm.tubes = tubesInTable;
            vm.tubeList = [];
            for(var m = 0; m < tubesInTable.length;m++){
                for(var n = 0; n < tubesInTable[m].length;n++){
                    vm.tubeList.push(tubesInTable[m][n]);
                }
            }
            //是否满盒出库
            needScanTubesLen = _.filter(vm.tubeList,{stockOutFlag:1}).length;
            if(needScanTubesLen == vm.tubeList.length){
                vm.boxInFullFlag = true;
                vm.boxInTubes = vm.tubeList;
            }else{
                vm.boxInFullFlag = false;
            }
            settings.rowHeaders = rowHeaders;
            settings.colHeaders = colHeaders;
            tableCtrl.updateSettings(settings);
            tableCtrl.loadData(tubesInTable);
        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, rowNO, colNO, pos){
            var tube = {
                id: null,
                sampleCode: "",
                sampleTempCode: "",
                status: "",
                memo: "",
                stockOutFlag:"",
                scanCodeFlag:"",
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns,
                repealReason:null,
                rowNO: rowNO,
                colNO: colNO
            };
            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;
                tube.sampleType = tubeInBox.sampleType;
                tube.sampleClassification = tubeInBox.sampleClassification;
                tube.stockOutFlag = tubeInBox.stockOutFlag;
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                tube.repealReason = tubeInBox.repealReason;
            }
            return tube;
        }

        //待出库样本(管子)
        _initSampleDetailsTable();
        function _initSampleDetailsTable() {
            var remarkArray;//批注
            vm.aRemarkArray = [];
            vm.sampleDetailsTableSettings = {
                // 点击表格外部时，表格内选项仍然能够选中
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,

                // 表格内单元格只能单选
                multiSelect: true,

                // 显示行和列的Title
                showColHeaders: true,
                showRowHeaders: true,

                // 默认每列的宽度
                colWidths: 30,
                // 行头的宽度
                rowHeaderWidth: 30,
                rowHeaderHeight: 20,

                // 默认行列Title
                rowHeaders: ['A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K'],
                colHeaders: ['1', '2', '3', '4', '5', '6', '7', '8', '9', '10'],

                // 默认显示的行和列的数量
                minRows: 10,
                minCols: 10,

                // 默认显示的数据
                data: [["", "", "", ""]],

                // 默认表格占用的宽高，超过范围显示滚动条
                // width: 584,
                // height: 380,
                // 是否自动拉伸单元格
                stretchH: 'all',
                //右下角拖拽
                fillHandle: false,
                editor: false,
                comments: true,
                onAfterSelectionEnd:function (row, col, row2, col2) {
                    var td = this;
                    remarkArray = this.getData(row,col,row2,col2);
                    vm.selectTubeArray = this.getSelected();


                    if(window.event && window.event.ctrlKey){
                        _fnRemarkSelectData(td,remarkArray,vm.selectTubeArray);
                    }else{
                        $(".tube-selected").remove();
                        vm.aRemarkArray = [];
                        _fnRemarkSelectData(td,remarkArray,vm.selectTubeArray);


                    }
                },
                // 单元格的渲染函数
                renderer: _customRenderer
            };
            // 渲染单元格
            function _customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = "relative";
                var txt = "";
                if(tube.memo && tube.memo != " "){
                    txt = tube.memo;

                }
                if(tube.repealReason && tube.repealReason != " "){
                    txt = tube.repealReason + tube.memo;
                }
                if(txt){
                    cellProperties.comment = txt;

                }
                //样本类型
                if(tube.sampleClassification){
                    td.style.backgroundColor = tube.sampleClassification.backColor;
                }else{
                    if(tube.sampleType){
                        td.style.backgroundColor = tube.sampleType.backColor;
                    }
                }
                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(tube.status){
                    // changeSampleStatus(tube.status,td)
                }
                var code = tube.sampleCode && tube.sampleCode != " " ? tube.sampleCode : tube.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word'
                }).appendTo(td);
                //待出库样本
                if(tube.stockOutFlag && tube.stockOutFlag == 1 && !tube.orderIndex){
                    var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px solid green;font-size:40px;color:rgba(0,128,0,0.3);text-align: center;">' +
                        '<i class="fa fa-question"></i>' +
                        '</div>';
                    $(txt).appendTo($div);
                }
                //申请撤销的样本标识
                if(tube.stockOutFlag && tube.stockOutFlag == 2){
                    $(".fa-question",td).remove();
                    var txt = '<div style="position: absolute;top:0;left:0;bottom:0;right:0;color:rgba(216,0,0,0.3);padding-left: 33%;font-size:42px"><i class="fa fa-close"></i></div>';
                    $(txt).appendTo($div);
                }
                //已扫码样本
                if(tube.scanCodeFlag){
                    $(".fa-question",td).remove();
                    var txt = '<div style="position: absolute;top:0;left:0;bottom:0;right:0;border:1px solid green;padding-top:10px;color:rgba(0,128,0,0.3);text-align:center;font-size:42px">'+tube.orderIndex+'</div>';
                    $(txt).appendTo($div);
                }
                if(tube.status == '3004'){
                    var txt = '<div style="position: absolute;bottom:2px;right:2px;width:10px;height:10px;border-radius:50%;background-color: red;"></div>';
                    $(txt).appendTo($div);
                }
            }
            //备注 选择单元格数据
            function _fnRemarkSelectData(td,remarkArray,selectTubeArrayIndex) {
                var txt = '<div class="tube-selected" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px dashed #5292F7;background-color: rgba(82,146,247,0.3)"></div>';
                for(var m = 0; m < remarkArray.length; m++){
                    for (var n = 0; n < remarkArray[m].length; n++){
                        vm.aRemarkArray.push(remarkArray[m][n]);
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
            //修改样本状态正常、空管、空孔、异常
            function changeSampleStatus(sampleStatus,td) {
                //异常
                if(sampleStatus == 3004){
                    td.style.backgroundColor = 'red';
                    td.style.border = '3px solid red;margin:-3px';
                }
            }

        }

        //装盒的样本
        vm.boxInTubes = [];

        var scanCodeTimer;
        vm.scanClick = function () {
            $("#focusTextarea").focus();
            vm.flagStatus = true;
            window.clearInterval(scanCodeTimer);
            scanCodeTimer = setInterval(function(){
                _fnScanCode();
            },500);
        };
        vm.scanCodeBlur = function () {
            vm.flagStatus = false;
            window.clearInterval(scanCodeTimer);
        };
        //扫码取样
        function _fnScanCode(){
            if (!vm.sampleCode) {
                return;
            }
            var tableCtrl = _getSampleDetailsTableCtrl();
            var sampleCode = vm.sampleCode.toUpperCase();
            vm.sampleCode = "";
            //获取待出库样本
            var stockOutTubes = _.filter(vm.tubeList,{stockOutFlag:1});

            //获取扫码取得样本
            var scanCodeTubes = _.filter(stockOutTubes,{sampleCode: sampleCode});
            if (!scanCodeTubes.length){
                scanCodeTubes = _.filter(stockOutTubes,{sampleTempCode: sampleCode});
            }

            if(!scanCodeTubes.length) {
                setTimeout(function(){
                    toastr.error("编码错误，请重新扫码!");
                },100);
                return;
            }
            var row = scanCodeTubes[0].rowNO;
            var col = scanCodeTubes[0].colNO;
            var boxInTubes = vm.boxInTubes || [];
            //装盒样本
            var len = _.filter(boxInTubes,{id: scanCodeTubes[0].id}).length;

            // 样本是否已经扫码
            if(len){
                setTimeout(function(){
                    toastr.error("扫码重复!");
                },100);
                return;
            }
            scanCodeTubes[0].orderIndex = boxInTubes.length + 1;
            vm.tubes[row][col-1].orderIndex = boxInTubes.length + 1;
            boxInTubes.push(scanCodeTubes[0]);
            //判断是否都全部扫码
            vm.allInFlag = boxInTubes.length == stockOutTubes.length;
            //扫码标识
            vm.tubes[row][col-1].scanCodeFlag = true;
            tableCtrl.loadData(vm.tubes);
            vm.boxInTubes = boxInTubes;
        }
        vm.box = {};
        //撤销
        function _fnRepealModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    items: function () {
                        return {
                            status:1
                        };
                    }
                }
            });

            modalInstance.result.then(function (repealReason) {
                var repealList = [];
                for(var i = 0; i < vm.aRemarkArray.length; i++){
                    if(vm.aRemarkArray[i].stockOutFlag){
                        vm.aRemarkArray[i].repealReason = repealReason;
                        repealList.push(vm.aRemarkArray[i]);
                    }
                }
                TaskService.repeal(repealList).success(function (data) {
                    toastr.success("申请撤销样本成功!");
                    _fnQueryTaskBoxes();
                    _fnLoadTubes();
                    vm.box.frozenBoxCode =boxCode;
                });
                vm.aRemarkArray = [];
                var tableCtrl = _getSampleDetailsTableCtrl();
                tableCtrl.loadData(vm.tubes);
            });
        }

        //1：未出库样本批注、2：已出库样本批注
        function _fnCommentModal(status,tempBoxId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/comment-modal.html',
                controller: 'TaskCommentModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    items: function () {
                        return {
                            status:status
                        };
                    }
                }
            });

            modalInstance.result.then(function (memo) {
                // 1：未出库样本批注、2：已出库样本批注
                if(status == 1){
                    for(var i = 0; i < vm.aRemarkArray.length; i++){
                        if(vm.aRemarkArray[i].sampleCode){
                            vm.aRemarkArray[i].memo = vm.aRemarkArray[i].memo + memo;
                        }
                    }
                    TaskService.fnNote(vm.aRemarkArray).success(function (data) {

                    });
                    vm.aRemarkArray = [];
                    var tableCtrl = _getSampleDetailsTableCtrl();
                    tableCtrl.loadData(vm.tubes);
                }else{
                    var obj  = {};
                    obj.id = tempBoxId;
                    obj.memo = memo;
                    TaskService.outputNote(obj).success(function (data) {
                        toastr.success("批注成功!");
                        _fnInitTask();
                    });
                }

            });
        }
        //异常
        function _fnAbnormal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    items: function () {
                        return {
                            status:2
                        };
                    }
                }
            });

            modalInstance.result.then(function (abnormalReason) {
                for(var i = 0; i < vm.aRemarkArray.length; i++){
                    if(vm.aRemarkArray[i].sampleCode){
                        vm.aRemarkArray[i].memo = vm.aRemarkArray[i].memo + abnormalReason;
                        vm.aRemarkArray[i].status = "3004";
                    }
                }
                TaskService.abnormal(vm.aRemarkArray).success(function (data) {
                });
                vm.aRemarkArray = [];
                var tableCtrl = _getSampleDetailsTableCtrl();
                tableCtrl.loadData(vm.tubes);
            });
        }
        //装盒
        function _fnBoxInModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/box-in-modal.html',
                controller: 'TaskBoxInModalController',
                controllerAs: 'vm',
                size: '90',
                resolve: {
                    items: function () {
                        return {
                            allInFlag:vm.allInFlag,
                            boxInTubes:vm.boxInTubes,
                            taskId:vm.taskId,
                            boxInFullFlag:vm.boxInFullFlag,
                            frozenBox:frozenBox
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {
                _fnInitTask();
                var tableCtrl = _getSampleDetailsTableCtrl();
                tableCtrl.loadData([[]]);
            });
        }

        // 获取待出库列表的控制实体
        function _getSampleDetailsTableCtrl() {
            vm.sampleDetailsTableCtrl = hotRegisterer.getInstance('sampleDetailsTable');
            return vm.sampleDetailsTableCtrl;
        }

        vm.selected = {};
        vm.selectAll = false;
        // 处理盒子选中状态
        vm.toggleAll = function (selectAll, selectedItems) {
            selectedItems = vm.selected;
            selectAll = vm.selectAll;
            var arrayId = [];
            for (var id in selectedItems) {
                arrayId.push(id);
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
            vm.strBoxIds = _.join(arrayId,",");
            if(!selectAll){
                vm.strBoxIds = "";
            }
        };

        vm.toggleOne = function (selectedItems) {
            var arrayId = [];
            for (var id in selectedItems) {
                if(selectedItems[id]){
                    arrayId.push(id);
                }
            }
            vm.strBoxIds = _.join(arrayId,",");
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        vm.selectAll = false;
                        return;
                    }
                }
            }
            vm.selectAll = true;

        };
        //已出库列表
        vm.stockOutSampleOptions = BioBankDataTable.buildDTOption("BASIC", 300)
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码').withOption("width", "100").notSortable(),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置').withOption("width", "220").notSortable(),
            DTColumnBuilder.newColumn('stockOutHandoverTime').withTitle('交接时间').withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", "50").notSortable(),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", "auto").notSortable(),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50").notSortable(),
            DTColumnBuilder.newColumn(null).withTitle('操作').notSortable().renderWith(actionsHtml).withOption("width", "80").notSortable(),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            vm.selectAll = false;
            var html = '';
            if(full.status == '1701'){
                html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
        }
        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '1701': status = '待出库';break;
                case '1702': status = '已出库';break;
                case '1703': status = '已交接';break;
            }
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            if(full.status == '1701'){
                return '<button type="button" ng-disabled="!vm.task.stockOutHeadId1 || !vm.task.stockOutHeadId2" class="btn btn-default btn-xs" ng-if="'+full.status+'== 1701" ng-click="vm.taskStockOutModal('+ full.id +')">' +
                    '出库' +
                    '</button> &nbsp;'+
                    '<button type="button" class="btn btn-default btn-xs"  ng-click="vm.commentModal(2,'+ full.id +')">' +
                    '批注' +
                    '</button>';
            }else{
                return '<button type="button" class="btn btn-default btn-xs"  ng-click="vm.commentModal(2,'+ full.id +')">' +
                    '批注' +
                    '</button>';
            }


        }

        //出库
        var stockOutFlag = false;
        function _fnTaskStockOutModal(frozenBoxIds) {
            stockOutFlag = true;
            _fnSaveTask();
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/task-stock-out-modal.html',
                controller: 'TaskStockOutModalController',
                controllerAs: 'vm',
                size: '90',
                resolve: {
                    items: function () {
                        return {
                            frozenBoxIds:frozenBoxIds || vm.strBoxIds,
                            taskId:vm.taskId,
                            stockOutHeadName1:vm.task.stockOutHeader1,
                            stockOutHeadName2:vm.task.stockOutHeader2
                        };
                    }
                }
            });
            modalInstance.result.then(function (data) {
                _fnQueryStockOutList();
                stockOutFlag = false;
            });
        }
        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }

    }
})();
