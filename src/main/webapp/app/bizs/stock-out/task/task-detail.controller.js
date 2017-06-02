/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskDetailController', TaskDetailController);

    TaskDetailController.$inject = ['$scope','$compile','$stateParams','$uibModal','hotRegisterer','$timeout','DTOptionsBuilder','DTColumnBuilder','TaskService','SampleUserService','MasterData','BioBankBlockUi','toastr','SampleService'];

    function TaskDetailController($scope,$compile,$stateParams,$uibModal,hotRegisterer,$timeout,DTOptionsBuilder,DTColumnBuilder,TaskService,SampleUserService,MasterData,BioBankBlockUi,toastr,SampleService) {
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
                    vm.task.stockOutDate = new Date(data.stockOutDate)
                    taskId = data.id;
                }).then(function () {

                })
            }
            //出库负责人
            SampleUserService.query({}, onPersonSuccess, onError);
            function onPersonSuccess(data) {
                vm.personOptions = data;
            }
            //获取冻存盒列表
            TaskService.queryTaskBox(vm.taskId).success(function (data) {
                vm.boxOptions.withOption('data', data);
                vm.boxInstance.rerender();
            })
            //获取已出库列表
            TaskService.queryOutputList(vm.taskId).success(function (data) {
                vm.stockOutSampleOptions.withOption('data', data);
                vm.stockOutSampleInstance.rerender();
            })


        }
        _fnInitTask();

        vm.personConfig = {
            valueField: 'id',
            labelField: 'userName',
            maxItems: 1

        };
        //出库状态
        vm.taskStatusOptions = MasterData.taskStatus;
        vm.taskStatusConfig = {
            valueField: 'id',
            labelField: 'name',
            maxItems: 1

        };

        function _fnSaveTask() {
            BioBankBlockUi.blockUiStart();
            TaskService.saveTaskBox(vm.task).success(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.success("保存任务成功!");
            }).error(function (data) {
                toastr.error("保存任务失败!");
                BioBankBlockUi.blockUiStop();
            })
        }
        function _fnPrintBox() {
            window.open ('/api/stock-out-frozen-boxes/task/' + vm.taskId +'/print');
        }
        //冻存盒列表
        vm.boxOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('rowCallback', rowCallback);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "50"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('出库样本量')
        ];
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $(nRow).bind('click', function() {
                var tr = this;
                $scope.$apply(function () {
                    rowClickHandler(tr,oData);
                })
            });
            if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
                $(nRow).addClass('rowLight');
            }
            return nRow;
        }
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            TaskService.queryTubes(data.frozenBoxCode,vm.taskId).success(function (data) {
                // vm.frozenTubeList = data;
                var box = data;
                _reloadTubesForTable(box)
            }).error(function (data) {

            })
        }
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
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1)
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
                vm.tubes = tubesInTable;
                vm.tubeList = [];
                for(var m = 0; m < tubesInTable.length;m++){
                    for(var n = 0; n < tubesInTable[m].length;n++){
                        vm.tubeList.push(tubesInTable[m][n]);
                    }
                }
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
                    var selectTubeArray = this.getSelected();
                    $(".tube-selected").remove();
                    _fnRemarkSelectData(td,remarkArray,selectTubeArray)
                    if(window.event && window.event.ctrlKey){

                    }
                },
                // 单元格的渲染函数
                renderer: _customRenderer
            };
            // 渲染单元格
            function _customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = "relative";
                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = tube.memo;
                }
                //样本类型
                if(tube.sampleClassification){
                    td.style.backgroundColor = tube.sampleClassification.backColor;
                }else{
                    if(vm.sampleType){
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
                //待出库样本
                if(tube.stockOutFlag){
                    var txt = '<div class="temp" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px solid green;"></div>';
                    $(txt).appendTo($div)
                }
                //已扫码样本
                if(tube.scanCodeFlag){
                    var txt = '<div style="position: absolute;top:0;left:0;bottom:0;right:0;color:rgba(0,128,0,0.3);padding-left: 33%;font-size:42px"><i class="fa fa-check"></i></div>'
                    $(txt).appendTo($div)
                }
            }
            //备注 选择单元格数据
            function _fnRemarkSelectData(td,remarkArray,selectTubeArray) {
                var txt = '<div class="tube-selected" style="position:absolute;top:0;bottom:0;left:0;right:0;border:1px dashed #5292F7;"></div>';
                for(var m = 0; m < remarkArray.length; m++){
                    for (var n = 0; n < remarkArray[m].length; n++){
                        vm.aRemarkArray.push(remarkArray[m][n])
                    }
                }
                for(var i = selectTubeArray[0];i <= selectTubeArray[2]; i++){
                    for(var j = selectTubeArray[1];  j <= selectTubeArray[3];j++)
                        $(td.getCell(i,j)).append(txt);
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
        //扫码取样
        //装盒的样本
        var boxInTubes = [];
        vm.sampleCode = "1494946117831-A3";
        function _fnScanCode(e){
            var tableCtrl = _getSampleDetailsTableCtrl();
            var keycode = window.event ? e.keyCode : e.which;
            if(keycode==13){
                //获取待出库样本
                var stockOutTubes = _.filter(vm.tubeList,{stockOutFlag:1});

                //获取扫码取得样本
                var scanCodeTubes = _.filter(stockOutTubes,{sampleTempCode:vm.sampleCode});
                if(scanCodeTubes.length){
                    var row = scanCodeTubes[0].rowNO;
                    var col = scanCodeTubes[0].colNO;
                    //扫码标识
                    vm.tubes[row][col-1].scanCodeFlag = true;
                    //装盒样本
                    var len = _.filter(boxInTubes,{sampleTempCode:vm.tubes[row][col-1].sampleTempCode}).length;
                    if(len){
                        return
                    }else{
                        boxInTubes.push(vm.tubes[row][col-1]);
                    }
                    //判断是否都全部扫码
                    if(boxInTubes.length != stockOutTubes.length){
                        vm.allInFlag = false;
                    }else{
                        vm.allInFlag = true;
                    }


                    tableCtrl.loadData(vm.tubes);
                }else{
                    toastr.error("编码错误，请重新扫码!")
                }

            }
        }
        //撤销
        function _fnRepealModal(status) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (repealReason) {

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
                        }
                    }
                }
            });

            modalInstance.result.then(function (memo) {
                if(status == 1){
                    for(var i = 0; i < vm.aRemarkArray.length; i++){
                        if(vm.aRemarkArray[i].sampleCode){
                            vm.aRemarkArray[i].memo = memo;
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
                        toastr.success("批注成功!")
                        _fnInitTask();
                    });
                }

            });
        }
        //异常
        function _fnAbnormal() {
            for(var i = 0; i < vm.aRemarkArray.length; i++){
                if(vm.aRemarkArray[i].sampleCode){
                    vm.aRemarkArray[i].status = "3004";
                }
            }
            TaskService.abnormal(vm.aRemarkArray).success(function (data) {

            });
            vm.aRemarkArray = [];
            var tableCtrl = _getSampleDetailsTableCtrl();
            tableCtrl.loadData(vm.tubes);
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
                            boxInTubes:boxInTubes,
                            taskId:vm.taskId
                        }
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
                arrayId.push(id)
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
            // console.log(JSON.stringify(selectedItems))
            var arrayId = [];
            for (var id in selectedItems) {
                if(selectedItems[id]){
                    arrayId.push(id)
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
        vm.stockOutSampleOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('stockOutHandoverTime').withTitle('出库交接时间'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('盒内样本量'),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn(null).withTitle('操作').notSortable().renderWith(actionsHtml),
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            vm.selectAll = false;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }
        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '1701': status = '待出库';break;
                case '1702': status = '已出库';break;
                case '1703': status = '已交接';break;
            }
            $('td:eq(2)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning btn-sm" ng-if="'+full.status+'== 1701" ng-click="vm.taskStockOutModal('+ full.id +')">' +
                '出库' +
                '</button> &nbsp;'+
            '<button type="button" class="btn btn-warning btn-sm"  ng-click="vm.commentModal(2,'+ full.id +')">' +
                '批注' +
                '</button>'
        }

        //出库
        function _fnTaskStockOutModal(frozenBoxIds) {
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
                            taskId:vm.taskId
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {
                _fnInitTask();
            });
        }




        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }

    }
})();
