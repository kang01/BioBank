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
        //添加新的临时盒
        vm.addNewBox = _fnAddNewBox;
        //装盒
        vm.boxIn = _fnBoxIn;

        vm.box = {
            frozenBoxCode:null,
            frozenBoxType:{
                id:null
            },
            sampleCount:null
        };
        //待入库样本都已扫码
        vm.allInFlag = items.allInFlag;

        var boxInTubes = items.boxInTubes;
        var taskId = items.taskId;
        var boxList = [];
        //临时盒子
        TaskService.queryTempBoxes(taskId).success(function (data) {
           // console.log(JSON.stringify(data))
            for(var i = 0; i < data.length; i++){
                data[i].sampleCount = null;
                boxList.push(data[i])
            }



            vm.tempBoxOptions.withOption('data', boxList);
            vm.tempBoxInstance.rerender();
        });
        //盒子类型
        FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['esc'])
            // vm.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
            vm.box.frozenBoxType.id = vm.frozenBoxTypeOptions[0].id;
            vm.box.frozenBoxType.frozenBoxTypeRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
            vm.box.frozenBoxType.frozenBoxTypeColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns;
        }
        //盒子类型 17:10*10 18:8*8
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                var boxType = _.filter(vm.frozenBoxTypeOptions, {id:+value})[0];
                // if (!boxType) {
                //     return;
                // }
                // vm.box.frozenBoxTypeId = value;
                vm.box.frozenBoxType.id = value;
                vm.box.frozenBoxType.frozenBoxTypeRows = boxType.frozenBoxTypeRows;
                vm.box.frozenBoxType.frozenBoxTypeColumns = boxType.frozenBoxTypeColumns;

                // var box = vm.box;
                // _reloadTubesForTable(box);
                // hotRegisterer.getInstance('my-handsontable').render();
            }
        };


        // console.log(JSON.stringify(boxInTubes));
        var tubes = [];
        for(var i = 0; i < boxInTubes.length; i++){
            var tube = {
                id:boxInTubes[i].id,
                sampleTypeName:boxInTubes[i].sampleType.sampleTypeName,
                sampleCode:boxInTubes[i].sampleTempCode,
                checkedFlag:false,
                pos:"",
                row:"",
                col:""
            }
            tubes.push(tube)
        }

        vm.tempBoxInstance = {};
        vm.sampleInstance = {};
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
            // DTColumnBuilder.newColumn('id').notVisible()
        ];
        function createdRow(row, data, dataIndex) {
            var sampleCount = data.frozenTubeDTOS.length;
            $('td:eq(1)', row).html(sampleCount);
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
            // if (vm.box && vm.box.frozenBoxCode == oData.frozenBoxCode){
            //     $(nRow).addClass('rowLight');
            // }
            return nRow;
        }
        var selectBox;
        function rowClickHandler(tr,data) {
            $(tr).closest('table').find('.rowLight').removeClass("rowLight");
            $(tr).addClass('rowLight');
            // console.log(JSON.stringify(data))
            selectBox = data;
            var tubeList =  _.filter(selectBox.frozenTubeDTOS,{sampleTempCode:""});
            _.orderBy(tubeList, ['tubeRows'], ['esc']);
            vm.pos = tubeList[0].tubeRows + tubeList[0].tubeColumns
        }
        //添加新盒

        function _fnAddNewBox(){
            _reloadTubesForTable();
            boxList.push(angular.copy(vm.box));
            vm.tempBoxOptions.withOption('data', boxList);
            vm.tempBoxInstance.rerender();

        }
        //加载管子表控件
        function _reloadTubesForTable(){
            var tubesInTable = [];
            for (var i=0; i < 10; ++i){
                var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                if(i > 7){
                    pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1)
                }
                var tubes = [];
                for (var j=0; j <10; ++j){
                    pos.tubeColumns = j + 1 + "";
                    // var tubeInBox = _.filter(frozenTubeList, pos)[0];
                    var tube = _createTubeForTableCell(i, j + 1, pos);
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

            vm.box.frozenTubeDTOS = vm.tubeList;
            console.log(JSON.stringify(vm.box))
        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(rowNO, colNO, pos){
            var tube = {
                sampleCode: "",
                sampleTempCode: "",
                status: "",
                memo: "",
                stockOutFlag:"",
                scanCodeFlag:"",
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns,
                // rowNO: rowNO,
                // colNO: colNO
            };
            // if (tubeInBox){
            //     tube.id = tubeInBox.id;
            //     tube.sampleCode = tubeInBox.sampleCode;
            //     tube.sampleTempCode = tubeInBox.sampleTempCode;
            //     tube.sampleType = tubeInBox.sampleType;
            //     tube.sampleClassification = tubeInBox.sampleClassification;
            //     tube.stockOutFlag = tubeInBox.stockOutFlag;
            //     tube.status = tubeInBox.status;
            //     tube.memo = tubeInBox.memo;
            // }
            return tube;
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
        var posList = [];
        var count = 0;
        var  selectedTubes = [];
        vm.toggleOne = function (selectedItems,tubeId) {
            console.log(JSON.stringify(selectedItems))
            for(var id in selectedItems){
                if(selectedItems[id]){
                    for(var i = 0; i < tubes.length;i++){
                        if(id == tubes[i].id){
                            tubes[i].checkedFlag = true;
                            count++;
                        }
                    }

                }else{
                    for(var i = 0; i < tubes.length;i++){
                        if(id == tubes[i].id){
                            tubes[i].checkedFlag = false;
                        }
                    }
                }
            }
            // for(var i = 0; i < 10; i++){
            //     var rowNO = i > 7 ? i+1 : i;
            //     rowNO = String.fromCharCode(rowNO+65);
            //     for(var j = 0; j < 10; j++){
            //         tubeList[i][j] = {
            //             tubeRows:rowNO,
            //             tubeColumns: k+1
            //         }
            //     }
            //
            // }
            for(var m = 0; m < tubes.length; m++){
                if(tubes[m].checkedFlag){
                    var str1 = vm.pos.charAt(0);
                    var str2 = vm.pos.substring(1);
                    tubes[m].col = +str2;
                    if(count > 10*m+1){
                        tubes[m].row = String.fromCharCode(str1.charCodeAt(0) + count);
                    }else{
                        tubes[m].row = str1
                    }
                    tubes[m].pos = tubes[m].row + tubes[m].col;
                    selectedTubes.push(tubes[m])
                }
            }
            vm.sampleOptions.withOption('data', tubes);
            vm.sampleInstance.rerender();


        };

        vm.sampleOptions = DTOptionsBuilder.newOptions()
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withOption('searching', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('createdRow', function(row, data, dataIndex) {

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
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('pos').withTitle('预装位置'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('标签'),
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
            vm.sampleOptions.withOption('data', tubes);
            vm.sampleInstance.rerender();
        },500);
        var tempBoxList = [];

        function _fnBoxIn() {
            var indexs = [];
            // console.log(JSON.stringify(selectBox));
            // console.log(JSON.stringify(selectedTubes));
            for(var i = 0; i < selectBox.frozenTubeDTOS.length; i++){
                for(var j = 0; j < selectedTubes.length; j++){
                    if(selectBox.frozenTubeDTOS[i].tubeRows == selectedTubes[j].row && selectBox.frozenTubeDTOS[i].tubeColumns == selectedTubes[j].col){
                        selectBox.frozenTubeDTOS[i].sampleCode =selectedTubes[j].sampleCode
                        selectBox.frozenTubeDTOS[i].id =selectedTubes[j].id
                    }
                }
            }
            for(var m = 0; m < selectBox.frozenTubeDTOS.length; m++){
                if(!selectBox.frozenTubeDTOS[m].sampleCode){
                    indexs.push(m)
                }

            }
            _.pullAt(selectBox.frozenTubeDTOS, indexs);
            tempBoxList.push(selectBox);
            console.log(JSON.stringify(tempBoxList));
        }

        vm.yes = function () {
            vm.allInFlag = true;
        };
        vm.ok = function () {
            TaskService.saveTempBoxes(taskId,tempBoxList).success(function (data) {
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
