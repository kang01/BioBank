/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleMovementController', SampleMovementController);

    SampleMovementController.$inject = ['$scope','hotRegisterer','$compile','$state','$stateParams','$uibModal','DTColumnBuilder','ProjectService','EquipmentService','SampleTypeService','MasterData','BoxInventoryService','BioBankDataTable','frozenBoxByCodeService','SampleService'];

    function SampleMovementController($scope,hotRegisterer,$compile,$state,$stateParams,$uibModal,DTColumnBuilder,ProjectService,EquipmentService,SampleTypeService,MasterData,BoxInventoryService,BioBankDataTable,frozenBoxByCodeService,SampleService) {
        var vm = this;
        vm.dtInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        var selectedSample = $stateParams.selectedSample || [];
        function _init() {

        }
        _init();
        vm.close = _fnClose;
        function _fnClose() {
            if(selectedSample.length){
                var modalInstance = $uibModal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    $state.go("sample-inventory");
                }, function () {
                });
            }else{
                $state.go("sample-inventory");
            }

        }
        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码').withOption("width", "130"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "60"),

        ];
        vm.selectedOptions.withOption('data', selectedSample);

        vm.boxOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                var searchForm = angular.toJson(vm.dto);
                BoxInventoryService.queryBoxList(data,searchForm).then(function (res){
                    var json = res.data;
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
            })
            .withOption('createdRow', createdRow);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('frozenBoxType').withTitle('盒子类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "60").renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "60"),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用').withOption("width", "60"),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余').withOption("width", "60"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "60")

        ];
        function createdRow(row, data, dataIndex) {
            // var projectName = _.find(vm.projectOptions,{projectCode:data.projectCode}).projectName;
            var status = "";
            status = MasterData.getFrozenBoxStatus(data.status);
            // $('td:eq(3)', row).html(projectName);
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnRowSelectorRender(data, type, full, meta) {
            var html = '';
            html = '<a ng-click="vm.sampleMovement('+full.frozenBoxCode+')">'+full.frozenBoxCode+'</a>';
            return html;
        }
        vm.sampleMovement = _fnSampleMovement;
        vm.sampleMovementFlag = false;
        function _fnSampleMovement(frozenBoxCode) {
            vm.sampleMovementFlag = true;
            frozenBoxByCodeService.get({code:"123456798"},onFrozenSuccess,onError);
        }
        function onFrozenSuccess(data) {
            vm.box = data;
            _reloadTubesForTable(vm.box);
            _initSampleType();
        }
        _initFrozenBoxPanel();

        function _initSampleType() {
            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"99"});
                if(!vm.box.sampleTypeId){
                    vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.box.sampleTypeName = vm.sampleTypeOptions[0].sampleTypeName;
                    vm.box.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
                }

                vm.box.sampleType = vm.sampleTypeOptions[0];
                vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId}).isMixed;
                setTimeout(function () {
                    _fnQueryProjectSampleClass("1",vm.box.sampleTypeId,vm.isMixed);
                },500);
            });
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+value}).isMixed;
                    vm.box.sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeName;
                    vm.box.sampleTypeCode = _.find(vm.sampleTypeOptions,{'id':+value}).sampleTypeCode;
                    _fnQueryProjectSampleClass(vm.entity.projectId,value,vm.isMixed);
                }
            };
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId,isMixed) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    //是否混合类型 1：是混合类型
                    if(isMixed == 1){

                        //     //混合型无分类
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(!vm.frozenTubeArray[i][j].sampleCode){
                                    vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                    vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeName;
                                    vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).backColor;
                                }
                            }

                        }
                        // }

                    }else{
                        if(!vm.box.sampleClassification){
                            if(vm.projectSampleTypeOptions.length){
                                vm.box.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                            }
                        }

                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(vm.box.sampleClassification){
                                    if(!vm.frozenTubeArray[i][j].sampleCode){
                                        vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassificationId;

                                    }
                                }
                                vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                vm.frozenTubeArray[i][j].sampleTypeName = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).sampleTypeName;
                                vm.frozenTubeArray[i][j].backColor = _.find(vm.sampleTypeOptions,{'id':+sampleTypeId}).backColor;
                            }
                        }
                    }

                    hotRegisterer.getInstance('my-handsontable').render();
                });
            }
            vm.projectSampleTypeConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    vm.box.sampleClassificationId = value;
                    vm.box.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassificationId;
                        }
                    }
                    hotRegisterer.getInstance('my-handsontable').render();
                }
            };
        }

        function _initFrozenBoxPanel(){
            vm.frozenTubeArray = [];//初始管子数据二位数组
            var operateColor;//单元格颜色

            initFrozenTube(10,10);

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
                colWidths: 80,
                rowHeaderWidth: 30,
                multiSelect: true,
                comments: true,
                enterMoves:function () {
                    if(vm.nextFlag){
                        var hotMoves = hotRegisterer.getInstance('my-handsontable');
                        var selectedCol = hotMoves.getSelected()[1];
                        if(selectedCol + 1 < hotMoves.countCols()){
                            return{row:0,col:1};
                        } else{
                            return{row:1,col:-selectedCol};
                        }
                    }else{
                        return{row:0,col:0};
                    }

                }

            };
            // 获取控制实体
            function _getTableCtrl(){
                vm.TableCtrl = hotRegisterer.getInstance('my-handsontable');
                return vm.TableCtrl;
            }
            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = 'relative';

                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = tube.memo;
                }
                // if(vm.box.sampleType.sampleTypeName == '98'){
                //     if(tube.backColorForClass){
                //         td.style.backgroundColor = tube.backColorForClass;
                //     }else{
                //         td.style.backgroundColor = tube.backColor;
                //     }
                //
                // }else{
                //     //样本类型
                    if(tube.sampleClassificationId){
                        SampleService.changeSampleType(tube.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                    }else{
                        if(vm.sampleTypeOptions){
                            SampleService.changeSampleType(tube.sampleTypeId,td,vm.sampleTypeOptions,2);
                        }
                    }
                // }


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
                $div = $("<div id='microtubesStatus'/>").html(tube.status).hide().appendTo(td);

            }

            //修改样本状态正常、空管、空孔、异常
            function changeSampleStatus(sampleStatus,row,col,td,cellProperties) {

                operateColor = td.style.backgroundColor;
                //正常
                if(sampleStatus == 3001){
                }
                //空管
                if(sampleStatus == 3002){
                    td.style.background = 'linear-gradient(to right,'+operateColor+',50%,black';
                }
                //空孔
                if(sampleStatus == 3003){
                    td.style.background = '';
                    td.style.backgroundColor = '#ffffff';
                    td.style.color = '#ffffff';
                }
                //异常
                if(sampleStatus == 3004){
                    td.style.backgroundColor = 'red';
                    td.style.border = '3px solid red;margin:-3px';
                }
            }


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

        }
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, box, rowNO, colNO, pos){
            var tube = {
                id: null,
                sampleCode: "",
                sampleTempCode: "",
                sampleTypeId: "",
                sampleTypeName: "",
                sampleClassificationId:"",
                frozenBoxId: box.frozenBoxType.frozenBoxTypeId,
                frozenBoxCode: box.frozenBoxType.frozenBoxCode,
                status: "3001",
                memo: "",
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns,
                rowNO: rowNO,
                colNO: colNO,
            };

            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                if(tubeInBox.sampleClassification){
                    tube.sampleClassificationId = tubeInBox.sampleClassification.sampleClassificationId;
                    tube.backColorForClass = tubeInBox.sampleClassification.backColorForClass;
                }
                tube.sampleTypeId = tubeInBox.sampleType.sampleTypeId;
                tube.sampleTypeName = tubeInBox.sampleType.sampleTypeName;
                tube.backColor = tubeInBox.sampleType.backColor;
            }else{
                tube.sampleTypeId = box.sampleType.sampleTypeId;
                tube.sampleTypeName = box.sampleType.sampleTypeName;
                tube.sampleClassificationId = box.sampleClassification.sampleClassificationId;

            }

            return tube;
        }
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
                    // if(box.isMixed == "1"){
                    //     for (var l = 0; l < vm.projectSampleTypeOptions.length; l++) {
                    //         if (vm.projectSampleTypeOptions[l].columnsNumber == pos.tubeColumns) {
                    //             if(!tube.sampleClassificationId){
                    //                 tube.sampleClassificationId = vm.projectSampleTypeOptions[l].sampleClassificationId;
                    //             }
                    //
                    //         }
                    //     }
                    // }
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

        }


        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
})();
