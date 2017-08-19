/**
 * Created by gaokangkang on 2017/5/12.
 * 任务中装盒操作
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskBoxInModalController', TaskBoxInModalController);

    TaskBoxInModalController.$inject = ['$scope','$compile','$uibModalInstance','$uibModal','toastr','items','DTOptionsBuilder','DTColumnBuilder','TaskService','FrozenBoxTypesService','BioBankBlockUi','BoxCodeIsRepeatService','BioBankDataTable'];

    function TaskBoxInModalController($scope,$compile,$uibModalInstance,$uibModal,toastr,items,DTOptionsBuilder,DTColumnBuilder,TaskService,FrozenBoxTypesService,BioBankBlockUi,BoxCodeIsRepeatService,BioBankDataTable) {
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
        vm.selectedTubesLen = boxInTubes.length;
        var boxInTubesCopy = angular.copy(boxInTubes);
        vm.boxCopyLen = boxInTubesCopy.length;
        var taskId = items.taskId;
        //是否整盒出库
        vm.boxInFullFlag = items.boxInFullFlag;
        var frozenBox = items.frozenBox;
        //临时盒list
        var boxList = [];

        //添加新的临时盒
        vm.addNewBox = _fnAddNewBox;
        //装盒
        vm.boxIn = _fnBoxIn;
        //复原
        vm.recover = _fnRecover;
        //整盒出库
        vm.stockOutBox = _fnStockOutBox;
        vm.cancelStockOutBox = _fnCancelStockOutBox;

        function _init() {
            //临时盒子
            TaskService.queryTempBoxes(taskId).success(function (data) {
                for(var i = 0; i < data.length; i++){
                    data[i].sampleCount = null;
                    boxList.push(data[i]);
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

            BoxCodeIsRepeatService.getByCode(vm.box.frozenBoxCode).then(function (data) {
                vm.isRepeat = data;
                if (vm.isRepeat){
                    toastr.error("冻存盒编码已存在!");
                    vm.box.frozenBoxCode = "";
                    return;
                }
                var len = _.filter(boxList,{frozenBoxCode:vm.box.frozenBoxCode}).length;
                if(len === 0){
                    boxList.push(angular.copy(vm.box));
                    vm.tempBoxOptions.withOption('data', boxList);
                    vm.tempBoxInstance.rerender();
                }else{
                    toastr.error("冻存盒编码已存在!");
                }

            });


        }
        //复原
        function _fnRecover() {
            boxList = [];
            vm.selectAll = false;
            vm.pos = "";
            //临时盒子
            TaskService.queryTempBoxes(taskId).success(function (data) {
                for(var i = 0; i < data.length; i++){
                    data[i].sampleCount = null;
                    boxList.push(data[i]);
                }
                vm.tempBoxOptions.withOption('data', boxList);
                vm.tempBoxInstance.rerender();
            });
            for(var i = 0; i < boxInTubes.length; i++){
                boxInTubes[i].pos = "";
                boxInTubes[i].checkedFlag = true;
                boxInTubes[i].sampleTypeName = boxInTubes[i].sampleType.sampleTypeName;
            }
            boxInTubesCopy = [];
            boxInTubesCopy = angular.copy(boxInTubes);
            _fnLoadTube();
        }
        //整盒出库
        function _fnStockOutBox() {
            var boxList = [];
            boxList.push(frozenBox);
            TaskService.saveTempBoxes(taskId,boxList).success(function (data) {
                toastr.success("保存成功!");
                $uibModalInstance.close();
            });
        }

        function _fnCancelStockOutBox() {
            vm.boxInFullFlag = false;
        }

        //临时盒子
        vm.tempBoxOptions = BioBankDataTable.buildDTOption("BASIC", 298)
            .withOption('rowCallback', rowCallback)
            .withOption('createdRow', createdRow);
        vm.tempBoxColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sampleCount').withTitle('盒内样本数')
        ];
        var sampleTotalCount;
        function createdRow(row, data, dataIndex) {
            sampleTotalCount =  data.frozenBoxType.frozenBoxTypeRows * data.frozenBoxType.frozenBoxTypeColumns;
            var sampleOutCount = data.frozenTubeDTOS.length - (_.filter(data.frozenTubeDTOS,{sampleTempCode:""}).length);
            var str = sampleOutCount+"/"+sampleTotalCount;
            $('td:eq(1)', row).html(str);
            $compile(angular.element(row).contents())($scope);
        }
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td', nRow).unbind('click');
            $(nRow).bind('click', function() {
                var tr = this;
                // $scope.$apply(function () {
                    rowClickHandler(tr,oData);
                // })
            });
            return nRow;
        }
        // var selectBox;
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            vm.selectBox = data;
            if(vm.selectBox.frozenTubeDTOS.length >= sampleTotalCount){
                vm.pos = "";
                $scope.$apply();
                return
            }
            if(vm.selectBox.frozenTubeDTOS.length){
                var len = vm.selectBox.frozenTubeDTOS.length-1;
                _.orderBy(vm.selectBox.frozenTubeDTOS, ['tubeRows'], ['esc']);
                vm.pos = vm.selectBox.frozenTubeDTOS[len].tubeRows + (+vm.selectBox.frozenTubeDTOS[len].tubeColumns+1);
            }else{
                vm.pos = "A1";
            }

            _FnPreassemble(vm.selectedTubes);
            $scope.$apply();
        }
        //位置鼠标移除事件
        vm.posBlur = function () {
            var startPos = vm.pos;
            var startRow =  startPos.charAt(0);
            var startCol =  +startPos.substring(1);
            var row = startRow.charCodeAt(0) - 65;
            console.log(row);
            if(row == 40){
                return;
            }
            if(row >42){
                return;
            }
            if(vm.pos){
               _FnPreassemble(vm.selectedTubes);
            }else{
                _FnPreassemble(vm.selectedTubes);
            }
        };


        //待装盒样本
        vm.selected = {};
        vm.selectAll = false;
        // 处理盒子选中状态
        vm.toggleAll = function (selectAll, selectedItems) {

            vm.selectedTubes = [];
            _.each(boxInTubesCopy, function(b){
                b.pos = null;
                if(vm.selectAll){
                    b.checkedFlag = true;
                }else{
                    b.checkedFlag = false;
                }

                if (b.checkedFlag){
                    vm.selectedTubes.push(b);
                }
            });
            if(vm.pos){
                _FnPreassemble(vm.selectedTubes);
            }
            vm.sampleInstance.DataTable.draw();
            // selectedItems = vm.selected;
            // selectAll = vm.selectAll;
            // for (var id in selectedItems) {
            //     if (selectedItems.hasOwnProperty(id)) {
            //         selectedItems[id] = selectAll;
            //     }
            // }
        };
        vm.selectedTubes = [];
        vm.toggleOne = function (selectedItems,tubeId) {
            vm.selectedTubes = [];
            _.each(boxInTubesCopy, function(b){
                b.pos = null;
                b.checkedFlag = selectedItems[b.id];
                if (b.checkedFlag){
                    vm.selectedTubes.push(b);
                }
            });
            if(vm.selectedTubes.length == boxInTubesCopy.length){
                vm.selectAll = true;
            }else{
                vm.selectAll = false;
            }
            if (!vm.pos){
                vm.sampleInstance.DataTable.draw();
                return;
            }
            if(vm.pos){
                //预装位置
                _FnPreassemble(vm.selectedTubes);
            }

            vm.sampleInstance.DataTable.draw();

        };
        //预装位置
        function _FnPreassemble(selectedTubes) {
            var startPos = vm.pos;
            var startRow =  startPos.charAt(0);
            var startCol =  +startPos.substring(1);
            var pos={tubeRows:startRow,tubeColumns:startCol};
            var countOfCols = +vm.selectBox.frozenBoxType.frozenBoxTypeRows;
            var countOfRows = +vm.selectBox.frozenBoxType.frozenBoxTypeColumns;
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
            tempBoxList = [];
            vm.selectAll = false;
            for(var i = 0; i < vm.selectedTubes.length;i++){
                var index = _.indexOf(vm.selectBox.frozenTubeDTOS, vm.selectedTubes[i]);
                if(index == '-1'){
                    vm.selectBox.frozenTubeDTOS.push(vm.selectedTubes[i]);
                    _.pull(boxInTubesCopy, vm.selectedTubes[i]);
                }
            }
            vm.boxCopyLen = boxInTubesCopy.length;
            vm.selectedTubes = [];
            tempBoxList.push(vm.selectBox);
            vm.tempBoxInstance.DataTable.draw();
            vm.selectBox = {};
            vm.pos = "";
        }



        vm.sampleOptions = BioBankDataTable.buildDTOption("BASIC", 298)
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
                '<button type="button" ng-click="vm.sampleUp('+ full.orderIndex +')" class="btn btn-warning btn-xs" >' +
                '   <i class="fa  fa-long-arrow-up"></i>' +
                '</button>&nbsp;'+
                '<button type="button" ng-click="vm.sampleDown('+ full.orderIndex +')" class="btn btn-warning btn-xs" >' +
                '   <i class="fa fa-long-arrow-down"></i>' +
                '</button>&nbsp;'+
                '</div>';
        }

        function _fnLoadTube() {
            vm.selectedTubes = [];
            setTimeout(function () {
                for(var i = 0; i < boxInTubesCopy.length; i++){
                    boxInTubesCopy[i].pos = "";
                    boxInTubesCopy[i].checkedFlag = true;
                    boxInTubesCopy[i].sampleTypeName = boxInTubesCopy[i].sampleType.sampleTypeName;
                    vm.selectedTubes.push(boxInTubesCopy[i]);
                }
                vm.sampleOptions.withOption('data', boxInTubesCopy);
                vm.selectAll = true;
            },500);
        }
        _fnLoadTube();
        //向上排序
        var operateTubes = [];
        vm.sampleUp = function (oIndex) {
            operateTubes = [];
            operateTubes = _.filter(boxInTubesCopy,{checkedFlag:true});

            if(operateTubes.length > 1){
                var index = _.findIndex(operateTubes,{orderIndex:oIndex});
                var currentTube =  operateTubes[index];
                var preTube =  operateTubes[index-1];
                _fnTransposition(currentTube,preTube);
            }
        };
        //向下排序
        vm.sampleDown = function (oIndex) {
            operateTubes = [];
            operateTubes = _.filter(boxInTubesCopy,{checkedFlag:true});

            if(operateTubes.length > 1){
                var index = _.findIndex(operateTubes,{orderIndex:oIndex});
                var currentTube =  operateTubes[index];
                var preTube =  operateTubes[index+1];
                _fnTransposition(currentTube,preTube);
            }
        };
        function _fnTransposition(currentTube,preTube) {
            if(vm.pos){
                var middleTube = {
                    tubeRows: currentTube.tubeRows,
                    tubeColumns:currentTube.tubeColumns,
                    pos:currentTube.pos,
                    orderIndex:currentTube.orderIndex
                };
                currentTube.tubeRows = preTube.tubeRows;
                currentTube.tubeColumns = preTube.tubeColumns;
                currentTube.pos = preTube.pos;
                currentTube.orderIndex = preTube.orderIndex;

                preTube.tubeRows = middleTube.tubeRows;
                preTube.tubeColumns = middleTube.tubeColumns;
                preTube.pos = middleTube.pos;
                preTube.orderIndex = middleTube.orderIndex;

                for(var i = 0; i < boxInTubesCopy.length; i++){
                    for(var j = 0; j < operateTubes.length; j++){
                        if(operateTubes[j].id == boxInTubesCopy[i].id){
                            boxInTubesCopy[i] = operateTubes[j];
                        }
                    }
                }
                boxInTubesCopy = _.orderBy(boxInTubesCopy,['orderIndex'],['asc']);
                vm.sampleOptions.withOption('data', boxInTubesCopy);
                vm.sampleInstance.rerender();
            }





        }
        //装盒界面
        vm.isBoxInFlag = false;
        vm.boxInOperate = function () {
            vm.isBoxInFlag = true;
        }
        vm.yes = function () {
            vm.allInFlag = true;
        };
        vm.ok = function () {
            if(vm.selectedTubes.length && vm.pos){
                vm.boxIn();
            }
            var tempBoxListCopy = angular.copy(tempBoxList);
            for(var i =0; i < tempBoxListCopy.length; i++){
                delete tempBoxListCopy[i].sampleCount;
                delete tempBoxListCopy[i].sampleOutCount;
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
