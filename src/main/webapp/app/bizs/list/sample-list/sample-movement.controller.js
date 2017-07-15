/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SampleMovementController', SampleMovementController);

    SampleMovementController.$inject = ['$scope','hotRegisterer','$compile','$state','$stateParams','$uibModal','toastr','DTColumnBuilder','ProjectService','EquipmentService',
        'SampleTypeService','MasterData','BoxInventoryService','BioBankDataTable','StockInBoxByCodeService','SampleService','FrozenBoxTypesService','RequirementService','SampleInventoryService','SampleUserService'];

    function SampleMovementController($scope,hotRegisterer,$compile,$state,$stateParams,$uibModal,toastr,DTColumnBuilder,ProjectService,EquipmentService,
                                      SampleTypeService,MasterData,BoxInventoryService,BioBankDataTable,StockInBoxByCodeService,SampleService,FrozenBoxTypesService,RequirementService,SampleInventoryService,SampleUserService) {
        var vm = this;
        vm.boxInstance = {};
        vm.selectedInstance = {};
        vm.dto = {};
        vm.movement = {
            whetherFreezingAndThawing:false,
            positionMoveRecordDTOS:[]
        };
        // 选中的要上架的盒子
        vm.selectedTubes = $stateParams.selectedSample || [];
        vm.selected = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.selectedSample = [];
        var selectedSampleId = [];

        if(vm.selectedTubes.length){
            for(var i = 0; i < vm.selectedTubes.length; i++){
                selectedSampleId.push(vm.selectedTubes[i].id);
            }
        }
        var projectIds = [];

        function _init() {
            // 过滤已上架的冻存盒
            vm.filterNotPutInShelfTubes = function(tube){
                return !tube.isPutInShelf && true;
            };
            //批注人
            SampleUserService.query({},onUserSuccess, onError);
            function onUserSuccess(data) {
                vm.userOptions = data;
            }
            vm.userConfig = {
                valueField:'id',
                labelField:'userName',
                maxItems: 1

            };
            if(selectedSampleId.length){
                _searchTubesByIds();
            }

        }
        _init();
        function _searchTubesByIds() {
            var tubeIds = selectedSampleId.join(",");
            SampleInventoryService.queryTubeDesByIds(tubeIds).success(function (data) {
                vm.selectedSample = data;
                _.forEach(vm.selectedSample, function(tube){
                    vm.selected[tube.id] = false;
                });
            })
        }
        //关闭移位
        vm.close = _fnClose;
        //搜索
        vm.search = _fnSearch;
        //清空
        vm.empty = _fnEmpty;
        //复原
        vm.recover = _fnRecover;
        //移入
        vm.putIn = _fnPutIn;
        //关闭目标冻存区
        vm.closeSampleMovement = _fnCloseSampleMovement;
        //保存移位
        vm.saveMovement = _fnSaveMovement;
        //获取管子列表
        vm.sampleMovement = _fnSampleMovement;

        function _fnSearch() {
            if(projectIds.length){
                for(var i = 0; i <projectIds.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }
            vm.checked = false;
            vm.boxInstance.rerender();
        }
        function _fnEmpty() {
            vm.dto = {};
            vm.dto.projectCodeStr = [];
            projectIds = [];
            vm.projectCodeStr = [];
            vm.arrayBoxCode = [];
            vm.dto.spaceType = "2";
            vm.dto.compareType = "1";
            vm.dto.number = 0;
            vm.dto.status = "2004";
            vm.boxInstance.rerender();
        }

        function _fnClose() {
            if(vm.selectedSample.length){
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

        _initFrozenBoxPanel();

        function _initSampleType() {
            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','asc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"99"});
                if(!vm.box.sampleType){
                    vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.box.sampleTypeName = vm.sampleTypeOptions[0].sampleTypeName;
                    vm.box.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
                }else{
                    vm.box.sampleTypeId = vm.box.sampleType.id;
                    vm.box.sampleTypeName = vm.box.sampleType.sampleTypeName;
                    vm.box.sampleTypeCode = vm.box.sampleType.sampleTypeCode;
                }

                // vm.box.sampleType = vm.sampleTypeOptions[0];
                // vm.isMixed = _.find(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId}).isMixed;
                // setTimeout(function () {
                _fnQueryProjectSampleClass(vm.box.projectId,vm.box.sampleTypeId);
                // },500);
            });
            // vm.sampleTypeConfig = {
            //     valueField:'id',
            //     labelField:'sampleTypeName',
            //     maxItems: 1,
            //     onChange:function (value) {
            //         _fnQueryProjectSampleClass(vm.entity.projectId,value);
            //     }
            // };
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                });
            }
            // vm.projectSampleTypeConfig = {
            //     valueField:'sampleClassificationId',
            //     labelField:'sampleClassificationName',
            //     maxItems: 1,
            //     onChange:function (value) {
            //     }
            // };
        }


        vm.selectAll = false;
        vm.toggleAll = toggleAll;
        vm.toggleOne = toggleOne;
        function toggleAll (selectAll, selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
        }

        function toggleOne (selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        vm.selectAll = false;
                        return;
                    }
                }
            }
            vm.selectAll = true;
        }
        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
            // .withOption('data', selectedSample)
            // .withOption('createdRow', function(row, data, dataIndex) {
            //     $compile(angular.element(row).contents())($scope);
            // })
            // .withOption('headerCallback', function(header) {
            //     $compile(angular.element(header).contents())($scope);
            // })
            // .withPaginationType('full_numbers');
        // var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.selectedColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "30"),
            DTColumnBuilder.newColumn(1).withOption("width", "100"),
            DTColumnBuilder.newColumn(2).withOption("width", "60"),
            DTColumnBuilder.newColumn(3).withOption("width", "80"),
            DTColumnBuilder.newColumn(4).withOption("width", "80")

        ];

        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = true;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }


        /*目标冻存区*/
        function _initSearch() {
            vm.dto.spaceType = "2";
            vm.dto.compareType = "1";
            vm.dto.number = 0;
            vm.dto.status = "2004";

            //获取项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备
            EquipmentService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            //盒类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
            }
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeCode','asc']);
                _.remove(vm.sampleTypeOptions,{"sampleTypeCode":"99"})
            });
            //盒子编码
            vm.boxCodeConfig = {
                create: true,
                persist:false,
                onChange: function(value){
                    vm.dto.frozenBoxCodeStr = value;
                }
            };
            //项目编码
            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                searchField:'projectName',
                onChange:function(value){
                    vm.projectIds = _.join(value, ',');
                    projectIds = value;
                    vm.dto.projectCodeStr = [];
                    if(vm.dto.sampleTypeId){
                        _fnQueryProjectsSampleClass(vm.projectIds,vm.dto.sampleTypeId);
                    }
                }
            };
            //盒子类型
            vm.boxTypeConfig = {
                valueField:'id',
                labelField:'frozenBoxTypeName',
                maxItems: 1,
                onChange:function(value){

                }
            };
            //样本类型
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    if(vm.projectIds) {
                        _fnQueryProjectsSampleClass(vm.projectIds, value);
                    }
                }
            };
            vm.sampleClassConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            //样本分类
            function _fnQueryProjectsSampleClass(projectIds,sampleTypeId) {
                RequirementService.queryRequirementSampleClasses(projectIds,sampleTypeId).success(function (data) {
                    vm.sampleClassOptions = data;
                    if(vm.sampleClassOptions.length){
                        vm.dto.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;
                    }
                });
            }
        }
        _initSearch();
        //关闭
        function _fnCloseSampleMovement() {
            vm.sampleMovementFlag = false;
        }
        //移位
        vm.sampleMovementFlag = false;
        function _fnSampleMovement(frozenBoxCode) {
            vm.sampleMovementFlag = true;
            StockInBoxByCodeService.get({code:frozenBoxCode},onFrozenSuccess,onError);
        }
        function onFrozenSuccess(data) {
            vm.box = data;
            _initSampleType();
            setTimeout(function () {
                _reloadTubesForTable(vm.box);
            },500);


        }
        function _fnRecover() {
            var tableCtrl = hotRegisterer.getInstance('my-handsontable');
        }
        //盒子列表
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
            DTColumnBuilder.newColumn('frozenBoxType').withTitle('盒子类型').withOption("width", "80"),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "60").renderWith(_fnRowCodeRender),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "100"),
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
        function _fnRowCodeRender(data, type, full, meta) {
            var frozenBoxCode = "'"+full.frozenBoxCode + "'";
            var html = '';
            html = '<a ng-click="vm.sampleMovement('+frozenBoxCode+')">'+full.frozenBoxCode+'</a>';
            return html;
        }



        /*冻存管修改*/
        //移入
        function _fnPutIn() {
            var countOfCols = vm.box.frozenBoxType.frozenBoxTypeColumns;
            var countOfRows = vm.box.frozenBoxType.frozenBoxTypeRows;
            var tableCtrl = _getTableCtrl();
            var startEmptyPos = tableCtrl.getSelected();
            var cellRow = startEmptyPos[0];
            var cellCol = startEmptyPos[1];

            for(var i in vm.selected){
                if(vm.selected[i]){
                    // 遍历选中的冻存管，i是冻存管的Id
                    var tube = _.find(vm.selectedSample, {id: +i});
                    if (!tube){
                        continue;
                    }
                    if(tube.projectCode != vm.box.projectCode){
                        toastr.error("项目不一致，不能移入此冻存盒内！");
                        return;
                    }
                    if(tube.sampleClassificationCode){
                        if(tube.sampleClassificationCode != vm.box.sampleClassificationCode){
                            toastr.error("样本分类不一致，不能移入此冻存盒内！");
                            return;
                        }
                    }else{
                        if(tube.sampleTypeCode != vm.box.sampleTypeCode){
                            toastr.error("样本类型不一致，不能移入此冻存盒内！");
                            return;
                        }
                    }
                    for(; cellRow < countOfRows && !tube.isPutInShelf; ++cellRow){
                        for (; cellCol < countOfCols && !tube.isPutInShelf; ++cellCol){
                            var cellData = tableCtrl.getDataAtCell(cellRow, cellCol);
                            if (cellData && !cellData.isEmpty){
                                tube.isPutInShelf = true;
                                tube.tubeRows = cellData.tubeRows;
                                tube.tubeColumns = cellData.tubeColumns;
                                tube.frozenBoxId = cellData.frozenBoxId;
                                tube.frozenTubeId = tube.id;
                                vm.selected[i] = false;
                                // cellData.id = tube.id;
                                cellData.sampleCode = tube.sampleCode;
                                cellData.sampleTypeId = tube.sampleTypeId;
                                cellData.sampleTypeCode = tube.sampleTypeCode;
                                cellData.sampleTypeName = tube.sampleTypeName;
                                cellData.sampleClassificationId = tube.sampleClassificationId;
                                cellData.sampleClassificationCode = tube.sampleClassificationCode;
                                cellData.sampleClassificationName = tube.sampleClassificationName;
                                cellData.backColor = tube.backColor;
                                cellData.backColorForClass = tube.backColorForClass;
                                cellData.isEmpty = true;
                                break;
                            }


                        }
                        if (tube.isPutInShelf){
                            break;
                        }
                        cellCol = 0;
                    }
                }

            }

            vm.nextFlag = true;
            tableCtrl.render();
            vm.selectedInstance.rerender();
        }
        function _initFrozenBoxPanel(){
            vm.frozenTubeArray = [];//初始管子数据二位数组
            var operateColor;//单元格颜色

            initFrozenTube(10,10);

            vm.settings = {
                colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,
                data:vm.frozenTubeArray,
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
                colWidths: 80,
                rowHeaderWidth: 30,
                multiSelect: false,
                comments: true,
                editor: false,
                renderer: myCustomRenderer,
                cells: _changeCellProperties,
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
            //渲染管子表格
            function myCustomRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var tube= value||{};
                td.style.position = 'relative';
                if (cellProperties.readOnly){
                    $(td).addClass('htDimmed');
                    $(td).addClass('htReadOnly');
                } else {
                    $(td).removeClass('htDimmed');
                    $(td).removeClass('htReadOnly');
                }
                if(tube.memo && tube.memo != " "){
                    cellProperties.comment = tube.memo;
                }
                // if(tube.sampleType){
                //     td.style.backgroundColor = tube.sampleType.backColor;
                // }
                // if(tube.sampleClassification){
                //     td.style.backgroundColor = tube.sampleType.backColorForClass;
                // }
                if(tube.backColorForClass){
                    td.style.backgroundColor = tube.backColorForClass;
                }else{
                    td.style.backgroundColor = tube.backColor;
                }
                //样本类型
                // if(tube.sampleClassificationId){
                //     SampleService.changeSampleType(tube.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                // }else{
                //     if(vm.sampleTypeOptions){
                //         SampleService.changeSampleType(tube.sampleTypeId,td,vm.sampleTypeOptions,2);
                //     }
                // }
                // if(vm.box.sampleType.sampleTypeName == '97'){
                //     if(tube.backColorForClass){
                //         td.style.backgroundColor = tube.backColorForClass;
                //     }else{
                //         td.style.backgroundColor = tube.backColor;
                //     }
                // //
                // }else{
                //
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
            // 修改单元格属性
            function _changeCellProperties(row, col, prop) {
                var hot = _getTableCtrl();
                if (!hot){
                    return;
                }
                var cellProperties = {};
                var cellData = hot.getDataAtCell(row, col);
                cellProperties.isEmpty = true;
                if (cellData && cellData.sampleTempCode || cellData.sampleCode) {
                    // 单元格有数据，并且有冻存盒ID，表示该单元格在库里有位置
                    // 该单元格不能被使用
                    cellProperties.isEmpty = false;
                    // 该单元格不能编辑
                    cellProperties.editor = false;
                    // 该单元格只读
                    cellProperties.readOnly = true;
                    // 该单元格的样式
                    cellProperties.className = 'a00';
                    // cellProperties.readOnlyCellClassName = 'htDimmed';
                } else {
                    cellProperties.isEmpty = cellData && cellData.isEmpty;
                }

                return cellProperties;
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
                sampleTypeCode: "",
                backColor: "",

                sampleClassificationId:"",
                sampleClassificationName:"",
                sampleClassificationCode:"",
                backColorForClass:"",
                frozenBoxTypeId:box.frozenBoxType.id,
                frozenBoxId: box.id,
                frozenBoxCode: box.frozenBoxCode,
                status: "3001",
                memo: "",
                isEmpty: false,
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns,
                rowNO: rowNO,
                colNO: colNO
            };

            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                if(tubeInBox.sampleClassification){
                    tube.sampleClassificationId = tubeInBox.sampleClassification.id;
                    tube.sampleClassificationName = tubeInBox.sampleClassification.sampleClassificationName;
                    tube.sampleClassificationCode = tubeInBox.sampleClassification.sampleClassificationCode;
                    tube.backColorForClass = tubeInBox.sampleClassification.backColor;
                }
                tube.sampleTypeId = tubeInBox.sampleType.id;
                tube.sampleTypeCode = tubeInBox.sampleType.sampleTypeCode;
                tube.sampleTypeName = tubeInBox.sampleType.sampleTypeName;
                tube.backColor = tubeInBox.sampleType.backColor;
                if(tube.sampleCode || tube.sampleTempCode){
                    tube.isEmpty = true;
                }

            }else{
                tube.sampleTypeId = box.sampleType.id;
                tube.sampleTypeCode = box.sampleType.sampleTypeCode;
                tube.sampleTypeName = box.sampleType.sampleTypeName;
                tube.backColor = box.sampleType.backColor;
                if(box.sampleClassification){
                    tube.sampleClassificationId = box.sampleClassification.id;
                    tube.sampleClassificationName = box.sampleClassification.sampleClassificationName;
                    tube.sampleClassificationCode = box.sampleClassification.sampleClassificationCode;
                    tube.backColorForClass = box.sampleClassification.backColor;
                }

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
            var emptyPos;
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
                    if(!tubeInBox){
                        if(!emptyPos){
                            emptyPos = {row:i,col:j};
                        }

                    }
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
            tableCtrl.selectCell(emptyPos.row, emptyPos.col);
        }
        // 获取上架位置列表的控制实体
        function _getTableCtrl(){
            vm.tableCtrl = hotRegisterer.getInstance('my-handsontable');
            return vm.tableCtrl;
        }
        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }


        function _fnSaveMovement() {

            vm.movement.positionMoveRecordDTOS = _.filter(vm.selectedSample,{isPutInShelf:true});
            console.log(JSON.stringify(vm.movement));
            // var box = {
            //     id : vm.box.id,
            //     frozenTubeDTOS:[]
            // };
            // for(var i = 0; i < vm.frozenTubeArray.length; i++){
            //     for(var j = 0; j < vm.frozenTubeArray[i].length; j++){
            //         if(vm.frozenTubeArray[i][j].sampleCode || vm.frozenTubeArray[i][j].sampleTempCode){
            //             box.frozenTubeDTOS.push(vm.frozenTubeArray[i][j]);
            //         }
            //     }
            // }
            //
            // vm.movement.positionMoveForBoxList = [];
            // vm.movement.positionMoveForBoxList.push(box);
            // console.log(JSON.stringify(vm.movement));
            SampleInventoryService.saveMovementDes(vm.movement).success(function (data) {
                vm.movement.id = data.id;
                toastr.success("保存成功!");
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
    }
})();
