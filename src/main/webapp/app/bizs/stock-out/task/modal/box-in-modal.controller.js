/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中装盒操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskBoxInModalController', TaskBoxInModalController);

    TaskBoxInModalController.$inject = ['$scope','$compile','$uibModalInstance','$uibModal','toastr','items','DTOptionsBuilder','DTColumnBuilder','TaskService','FrozenBoxTypesService','BioBankBlockUi'];

    function TaskBoxInModalController($scope,$compile,$uibModalInstance,$uibModal,toastr,items,DTOptionsBuilder,DTColumnBuilder,TaskService,FrozenBoxTypesService,BioBankBlockUi) {
        var vm = this;
        vm.tempBoxInstance = {};
        vm.sampleInstance = {};
        vm.box = {
            frozenBoxCode:null,
            frozenBoxType:{
                id:null
            },
            sampleCount:null,
            sampleOutCount:null,
            frozenTubeDTOS:[]
        };
        //待入库样本都已扫码
        vm.allInFlag = items.allInFlag;
        //扫码取样过的管子
        var boxInTubes = items.boxInTubes;
        var taskId = items.taskId;
        //临时盒list
        var boxList = [];

        //添加新的临时盒
        vm.addNewBox = _fnAddNewBox;
        //装盒
        vm.boxIn = _fnBoxIn;

        function _init() {
            //临时盒子
            TaskService.queryTempBoxes(taskId).success(function (data) {
                for(var i = 0; i < data.length; i++){
                    data[i].sampleCount = null;
                    boxList.push(data[i])
                }

                vm.tempBoxOptions.withOption('data', boxList);
            });

            //盒子类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['esc']);
                vm.box.frozenBoxType.id = vm.frozenBoxTypeOptions[0].id;
                vm.box.frozenBoxType.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
                vm.box.frozenBoxType.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;
            }
            //盒子类型
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onChange:function(value){
                    var boxType = _.filter(vm.frozenBoxTypeOptions, {id:+value})[0];
                    vm.box.frozenBoxType.id = value;
                    vm.box.frozenBoxType.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                    vm.box.frozenBoxType.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;
                }
            };
        }
        _init();
        //添加新盒
        function _fnAddNewBox(){
            // _reloadTubesForTable(angular.copy(vm.box));
            boxList.push(angular.copy(vm.box))
            console.log(JSON.stringify(vm.box))
            // vm.tempBoxInstance.DataTable.draw();
            vm.tempBoxOptions.withOption('data', boxList);
            vm.tempBoxInstance.rerender();

        }
        //加载管子表控件
        function _reloadTubesForTable(box){
            var row = box.frozenBoxType.frozenBoxTypeRows;
            var col = box.frozenBoxType.frozenBoxTypeColumns;
            var tubesInTable = [];
            for (var i=0; i < row; ++i){
                var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                if(i > 7){
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1)
                }
                var tubes = [];
                for (var j=0; j < col; ++j){
                    pos.tubeColumns = j + 1 + "";
                    var tubeInBox = _.filter(box.frozenTubeDTOS, pos)[0];
                    var tube = _createTubeForTableCell(tubeInBox,i, j + 1, pos);
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
            var obox = angular.copy(vm.box);
            obox.frozenTubeDTOS = vm.tubeList;
            obox.frozenBoxType = box.frozenBoxType;
            obox.frozenBoxCode = box.frozenBoxCode;
            if(box.id){
                obox.id = box.id;
            }
            boxList.push(obox);
        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox,rowNO, colNO, pos){
            var tube = {
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
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
            }
            return tube;
        }
        //临时盒子
        vm.tempBoxOptions = DTOptionsBuilder.newOptions()
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withOption('searching', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('rowCallback', rowCallback)
            .withOption('createdRow', createdRow)
        vm.tempBoxColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sampleCount').withTitle('盒内样本数')
        ];
        function createdRow(row, data, dataIndex) {
            var sampleCount =  data.frozenBoxType.frozenBoxTypeRows * data.frozenBoxType.frozenBoxTypeColumns
            var sampleOutCount = data.frozenTubeDTOS.length - (_.filter(data.frozenTubeDTOS,{sampleTempCode:""}).length);
            var str = sampleOutCount+"/"+sampleCount;
            $('td:eq(1)', row).html(str);
            $compile(angular.element(row).contents())($scope);
        }
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $(nRow).bind('click', function() {
                var tr = this;
                $scope.$apply(function () {
                    rowClickHandler(tr,oData);
                })
            });
            return nRow;
        }
        var selectBox;
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            selectBox = data;
            if(selectBox.frozenTubeDTOS.length){
                var len = selectBox.frozenTubeDTOS.length-1;
                _.orderBy(selectBox.frozenTubeDTOS, ['tubeRows'], ['esc']);
                vm.pos = selectBox.frozenTubeDTOS[len].tubeRows + (+selectBox.frozenTubeDTOS[len].tubeColumns+1);
            }else{
                vm.pos = "A1";
            }

            _FnPreassemble(vm.selectedTubes);
        }
        vm.posInit = function () {
            if(vm.pos){
               _FnPreassemble(vm.selectedTubes);
            }
        }


        //待装盒样本
        vm.selected = {};
        vm.selectAll = false;
        // 处理盒子选中状态
        vm.toggleAll = function (selectAll, selectedItems) {
            selectedItems = vm.selected;
            selectAll = vm.selectAll;
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
        };
        vm.selectedTubes = [];
        vm.toggleOne = function (selectedItems,tubeId) {
            vm.selectedTubes = [];
            _.each(boxInTubes, function(b){
                b.pos = null;
                b.checkedFlag = selectedItems[b.id];
                if (b.checkedFlag){
                    vm.selectedTubes.push(b);
                }
            });
            if(vm.selectedTubes.length == boxInTubes.length){
                vm.selectAll = true;
            }else{
                vm.selectAll = false;
            }
            if (!vm.pos){
                vm.sampleInstance.DataTable.draw();
                return;
            }
            //预装位置
            _FnPreassemble(vm.selectedTubes);
            vm.sampleInstance.DataTable.draw();

        };
        //预装位置
        function _FnPreassemble(selectedTubes) {
            var startPos = vm.pos;
            var startRow =  startPos.charAt(0);
            var startCol =  +startPos.substring(1);
            var pos={tubeRows:startRow,tubeColumns:startCol};
            var countOfCols = +selectBox.frozenBoxType.frozenBoxTypeRows;
            var countOfRows = +selectBox.frozenBoxType.frozenBoxTypeColumns;
            var countOfSelect = selectedTubes.length;
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
                selectedTubes[i].pos = pos;
                selectedTubes[i].tubeRows = pos.tubeRows;
                selectedTubes[i].tubeColumns = pos.tubeColumns;
                delete  selectedTubes[i].rowNO;
                delete  selectedTubes[i].colNO;
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
            function _checkPosition(){
                return true;
            }
        }
        //装盒
        var tempBoxList = [];
        function _fnBoxIn() {
            for(var i = 0; i < vm.selectedTubes.length;i++){
                selectBox.frozenTubeDTOS.push(vm.selectedTubes[i])
            }
            tempBoxList.push(selectBox);
            console.log(JSON.stringify(tempBoxList))
        }



        vm.sampleOptions = DTOptionsBuilder.newOptions()
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withOption('searching', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('createdRow', function(row, data, dataIndex) {
                if (data.pos){
                    $('td:eq(3)', row).html(data.pos.tubeRows+data.pos.tubeColumns);
                }
                $compile(angular.element(row).contents())($scope);
            })
            .withOption('headerCallback', function(header) {
                // if (!vm.headerCompiled) {
                //     // Use this headerCompiled field to only compile header once
                //     vm.headerCompiled = true;
                //
                // }
                $compile(angular.element(header).contents())($scope);
            });
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.sampleColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSampleSelectorRender),
            DTColumnBuilder.newColumn('sampleTempCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('pos').withTitle('预装位置'),
            DTColumnBuilder.newColumn('sampleCode').withTitle('标签'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('checkedFlag').notVisible()
        ];

        function _fnRowSampleSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = full.checkedFlag;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected,'+full.id+')">';
            return html;
        }
        function actionsHtml(data, type, full, meta) {
            return '<div ng-if="'+full.checkedFlag+'">'+
                '<button type="button" class="btn btn-warning btn-xs" >' +
                '   <i class="fa  fa-long-arrow-up"></i>' +
                '</button>&nbsp;'+
                '<button type="button" class="btn btn-warning btn-xs" >' +
                '   <i class="fa fa-long-arrow-down"></i>' +
                '</button>&nbsp;'+
                '</div>'
        }

        setTimeout(function () {
            for(var i = 0; i < boxInTubes.length; i++){
                boxInTubes[i].pos = "";
                boxInTubes[i].checkedFlag = false;
                boxInTubes[i].sampleTypeName = boxInTubes[i].sampleType.sampleTypeName;
            }
            vm.sampleOptions.withOption('data', boxInTubes);
        },500);


        vm.yes = function () {
            vm.allInFlag = true;
        };
        vm.ok = function () {
            var tempBoxListCopy = angular.copy(tempBoxList)
            for(var i =0; i < tempBoxListCopy.length; i++){
                delete tempBoxListCopy[i].sampleCount;
                delete tempBoxListCopy[i].sampleOutCount
            }
            TaskService.saveTempBoxes(taskId,tempBoxListCopy).success(function (data) {
                toastr.success("保存成功!");
                $uibModalInstance.close();
            });

        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }


    }
})();
