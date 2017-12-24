/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskDetailController', TaskDetailController)
        .controller('AffirmModalController', AffirmModalController);

    TaskDetailController.$inject = ['$scope','$state','$compile','$stateParams','$uibModal','hotRegisterer','Principal','$interval','DTColumnBuilder','TaskService','SampleUserService','MasterData','BioBankBlockUi','toastr','SampleService','BioBankDataTable'];
    AffirmModalController.$inject = ['$uibModalInstance','items'];
    function TaskDetailController($scope,$state,$compile,$stateParams,$uibModal,hotRegisterer,Principal,$interval,DTColumnBuilder,TaskService,SampleUserService,MasterData,BioBankBlockUi,toastr,SampleService,BioBankDataTable) {
        var vm = this;
        var modalInstance;
        vm.boxInstance = {};
        //出库
        vm.stockOutSampleInstance = {};
        //已扫码的样本
        vm.selectedInstance = {};
        vm.task = {};

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }


        //保存任务
        vm.saveTask = _fnSaveTask;
        //作废任务
        vm.invalidTask = _invalidTask;
        //样本交接
        vm.takeOver = _fnTakeOver;
        //打印取盒单
        vm.printBox = _fnPrintBox;
        //扫码取样
        vm.scanCode = _fnScanCode;
        //撤销
        vm.repealModal = _fnRepealModal;
        //撤销盒子
        vm.repealBox = _repealBox;
        //异常
        vm.abnormal = _fnAbnormal;
        //1未出库样本、2已出库样本批注
        vm.commentModal = _fnCommentModal;
        //冻存管装盒原盒出库 1.装盒 2.原盒
        vm.tubeStockOutBox = _fnTubeStockOutBox;
        //预装位置
        vm.prePos = _fnPrePos;
        //出库
        vm.taskStockOutModal = _fnTaskStockOutModal;
        //添加临时盒
        vm.selectTempBox = _fnSelectTempBox;
        //查看扫码的样本
        vm.viewSampleDesc = _fnViewSampleDesc;
        //删除选中的管子
        vm.delSelectedTube = _fnDelSelectedTube;
        //下一盒
        vm.nextBox = _fnNextBox;
        vm.closeSidebar = _closeSiderBar;
        //关闭侧边栏
        function _closeSiderBar() {
            vm.checked = false;
        }

        _fnInitTask();
        function _fnInitTask() {
            //编辑
            var taskId;
            if($stateParams.taskId){
                vm.taskId = $stateParams.taskId;
                TaskService.queryTaskDesc(vm.taskId).success(function (data) {
                    vm.task = data;
                    if(!vm.task.stockOutHeadId1){
                        if(vm.account.login != "admin"){
                            vm.task.stockOutHeadId1 = vm.account.id;
                        }
                    }
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
            _fnQueryUser();
            startTimer();
        }
        //获取任务冻存盒列表
        function _fnQueryTaskBoxes() {
            return TaskService.queryTaskBox(vm.taskId).success(function (data) {
                vm.stockOutbox = data;
                vm.boxOptions.withOption('data', vm.stockOutbox);
            });
        }
        //获取待出库列表
        function _fnQueryStockOutList() {

            TaskService.queryOutputList(vm.taskId).success(function (data) {
                vm.stockOutBoxList = data;
                //1702已出库
                vm.stockOutLen = _.filter(vm.stockOutBoxList,{status:"1702"}).length;
                vm.stockOutSampleOptions.withOption('data', vm.stockOutBoxList);
            });
        }
        //获取当前用户名
        function _fnQueryUser() {
            Principal.identity().then(function(account) {
                vm.account = account;
            });
        }
        //下一盒
        function _fnNextBox() {

            var selectRow = vm.boxInstance.DataTable.rows(".rowLight");
            var row = selectRow.nodes().to$();
            if(!row.length || row[0].rowIndex == vm.stockOutbox.length){
                selectRow = vm.boxInstance.DataTable.rows(".row0");
                row = selectRow.nodes().to$();
            }else{
                row = row.next();
            }

            row.click();
        }
        //开始任务计时器
        var taskTimer;
        function startTimer() {
            taskTimer = $interval(function() {
                TaskService.taskTimer(vm.taskId).then(function (res) {
                    vm.usedTime = (res.data.usedTime/60).toFixed(1);
                    if(vm.usedTime < 1){
                        vm.usedTime = "小于1小时"
                    }else{
                        vm.usedTime = vm.usedTime + "小时"
                    }
                });
            }, 90000);
            //  taskTimer = setInterval(function(){
            //     TaskService.taskTimer(vm.taskId).then(function (res) {
            //         vm.usedTime = (res.data.usedTime/60).toFixed(1);
            //         if(vm.usedTime < 1){
            //             vm.usedTime = "小于1小时"
            //         }else{
            //             vm.usedTime = vm.usedTime + "小时"
            //         }
            //     });
            // },90000);
        }


        $scope.$on('$destroy',function(){
            // window.clearInterval(taskTimer);
            $interval.cancel(taskTimer);
        });
        vm.close = function () {
            $state.go('task-list');
        };
        //保存任务信息
        function _fnSaveTask() {
            if(!isStockOutFlag){
                BioBankBlockUi.blockUiStart();
            }
            TaskService.saveTaskBox(vm.task).success(function (data) {
                BioBankBlockUi.blockUiStop();
                if(!isStockOutFlag){
                    toastr.success("保存任务成功!");
                }
            }).error(function (data) {
                toastr.error(data.message);
                BioBankBlockUi.blockUiStop();
            });
        }
        //作废任务
        function _invalidTask() {
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
                TaskService.invalidTask(vm.taskId,invalidReason).success(function (data) {
                    toastr.success("作废成功！");
                    $state.go('task-list');
                }).error(function (data) {
                    toastr.error(data.message);
                })
            }, function () {
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
        var dom = "<'row mt-10'<'col-xs-12 text-left pl-25' f> <'col-xs-1 text-right mb-5' > r> t <'row mt-0'<'col-xs-6'i> <'col-xs-6'p>>";
        vm.boxOptions = BioBankDataTable.buildDTOption("SORTING,SEARCHING", 410,5,dom)
            .withOption('order', [[0,'asc']])
            .withOption('rowCallback', rowCallback);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('冻存盒编码').withOption("width", "100").renderWith(_fnRowRender),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('类型').withOption("width", "50"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置').withOption("width", "auto"),
            DTColumnBuilder.newColumn('countOfSample').withTitle('数量').withOption("width", "50")
        ];
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $(nRow).addClass('row'+iDisplayIndex);
            $(nRow).bind('click', function() {
                var tr = this;
                rowClickHandler(tr,oData);
            });
            if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
                $(nRow).addClass('rowLight');
            }
            return nRow;
        }
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            vm.box = angular.copy(data);
            if(vm.tempBoxObj.projectCode && vm.tempBoxObj.projectCode != vm.box.projectCode){
                toastr.error("此盒项目编码与待装临时盒的项目编码不一致！");
            }
            var boxCode = data.frozenBoxCode;
            _fnLoadTubes(boxCode);
        }
        function _fnRowRender(data, type, full, meta) {
            var frozenBoxCode = '';
            if(full.frozenBoxCode1D){
                frozenBoxCode = "1D:"+full.frozenBoxCode1D +"<br>" + "2D:"+full.frozenBoxCode;
            }else{
                frozenBoxCode = "2D:"+full.frozenBoxCode;
            }
            return frozenBoxCode;
        }

        //加载管子
        var frozenBox;
        function _fnLoadTubes(boxCode) {
            TaskService.queryTubes(boxCode,vm.taskId).success(function (data) {
                frozenBox = data;
                vm.frozenTubeDTOS = frozenBox.frozenTubeDTOS;
                var len = _.filter(frozenBox.frozenTubeDTOS,{stockOutFlag:1}).length;
                //更新左侧列表中冻存盒列表中的样本量
                _.find(vm.stockOutbox,{"frozenBoxCode":frozenBox.frozenBoxCode}).countOfSample = len;
                vm.boxOptions.withOption('data', vm.stockOutbox);
                _reloadTubesForTable(frozenBox);
            }).error(function (data) {

            });
        }
        //扫码的数量
        var needScanTubesLen;
        //加载管子表控件
        function _reloadTubesForTable(box){
            vm.tubeList = [];
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
                    colHeaders.push(pos.tubeColumns);
                    var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                    var tube = _createTubeForTableCell(tubeInBox, i, j + 1, pos);
                    tubes.push(tube);
                }
                tubesInTable.push(tubes);

            }
            //撤销时
            for(var m = 0; m < tubesInTable.length;m++){
                for(var n = 0; n < tubesInTable[m].length;n++){
                    for(var k = 0; k < vm.boxInTubes.length; k++){
                        if(vm.boxInTubes[k].id == tubesInTable[m][n].id){
                            //扫码标识
                            tubesInTable[m][n].scanCodeFlag = true;
                            //扫码下标
                            tubesInTable[m][n].orderIndex = vm.boxInTubes[k].orderIndex;
                        }
                    }
                    if(tubesInTable[m][n].sampleCode || tubesInTable[m][n].sampleTempCode){
                        vm.tubeList.push(tubesInTable[m][n]);
                    }

                }
            }
            vm.tubes = tubesInTable;
            //是否满盒出库 stockOutFlag出库标识，1：要出库样本 0 不出库样本
            needScanTubesLen = _.filter(vm.tubeList,{stockOutFlag:1}).length;
            if(needScanTubesLen == vm.tubeList.length){
                vm.boxInFullFlag = true;
            }else{
                vm.boxInFullFlag = false;
            }
            settings.rowHeaders = rowHeaders;
            settings.colHeaders = colHeaders;
            tableCtrl.updateSettings(settings);
            tableCtrl.loadData(tubesInTable);
        }
        var orderIndex = 0;
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

            if(vm.boxInTubes.length){
                var indexFlag = false;
                for(var i = 0; i < vm.boxInTubes.length; i++){
                    //撤销
                    if(tube.stockOutFlag == "2"){
                        if(vm.boxInTubes[i].id == tube.id){
                            orderIndex = vm.boxInTubes[i].orderIndex;
                            _.pullAt(vm.boxInTubes,i);
                            indexFlag = true;
                        }
                    }
                }

            }
            if(orderIndex){
                for(var j = 0; j < vm.boxInTubes.length; j++){
                    if(vm.boxInTubes[j].orderIndex > orderIndex){
                        vm.boxInTubes[j].orderIndex = vm.boxInTubes[j].orderIndex - 1;
                    }
                }
                orderIndex = 0;
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
                outsideClickDeselectsCache: true,
                outsideClickDeselects: true,

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
                beforeOnCellMouseDown: function (event, coords, element) {
                    var self = this;
                    if(coords.row == "-1" && coords.col == "-1" && $(element).is("th")){
                        var row2 = this.countRows()-1;
                        var col2 = this.countCols()-1;
                        setTimeout(function(){
                            self.selectCell(0,0,row2,col2,true,true);
                        },200);
                    }
                },
                // 单元格的渲染函数
                renderer: _customRenderer,
                beforeKeyDown:function (event) {
                    if(event.keyCode == 46){
                        event.stopImmediatePropagation();
                    }

                }
            };
            // 渲染单元格
            function _customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = "relative";
                var txt = "";
                if(tube.memo && tube.memo != " "){
                    txt = tube.memo;
                }else{
                    tube.memo = ""
                }
                if(tube.repealReason && tube.repealReason != " "){
                    txt = tube.repealReason + "\t\n" +tube.memo;
                }
                if(txt){
                    cellProperties.comment = {value:txt};

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
                    changeSampleStatus(tube.status,td)
                }
                var code = tube.sampleCode && tube.sampleCode != " " ? tube.sampleCode : tube.sampleTempCode;
                $(td).html("");
                var $div = $("<div/>").html(code).css({
                    'line-height': '20px',
                    'word-wrap': 'break-word'
                }).appendTo(td);
                $div = $("<div  class='tube-status'/>").html(tube.status).appendTo(td);
                //待出库样本
                if(tube.stockOutFlag && tube.stockOutFlag == 1 && !tube.orderIndex){
                    var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:2px solid green;font-size:40px;color:rgba(0,128,0,0.3);background-color: rgba(0,128,0,0.15);text-align: center;">' +
                        // '<i class="fa fa-question"></i>' +
                        '</div>';
                    $(txt).appendTo(td);
                }
                //申请撤销的样本标识
                if(tube.stockOutFlag && tube.stockOutFlag == 2){
                    $(".fa-question",td).remove();
                    var txt = '<div style="position: absolute;top:0;left:0;bottom:0;right:0;color:rgba(216,0,0,0.3);padding-left: 33%;font-size:42px"><i class="fa fa-close"></i></div>';
                    $(txt).appendTo(td);
                }
                //已扫码样本
                if(tube.scanCodeFlag){
                    $(".fa-question",td).remove();
                    var txt = '<div style="position: absolute;top:0;left:0;bottom:0;right:0;border:2px solid green;color:rgba(0,128,0,0.6);background-color: rgba(0,128,0,0.15);text-align:center;font-size:42px">'+tube.orderIndex+'</div>';
                    $(txt).appendTo(td);
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
                //正常
                if(sampleStatus == 3001){
                    $(td).removeClass("error-tube-color");
                }
                //空管
                if(sampleStatus == 3002){
                    $(td).addClass("empty-tube-color");
                }
                //空孔
                if(sampleStatus == 3003){
                    $(td).removeClass("empty-tube-color");
                    $(td).addClass("empty-hole-color");
                }
                //异常
                if(sampleStatus == 3004){
                    $(td).removeClass("empty-hole-color");
                    $(td).addClass("error-tube-color");
                }
            }

        }
        /*----------------------扫码-------------------------*/
        //装盒的样本
        vm.boxInTubes = [];
        var scanCodeTimer;
        //扫码操作
        vm.scanClick = function () {
            $("#focusTextarea").focus();
            //操作按钮切换
            vm.flagStatus = true;
            //定时器
            window.clearInterval(scanCodeTimer);

            scanCodeTimer = setInterval(function(){
                _fnScanCode();
            },500);
        };
        //取消扫码
        vm.scanCodeBlur = function () {
            vm.flagStatus = false;
            window.clearInterval(scanCodeTimer);
        };
        //扫码取样
        function _fnScanCode(){
            if (!vm.sampleCode) {
                return;
            }
            //扫码的数量与临时盒的管子容量比较
            if(vm.boxInTubes.length == vm.totalLen){
                toastr.error("扫码失败，临时盒已满，请先出库！");
                return
            }
            var tableCtrl = _getSampleDetailsTableCtrl();
            var sampleCode = vm.sampleCode.toUpperCase();
            vm.sampleCode = "";
            //获取待出库样本
            var stockOutTubes = _.filter(vm.tubeList,{stockOutFlag:1});

            _.forEach(stockOutTubes, function(sample) {
                if(sample.sampleCode){
                    sample.sampleCode = sample.sampleCode.toUpperCase();
                }else{
                    sample.sampleCode = sample.sampleTempCode.toUpperCase();
                }

            });
            //获取扫码取得样本
            var scanCodeTubes = _.find(stockOutTubes,{"sampleCode": sampleCode});
            if (!scanCodeTubes){
                scanCodeTubes = _.find(stockOutTubes,{"sampleTempCode": sampleCode});
            }
            if(!scanCodeTubes) {
                setTimeout(function(){
                    toastr.error("编码错误，请重新扫码!");
                },100);
                return;
            }
            if(vm.tempBoxObj.projectCode && vm.tempBoxObj.projectCode != frozenBox.projectCode){
                toastr.error("项目编码不一致，请重新扫码!");
                return;
            }
            var boxInTubes = angular.copy(vm.boxInTubes);
            //装盒样本
            var len = _.filter(boxInTubes,{id: scanCodeTubes.id}).length;

            // 样本是否已经扫码
            if(len){
                setTimeout(function(){
                    toastr.error("扫码重复!");
                },100);
                return;
            }
            scanCodeTubes.orderIndex = boxInTubes.length + 1;
            scanCodeTubes.pos = "";
            var row = scanCodeTubes.rowNO;
            var col = scanCodeTubes.colNO;
            vm.tubes[row][col-1].orderIndex = boxInTubes.length + 1;
            boxInTubes.push(scanCodeTubes);

            vm.tempBoxObj.projectCode = frozenBox.projectCode;

            //判断是否都全部扫码
            vm.allInFlag = boxInTubes.length == stockOutTubes.length;
            //扫码标识
            vm.tubes[row][col-1].scanCodeFlag = true;
            vm.boxInTubes = boxInTubes;
            tableCtrl.loadData(vm.tubes);
            if(vm.tempBoxObj.frozenTubeDTOS.length){
                //预装位置
                _fnPrePos();
            }
           }
        //查看已扫码
        function _fnViewSampleDesc() {
            vm.checked = true;
            vm.selectedOptions.withOption('data', vm.boxInTubes);
        }
        //删除已选中的样本
        function _fnDelSelectedTube(tubeId) {
            var boxCode = vm.box.frozenBoxCode;
            orderIndex = _.find(vm.boxInTubes,{id:tubeId}).orderIndex;
            _.remove(vm.boxInTubes,{id:tubeId});
            _fnLoadTubes(boxCode);
            //预装位置
            _fnPrePos();
        }


        vm.box = {};
        //撤销
        function _fnRepealModal() {
            var len = _.filter(vm.aRemarkArray,{stockOutFlag:1}).length;
            if(!len){
                toastr.error("请选择要出库的样本！");
                return;
            }
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
                controllerAs: 'vm',
                size: 'md',
                backdrop:'static',
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
                var boxCode = vm.box.frozenBoxCode;
                TaskService.repeal(vm.taskId,repealList).success(function (data) {
                    toastr.success("申请撤销样本成功!");
                    _fnLoadTubes(boxCode);
                    vm.box.frozenBoxCode =boxCode;
                });
                vm.aRemarkArray = [];
                var tableCtrl = _getSampleDetailsTableCtrl();
                tableCtrl.loadData(vm.tubes);
            });
        }
        //整盒撤销
        function _repealBox() {
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
                            status: 1
                        };
                    }
                }
            });
            modalInstance.result.then(function (repealReason) {
                var obj = {
                    frozenBoxIds:[],
                    repealReason:repealReason
                };
                obj.frozenBoxIds.push(vm.box.id);

                TaskService.repealBox(vm.taskId,obj).success(function (data) {
                    toastr.success("撤销成功!");

                    _.remove(vm.stockOutbox,{id:vm.box.id});
                    setTimeout(function () {
                        var box = {
                            frozenBoxType:{
                                frozenBoxTypeRows:10,
                                frozenBoxTypeColumns:10
                            }
                        };
                        _reloadTubesForTable(box);
                        vm.boxOptions.withOption('data', vm.stockOutbox);


                    },500);
                });
            }, function () {
            });
        }
        //批注 1：未出库样本、2：已出库样本
        function _fnCommentModal(status,boxId,memo) {
            if(status == 1){
                if(!vm.aRemarkArray.length){
                    toastr.error("请选择样本!");
                    return;
                }
                var memo;
                if(vm.aRemarkArray.length == 1){
                    memo = vm.aRemarkArray[0].memo;
                }
            }
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/comment-modal.html',
                controller: 'TaskCommentModalController',
                controllerAs: 'vm',
                size: 'md',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            status:status,
                            memo:memo
                        };
                    }
                }
            });

            modalInstance.result.then(function (memo) {
                // 1：未出库样本批注、2：已出库样本批注
                if(status == 1){
                    if(vm.aRemarkArray.length == 1){
                        vm.aRemarkArray[0].memo = memo
                    }else{
                        for(var i = 0; i < vm.aRemarkArray.length; i++){
                            if(vm.aRemarkArray[i].sampleCode || vm.aRemarkArray[i].sampleTempCode){
                                // if(vm.aRemarkArray[i].memo){
                                //     vm.aRemarkArray[i].memo = vm.aRemarkArray[i].memo + "\r\n"+memo;
                                // }else{
                                //     vm.aRemarkArray[i].memo = memo;
                                // }
                                vm.aRemarkArray[i].memo = memo;
                            }
                        }

                    }
                    TaskService.fnNote(vm.aRemarkArray).success(function (data) {
                        vm.aRemarkArray = [];
                        var tableCtrl = _getSampleDetailsTableCtrl();
                        tableCtrl.loadData(vm.tubes);
                    }).error(function (data) {
                        toastr.error(data.message);
                    });

                }else{
                    var obj  = {};
                    obj.id = boxId;
                    obj.memo = memo;
                    TaskService.outputNote(obj).success(function (data) {
                        toastr.success("备注成功!");

                        //成功后更新已出库列表中的样本量
                        _.find(vm.stockOutBoxList,{"id":boxId}).memo = memo;
                        vm.stockOutSampleOptions.withOption('data', vm.stockOutBoxList);

                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }

            });
        }
        //异常
        function _fnAbnormal() {
            if(!vm.aRemarkArray.length){
                toastr.error("请选择样本!");
                return;
            }
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
                controllerAs: 'vm',
                size: 'md',
                backdrop:'static',
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
                    if(vm.aRemarkArray[i].sampleCode || vm.aRemarkArray[i].sampleTempCode){
                        vm.aRemarkArray[i].memo = vm.aRemarkArray[i].memo + "\r\n"+ abnormalReason;
                        vm.aRemarkArray[i].status = "3004";
                    }
                }
                TaskService.abnormal(vm.aRemarkArray).success(function (data) {
                    vm.aRemarkArray = [];
                    var tableCtrl = _getSampleDetailsTableCtrl();
                    tableCtrl.loadData(vm.tubes);
                }).error(function (data) {
                    toastr.error(data.message);
                });

            });
        }
        //管子出库操作 1.装盒 2.原盒
        function _fnTubeStockOutBox(status) {
            isStockOutFlag = true;
            //保存任务信息
            _fnSaveTask();
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'affirmModal.html',
                controller: 'AffirmModalController',
                controllerAs: 'vm',
                size: 'md',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            status:status
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {
                if(status == 1){
                    var tempBoxList = [];
                    vm.tempBoxObj.frozenTubeDTOS = vm.boxInTubes;
                    vm.tempBoxObj.countOfSample = vm.boxInTubes.length;
                    vm.tempBoxObj.sampleTypeCode = "98";
                    vm.tempBoxObj.sampleTypeName = "98";
                    vm.tempBoxObj.position = null;
                    vm.tempBoxObj.stockOutHandoverTime = null;
                    vm.tempBoxObj.memo = "";
                    vm.tempBoxObj.status = "1701";

                    tempBoxList.push(vm.tempBoxObj);
                    TaskService.saveTempBoxes(vm.taskId,tempBoxList).success(function (data) {
                        toastr.success("装盒成功!");
                        //更新待出库冻存盒列表
                        _fnReloadWaitingStockOutBox(data);
                        //更新左侧任务冻存盒列表
                        _fnReloadWaitingTaskBox();

                        vm.boxInTubes.length = 0;
                        vm.tempBoxObj = {
                            projectId : null,
                            projectCode:null,
                            frozenTubeDTOS :[]
                        };
                        //样本总量
                        vm.totalLen = undefined;
                        //更新右边管子信息
                        var selectRow = vm.boxInstance.DataTable.rows(".rowLight");
                        var row = selectRow.nodes().to$();
                        row.click();







                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }else{
                    var selfBoxList = [];
                    selfBoxList.push(frozenBox);
                    var frozenBoxCode = frozenBox.frozenBoxCode;

                    TaskService.saveTempBoxes(vm.taskId,selfBoxList).success(function (data) {
                        toastr.success("出库成功!");
                        //原盒出库完成后，移除任务冻存盒列表中的盒子数据
                        _.remove(vm.stockOutbox,{"frozenBoxCode":frozenBoxCode});
                        vm.boxOptions.withOption('data', vm.stockOutbox);
                        //清空右侧冻存管数据
                        var tableCtrl = _getSampleDetailsTableCtrl();
                        tableCtrl.loadData([[]]);

                        //成功后更新已出库列表中的样本量
                        data[0].position = "";
                        data[0].stockOutHandoverTime = "";
                        data[0].countOfSample = vm.box.countOfSample;
                        vm.stockOutBoxList.push(data[0]);
                        vm.stockOutSampleOptions.withOption('data', vm.stockOutBoxList);


                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }


            });
        }
        //更新待出库冻存盒列表
        function _fnReloadWaitingStockOutBox(data) {
            //成功后更新已出库列表中的样本量
            var frozenBoxCode = data[0].frozenBoxCode;
            var frozenBoxId = data[0].id;
            var len1 = _.filter(vm.stockOutBoxList,{"frozenBoxCode":frozenBoxCode}).length;
            if(len1){
                _.forEach(vm.stockOutBoxList,function (box) {
                    if(box.frozenBoxCode == frozenBoxCode){
                        box.countOfSample += vm.boxInTubes.length;
                    }
                });
            }else{
                vm.tempBoxObj.id = frozenBoxId;
                vm.stockOutBoxList.push(vm.tempBoxObj);
            }
            vm.stockOutSampleOptions.withOption('data', vm.stockOutBoxList);
        }
        //更新左侧任务冻存盒列表
        function _fnReloadWaitingTaskBox() {
            //更新左侧列表中冻存盒列表中的样本量
            var len2 = _.filter(vm.stockOutbox,{"frozenBoxCode":vm.box.frozenBoxCode}).length;
            if(len2){
                _.forEach(vm.stockOutbox,function (box) {
                    if(box.frozenBoxCode == vm.box.frozenBoxCode){
                        box.countOfSample -= vm.boxInTubes.length;
                    }
                });
            }
            vm.boxOptions.withOption('data', vm.stockOutbox);
        }
        //预装位置
        function _fnPrePos() {
            //开始位置
            var startPos = vm.tempBoxObj.startPos;
            var startRow =  startPos.charAt(0);//行
            var startCol =  +startPos.substring(1);//列
            var pos={tubeRows:startRow,tubeColumns:startCol};
            var countOfCols = +frozenBox.frozenBoxTypeRows;//总行数
            var countOfRows = +frozenBox.frozenBoxTypeColumns;//总列数
            var countOfSelect = vm.boxInTubes.length;
            for(var i = 0; i < countOfSelect; i++){
                // 检查盒内位置是否已经有管子
                var isValidPosition = _checkPosition(pos);
                while(pos && !isValidPosition){
                    pos = _moveToNextPos(pos, countOfCols, countOfRows);
                    isValidPosition = _checkPosition(pos);
                }

                if (!pos){
                    console.log("盒子空间不足");
                    break;
                }

                // 修改管子的POS
                vm.boxInTubes[i].pos = pos;
                vm.boxInTubes[i].tubeRows = pos.tubeRows;
                vm.boxInTubes[i].tubeColumns = pos.tubeColumns;
                delete  vm.boxInTubes[i].rowNO;
                delete  vm.boxInTubes[i].colNO;
                pos = _moveToNextPos(pos, countOfCols, countOfRows);
            }
            function _moveToNextPos(pos, maxCol, maxRow){
                var row = pos.tubeRows.charCodeAt(0) - 65;
                var col = +pos.tubeColumns;
                col++;
                if (col > maxCol){
                    col = col - maxCol;
                    row++;
                }
                if (row == maxRow){
                    return null;
                }

                return {
                    tubeRows: String.fromCharCode(row+65),
                    tubeColumns: col
                };
            }
            function _checkPosition(pos){
                pos.tubeColumns = pos.tubeColumns+"";
               var tubeId =  _.find(vm.tempBoxObj.frozenTubeDTOS,pos).id;
               if(tubeId){
                   return false;
               }
                return true;
            }
        }
        //初始化内容
        function _fnInitial() {
            //扫码的样本数
            vm.sampleOutCount = "";
            //临时盒总共的样本数
            vm.totalLen = undefined;
            vm.boxInTubes = [];
            vm.tempBoxObj = {
                frozenTubeDTOS : []
            };
            // _fnInitTask();
            var tableCtrl = _getSampleDetailsTableCtrl();
            tableCtrl.loadData([[]]);
        }

        //已扫码的样本
        vm.selectedOptions = BioBankDataTable.buildDTOption("SORTING", 371)
            .withOption('rowCallback', selectedSampleRowCallback);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn('id').withOption("width", "40").notSortable().withOption('searchable',false).withTitle('序号'),
            DTColumnBuilder.newColumn('sampleCode').withTitle('冻存管编码').withOption("width", 'auto').notSortable(),
            DTColumnBuilder.newColumn('pos').withTitle('预装位置').withOption("width", 'auto').notSortable(),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "40").withOption('searchable',false).notSortable().renderWith(selectedSampleActionsHtml)

        ];
        function selectedSampleRowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull) {
            $('td:first', nRow).html(iDisplayIndex+1);
            if (oData.pos){
                $('td:eq(2)', nRow).html(oData.pos.tubeRows+oData.pos.tubeColumns);
            }
            $compile(angular.element(nRow).contents())($scope);
        }
        function selectedSampleActionsHtml(data, type, full, meta) {
            var html = '';
            html = '<button type="button" class="btn btn-default btn-xs" ng-click="vm.delSelectedTube('+full.id+')">' +
                '   <i class="fa fa-times"></i>' +
                '</button>&nbsp;';
            return html;
        }

        //选择临时盒
        vm.tempBoxObj = {
            sampleTypeCode:"98",
            sampleTypeName:"98",
            frozenTubeDTOS :[]
        };
        function _fnSelectTempBox() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/task-temp-box-modal.html',
                controller: 'TaskTempBoxModalController',
                controllerAs: 'vm',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            taskId:vm.taskId,
                            tempBox:vm.tempBoxObj,
                            projectCode:vm.tempBoxObj.projectCode
                        };
                    }
                }
            });

            modalInstance.result.then(function (tempBox) {
                if(vm.tempBoxObj.projectCode && !tempBox.projectCode){
                    tempBox.projectCode = vm.tempBoxObj.projectCode;
                }
                vm.tempBoxObj = tempBox;
                vm.totalLen = vm.tempBoxObj.frozenTubeDTOS.length;
                var remainLen = _.filter(vm.tempBoxObj.frozenTubeDTOS,{sampleCode:"",sampleTempCode:""}).length;
                //已有样本数
                vm.sampleOutCount = vm.totalLen - remainLen;
                //更换临时盒时，预装位置也要变更
                if(vm.boxInTubes.length){
                    //预装位置
                    _fnPrePos();
                }

            });
        }

        // 获取待出库列表的控制实体
        function _getSampleDetailsTableCtrl() {
            vm.sampleDetailsTableCtrl = hotRegisterer.getInstance('sampleDetailsTable');
            return vm.sampleDetailsTableCtrl;
        }


        /*----------------------扫码 end-------------------------*/
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
            .withOption('order', [[1,'asc']])
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "100"),
            DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('一维编码').withOption("width", "100"),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "80"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置').withOption("width", "220"),
            DTColumnBuilder.newColumn('stockOutHandoverTime').withTitle('交接时间').withOption("width", "80"),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", "auto"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
            DTColumnBuilder.newColumn(null).withTitle('操作').notSortable().renderWith(actionsHtml).withOption("width", "80"),
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
            $('td:eq(8)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            if(full.status == '1701'){
                return '<button type="button" ng-disabled="!vm.task.stockOutHeadId1 || !vm.task.stockOutHeadId2" class="btn btn-default btn-xs" ng-if="'+full.status+'== 1701" ng-click="vm.taskStockOutModal('+ full.id +')">' +
                    '出库' +
                    '</button> &nbsp;'+
                    '<button type="button" class="btn btn-default btn-xs"  ng-click="vm.commentModal(2,'+ full.id +')">' +
                    '备注' +
                    '</button>';
            }else{
                return '<button type="button" class="btn btn-default btn-xs"  ng-click="vm.commentModal(2,'+ full.id +')">' +
                    '备注' +
                    '</button>';
            }


        }

        //出库
        var isStockOutFlag = false;
        function _fnTaskStockOutModal(frozenBoxIds) {
            if(vm.task.stockOutHeadId1 == vm.task.stockOutHeadId2){
                toastr.error("出库负责人不能为同一人！");
                return;
            }
            isStockOutFlag = true;
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
                isStockOutFlag = false;
            });
        }
        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }

    }
    function AffirmModalController($uibModalInstance,items) {
        var vm = this;
        //1.装盒 2.原盒
        vm.status = items.status;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
