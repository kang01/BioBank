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
        vm.task = {};

        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间

        function openCalendar(date) {
            vm.datePickerOpenStatus[date] = true;
        }

        vm.saveTask = _fnSaveTask;

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
            // $('td:first', nRow).html(iDisplayIndex+1);
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
            TaskService.queryTubes(data.frozenBoxCode).success(function (data) {
                vm.frozenTubeList = data;
                _reloadTubesForTable(vm.frozenTubeList)
            }).error(function (data) {

            })
        }
        //加载管子表控件
        function _reloadTubesForTable(frozenTubeList){
            var tableCtrl = _getSampleDetailsTableCtrl();
            var settings = {};
            var tubesInTable = [];
            var colHeaders = [];
            var rowHeaders = [];
            for (var i=0; i < 10; ++i){
                var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                if(i > 7){
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1)
                }
                var tubes = [];
                rowHeaders.push(pos.tubeRows);
                for (var j=0; j <10; ++j){
                    pos.tubeColumns = j + 1 + "";
                    // if (colHeaders.length < settings.minCols){
                    colHeaders.push(pos.tubeColumns);
                    // }
                    var tubeInBox = _.filter(frozenTubeList, pos)[0];
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

        //扫码取样
        vm.scanCode = _fnScanCode;
        //异常、撤销
        vm.abnormalModal = _fnAbnormalModal;
        //未出库样本、已出库样本批注
        vm.commentModal = _fnCommentModal;
        //装盒
        vm.boxInModal = _fnBoxInModal;
        //出库
        vm.taskStockOutModal = _fnTaskStockOutModal;
        //待出库样本(管子)
        _initSampleDetailsTable();
        function _initSampleDetailsTable() {
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
                    // console.log(this.getData(row,col))
                    console.log(this.getSelected());
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
        //1：异常、2：撤销
        function _fnAbnormalModal(status) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/abnormal-recall-modal.html',
                controller: 'AbnormalRecallModalController',
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

            modalInstance.result.then(function (data) {

            });
        }

        //1：未出库样本批注、2：已出库样本批注
        function _fnCommentModal(status) {
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

            modalInstance.result.then(function (data) {

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
                            boxInTubes:boxInTubes
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }

        //出库
        function _fnTaskStockOutModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/box-in-modal.html',
                controller: 'TaskBoxInModalController',
                controllerAs: 'vm',
                size: 'lg',
                resolve: {
                    items: function () {
                        return {}
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }

        // 获取待出库列表的控制实体
        function _getSampleDetailsTableCtrl() {
            vm.sampleDetailsTableCtrl = hotRegisterer.getInstance('sampleDetailsTable');
            return vm.sampleDetailsTableCtrl;
        }
        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }

    }
})();
