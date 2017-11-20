/**
 * Created by gaokangkang on 2017/5/12.
 */
(function()  {
    'use  strict';

    angular
        .module('bioBankApp')
        .directive('tubeBoxTable',  tubeBoxTable);

    tubeBoxTable.$inject  =  ["hotRegisterer"];

    function  tubeBoxTable(hotRegisterer)  {
        var  directive  =  {
            restrict:  'AE',
            scope  :  {
                dataBox: '=?',
                dataTubes  :  '=?',
                htSettings: '=',
                htInstance: '=?',

            },
            template: '<hot-table hot-id="tubeBoxTable" settings="vm.settings" class="tube" style="width: 100%; padding: 0;margin: 0;"></hot-table>',
            controller: ["$scope","toastr","SampleTypeService", ctrlFunc],
            controllerAs: 'vm',
            link:  linkFunc
        };

        return  directive;

        function ctrlFunc($scope,toastr, SampleTypeService){
            var vm = this;

            // 盒子中可以使用的样本分类
            vm.projectSampleTypeOptions = [];

            vm.api = {
                columnHeaders: [],
                rowHeaders:[],
                gridData: [],
                selectedTubes: [],
                selectedTubeElements: [],
                refresh: _refresh,
                loadData: _loadData,
                rollbackToOriginalGridData: _rollbackToOriginalGridData,
                updateOriginalGridData: _updateOriginalGridData,
                getGridData: _getGridData,
                updateSettings: _updateSettings,
                getSettings: _getSettings,
                getSelectedData: _getSelectedData,
                getSelectedElements: _getSelectedElements,
                getRangeCellData: _getRangeCellData,
                getRangeCellElements: _getRangeCellElements,
                selectRangeCell: _selectRangeCell,
                selectAll: _selectAll,
                exchangePos: _exchangePos,
                exchangeSelectedTubePosition: _exchangeSelectedTubePosition,
            };
            $scope.htInstance = $scope.htInstance || {};
            $scope.htInstance.api = vm.api;

            vm.defaultSettings = {
                isCellValueEditable: true,
                isCellStatusEditable: false,

                colHeaders: ['1','2','3','4','5','6','7','8','9','10'],
                rowHeaders: ['A','B','C','D','E','F','G','H','J','K'],
                minRows: 10,
                minCols: 10,
                fillHandle:false,
                stretchH: 'all',
                wordWrap:true,
                rowHeaderWidth: 25,
                colWidths: 60,
                columnHeaderHeight:25,
                editor: 'tubeCellInput',
                outsideClickDeselects:true,
                multiSelect: true,
                comments: true,
                afterInit: _onInit,
                afterGetColHeader: _onGetTableColHeader,
                afterBeginEditing: _onBeginEditing,
                beforeChange: _onTubeCellChanging,
                afterChange: _onTubeCellChanged,
                beforeValidate: _onTubeDataValidating,
                renderer: _tubeCellRenderer,
                onAfterSelectionEnd: _onTubeCellSelected,
                beforeOnCellMouseDown: _onTubeCellClicking,
                afterOnCellMouseDown: _onTubeCellClicked,
                enterMoves: _onEnterMoves,
                cells: _changeCellProperties,
            };

            vm.settings = _.cloneDeep(vm.defaultSettings);
            vm.settings = _.assign(vm.settings, $scope.htSettings);
            if (!vm.settings.isCellValueEditable){
                vm.settings.editor = false;
            } else {
                vm.settings.editor = "tubeCellInput";
                vm.settings.multiSelect = true;
            }
            if (vm.settings.isCellStatusEditable){
                vm.settings.editor = false;
                vm.settings.multiSelect = false;
            }

            $scope.$watch("dataBox", function(newOne, oldOne){
                if (newOne == oldOne){
                    return;
                }
                if (!newOne){
                    vm.projectSampleTypeOptions.length = 0;
                }
                var projectId = newOne.projectId;
                var sampleTypeId = newOne.sampleTypeId;
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    // console.log(data);
                });
            }, true);

            //================通用方法==================//
            // 获取handsontable实例
            function _getTableCtrl(){
                if (!$scope.htInstance.hTableInstance || !Object.keys($scope.htInstance.hTableInstance).length){
                    $scope.htInstance.hTableInstance = hotRegisterer.getInstance('tubeBoxTable');
                }
                return $scope.htInstance.hTableInstance;
            }

            // 根据父级元素调整表格宽度
            function _changeTubeColumnWidth(tableCtrl, tableSettings){
                tableSettings = tableSettings || _getSettings();
                var tableWrapper = tableCtrl.rootElement;
                tableWrapper = $(tableWrapper).closest("div");
                var maxWidth = tableWrapper.innerWidth();
                var rowHeaderWidth = +tableSettings.rowHeaderWidth;
                tableSettings.columnHeaderHeight = rowHeaderWidth;
                tableSettings.width = maxWidth;
                tableCtrl.updateSettings(tableSettings);
            }

            // 生成新的管子数据
            function _buildNewTubeData(box, sampleCode, row, col){
                var tube = {
                    id: null,
                    frozenTubeId: null,
                    stockInBoxId: box.id,
                    frozenBoxCode: box.frozenBoxCode,
                    sampleCode: sampleCode,
                    sampleTempCode: null,
                    frozenTubeCode: null,
                    frozenTubeState: "2002",
                    status: "3001",

                    // projectId: box.projectId,
                    // projectCode: box.projectCode,
                    // projectSiteId: box.projectSiteId,
                    // projectSiteCode: box.projectSiteCode,

                    backColor: box.backColor,
                    backColorForClass: box.backColorForClass,
                    frontColor: box.frontColor,
                    frontColorForClass: box.frontColorForClass,

                    memo: null,

                    tubeColumns: (col||"").toString(),
                    tubeRows: (row||"").toString()
                };

                switch (box.sampleTypeCode){
                    case "97":
                    case "98":
                        break;
                    default:
                        tube.sampleTypeId = box.sampleTypeId;
                        tube.sampleType = box.sampleType;
                        tube.sampleTypeCode = box.sampleTypeCode;
                        tube.sampleTypeName = box.sampleTypeName;

                        tube.projectId = box.projectId;
                        tube.projectCode = box.projectCode;
                        tube.projectSiteId = box.projectSiteId;
                        tube.projectSiteCode = box.projectSiteCode;

                        if (!box.isMixed){
                            tube.sampleClassificationId = box.sampleClassificationId;
                            tube.sampleClassification = box.sampleClassification;
                            tube.sampleClassificationCode = box.sampleClassificationCode;
                            tube.sampleClassificationName = box.sampleClassificationName;
                        } else {
                            var sampleClass = _.find(vm.projectSampleTypeOptions, {columnsNumber: tube.tubeColumns});
                            tube.sampleClassificationId = sampleClass.sampleClassificationId;
                            tube.sampleClassification = sampleClass;
                            tube.sampleClassificationCode = sampleClass.sampleClassificationCode;
                            tube.sampleClassificationName = sampleClass.sampleClassificationName;

                            tube.backColorForClass = sampleClass.backColor;
                            tube.frontColorForClass = sampleClass.frontColor;
                        }
                }

                return tube;
            }

            // 验证盒内管子数据
            function _validTubeData(row, col, tubeData, connectServer){
                if (!tubeData || !tubeData.status){
                    return false;
                }
                var box = $scope.dataBox;
                var gridData = _getGridData();
                var errorPos = [];
                _.each(gridData, function(rowTubes, rowIndex){
                    _.each(rowTubes, function(tube, colIndex){
                        if (tube.sampleCode == tubeData.sampleCode
                            && (row != rowIndex || col != colIndex)){
                            if (!box.isMixed){
                                errorPos.push([rowIndex, colIndex, tube, "盒内样本重复，相同的样本编码。"]);
                            } else if (box.sampleTypeCode == "98") {
                                if (tubeData.sampleClassificationId && tube.sampleClassificationId == tubeData.sampleClassificationId){
                                    errorPos.push([rowIndex, colIndex, tube, "盒内样本重复，相同的样本分类。"]);
                                } else if (tube.sampleTypeId == tubeData.sampleTypeId){
                                    errorPos.push([rowIndex, colIndex, tube, "盒内样本重复，相同的样本类型。"]);
                                }
                            } else if (box.sampleTypeCode == "99" || box.isMixed) {
                                if (!tubeData.sampleClassificationId){
                                    errorPos.push([rowIndex, colIndex, tube, "混合样本类型的样本，无样本分类信息。"]);
                                } else if (tube.sampleClassificationId == tubeData.sampleClassificationId){
                                    errorPos.push([rowIndex, colIndex, tube, "盒内样本重复，相同的样本分类。"]);
                                }
                            }
                        } else if (row == rowIndex && col == colIndex){
                            if (tube.flage && tube.flage == 2 && !_.isEuqal(tube, tubeData)){
                                errorPos.push([rowIndex, colIndex, tubeData, "此样本为原盒样本，不能被修改。"]);
                            } else if (box.sampleTypeCode != "98" && box.sampleTypeCode != "97"){
                                if (tubeData.projectId != box.projectId) {
                                    errorPos.push([rowIndex, colIndex, tubeData, "样本的项目与盒子不一致。"]);
                                } else if (tubeData.sampleTypeId != box.sampleTypeId){
                                    errorPos.push([rowIndex, colIndex, tubeData, "样本的类型与盒子不一致。"]);
                                } else if (!box.isMixed && tubeData.sampleClassificationId != box.sampleClassificationId){
                                    errorPos.push([rowIndex, colIndex, tubeData, "样本的分类与盒子不一致。"]);
                                }
                            }
                        }
                    });
                });

                if (errorPos.length){
                    console.log(errorPos);
                }

                return errorPos.length == 0;
            }

            // 获取空位编码所在的坐标位置
            function _convertTubePositionToCoordinate(rowCode, colCode){
                var settings = _getSettings();
                var coord = {row:null, col:null};

                if (rowCode){
                    coord.row = _.indexOf(settings.rowHeaders, rowCode);
                }
                if (colCode){
                    coord.col = _.indexOf(settings.colHeaders, colCode);
                }

                return coord;
            }

            // 获取坐标位置
            function _convertCoordinateToTubePosition(row, col){
                var settings = _getSettings();
                var coord = {row:null, col:null};

                if (row){
                    coord.row = settings.rowHeaders[row];
                }
                if (col){
                    coord.col = settings.colHeaders[col];
                }

                return coord;
            }


            //================表格配置==================//
            // 当初始化完成时触发
            function _onInit(){
                $scope.htInstance.hTableInstance = this;
                $(window).resize(function(){
                    // console.log(_getTableCtrl());
                    _changeTubeColumnWidth(_getTableCtrl());
                });
                var settings = vm.settings;
                $(this.container).toggleClass("cell_value_editing", !!settings.isCellValueEditable)
                $(this.container).toggleClass("cell_status_editing", !!settings.isCellStatusEditable)
            }

            // 当获取列头DOM时触发
            function _onGetTableColHeader(col, th){
                // console.log(arguments);

                // 给左上角的列头增加点击事件，点击后全选所有单元格
                if (col == -1){
                    $(th).off("click");
                    $(th).click(function(){
                        _selectAll();
                    });
                }
            }

            var _originalValueBeforeEditing = null;
            // 当开始触发Editor编辑时响应
            function _onBeginEditing(row, col){
                var tableCtrl = _getTableCtrl();
                // var input = tableCtrl.getActiveEditor();
                var tube = tableCtrl.getDataAtCell(row, col);
                // $(input).val(tube.sampleCode);
                // $(input).select();
                console.log("_onBeginEditing", arguments);
                _originalValueBeforeEditing = tube;
            }

            function _onTubeDataValidating(value, row, col, source){
                var tableCtrl = _getTableCtrl();
                console.log("_onTubeDataValidating", arguments);
            }

            // 当表格数据发生改变时，触发这个响应
            function _onTubeCellChanging(changes, source){
                console.log("_onTubeCellChanging", arguments)
                if (source != "edit" || !changes[0]){
                    return;
                }

                var tableCtrl = _getTableCtrl();
                var row = changes[0][0];
                var col = changes[0][1];
                var oldValue = changes[0][2];
                var newValue = changes[0][3];

                // 当删除一个Cell时source == undefined, newValue == ""
                if (!newValue || !newValue.sampleCode || !newValue.sampleCode.length){
                    newValue = {};
                    changes[0][3] = newValue;
                } else if (!oldValue || oldValue == "" || !Object.keys(oldValue).length
                    || !oldValue.status || (!oldValue.sampleCode && !oldValue.sampleTypeCode)){
                    // 新建样本
                    var tube = {};
                    if (newValue.sampleCode && newValue.sampleCode.length){
                        tube = _buildNewTubeData($scope.dataBox, newValue.sampleCode, vm.api.rowHeaders[row], vm.api.columnHeaders[col]);
                    }
                    newValue = tube;
                    changes[0][3] = newValue;
                }

                if (newValue.status && !_validTubeData(row, col, newValue)){
                    // 当编辑失败后应该回滚数据
                    tableCtrl.setDataAtCell(row,col,_originalValueBeforeEditing,"auto");
                    // changes[0] = null;
                    changes.splice(0, 1);
                }
            }

            // 当表格数据发生改变后，触发这个响应
            function _onTubeCellChanged(changes, source){
                console.log("_onTubeCellChanged", arguments)

                if (source != "edit" || !changes[0]){
                    return;
                }
                var tableCtrl = _getTableCtrl();
                var row = changes[0][0];
                var col = changes[0][1];
                var oldValue = changes[0][2];
                var newValue = changes[0][3];

                if (!newValue || !newValue.sampleCode || !newValue.sampleCode.length){
                    newValue = {};
                    tableCtrl.setDataAtCell(row, col, {}, "auto");
                }
                // 当修改的是99类型的样本，当前行的样本编码需要一致
                if ($scope.dataBox.sampleTypeCode == "99"){
                    var tubes = tableCtrl.getDataAtRow(row);
                    _.each(tubes, function(t, i){
                        if (i == col || (t && t.flag == 2)) {
                            // flag == 2原盒冻存管不能修改
                            return;
                        }
                        if (!newValue || !newValue.sampleCode || !newValue.sampleCode.length){
                            tableCtrl.setDataAtCell(row, i, {}, "auto");
                        } else {
                            if (!t || t == "" || !Object.keys(t).length){
                                t = _buildNewTubeData($scope.dataBox, newValue.sampleCode,
                                    vm.api.rowHeaders[row], vm.api.columnHeaders[i]);
                            } else {
                                t.sampleCode=newValue.sampleCode;
                            }
                            tableCtrl.setDataAtCell(row, i, t, "auto");
                        }
                    });
                }
                _refresh();
            }

            // 根据管子数据显示表格
            function _tubeCellRenderer(hotInstance, td, row, col, prop, value, cellProperties){
                // console.log(arguments);
                td.style.position = 'relative';
                if (!value || value == "" || !Object.keys(value).length){
                    // 清空单元格样式
                    td.innerHTML = "";
                    td.style.backgroundColor = "";
                    td.style.color = "";
                    $(td).removeClass("error-tube-color empty-tube-color empty-hole-color");
                    return;
                }

                if (value.memo && value.memo.length){
                    // 增加MEMO的Tooltip
                    cellProperties.comment = {value:value.memo};
                }

                if(!value.sampleCode){
                    value.sampleCode = "";
                }

                $(td).toggleClass('htDimmed', cellProperties.readOnly);
                $(td).toggleClass('htReadOnly', cellProperties.readOnly);

                // 设置单元格背景色
                if (value.sampleClassification && value.sampleClassification.backColor){
                    td.style.backgroundColor = value.sampleClassification.backColor;
                } else if (value.backColor){
                    td.style.backgroundColor = value.backColor;
                }

                // 设置单元格前景色
                if (value.sampleClassification && value.sampleClassification.frontColor){
                    td.style.color = value.frontColor;
                } else if (value.frontColor){
                    td.style.color = value.frontColor;
                }

                // 设置单元格样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                switch (value.status + ""){
                    case "3001":
                        $(td).removeClass("error-tube-color empty-tube-color empty-hole-color");
                        break;
                    case "3002":
                        $(td).addClass("empty-tube-color");
                        break;
                    case "3003":
                        $(td).addClass("empty-hole-color");
                        break;
                    case "3004":
                        $(td).addClass("error-tube-color");
                        break;
                    default:
                        $(td).removeClass("error-tube-color empty-tube-color empty-hole-color");
                        break;
                }

                var code = value.sampleCode && value.sampleCode != " " ? value.sampleCode : (value.sampleTempCode||"");
                $(td).html("");
                // 添加样本编码
                $("<div style='line-height: 20px;word-wrap: break-word'>"+code+"</div>")
                    .appendTo(td);
                // 添加样本状态标识
                $("<div  class='tube-status'>"+value.status+"</div>")
                    .appendTo(td);

            }

            // 当选中单元格后，触发这个响应
            function _onTubeCellSelected(row, col, row2, col2){
                var tableCtrl = _getTableCtrl();
                var tableSettings = _getSettings();
                var selectedTubes = vm.api.selectedTubes;
                var selectedTubeElements = vm.api.selectedTubeElements;

                if (tableSettings.isCellStatusEditable){
                    // 在修改状态时不能多选
                    selectedTubes.length = 0;
                    selectedTubeElements.length = 0;
                    $(tableCtrl.rootElement).find(".temp.selected-sample-color").remove();
                    return;
                }

                if(_multiSelect || window.event && window.event.ctrlKey){
                    _multiSelect = false;
                } else {
                    // 不是跳格多选时，清空之前选中的内容
                    $(tableCtrl.rootElement).find(".temp.selected-sample-color").remove();
                    selectedTubes.length = 0;
                    selectedTubeElements.length = 0;
                }

                var startRow = Math.min(row, row2);
                var endRow = Math.max(row, row2);
                var startCol = Math.min(col, col2);
                var endCol = Math.max(col, col2);
                // 记录本次选中的样本
                for (var i = startRow; i <= endRow; ++i ){
                    for (var j = startCol; j <= endCol; ++j){
                        var cell = tableCtrl.getCell(i,j);
                        var tube = tableCtrl.getDataAtCell(i,j);

                        // 原盒的管子不能选择
                        if (tube && tube.flag == 2){
                            continue;
                        }

                        var code = (tube.sampleCode||"") + (tube.sampleTempCode||"");
                        if (!_.trim(code).length){
                            continue;
                        }
                        selectedTubes.push(tube);
                        selectedTubeElements.push(cell);

                        // 给选中的样本增加选中标记
                        if (!$(cell).find(".temp.selected-sample-color").length){
                            $(cell).append('<div class="temp selected-sample-color"></div>');
                        }
                    }
                }
            }

            // 当点击一个单元格后，触发这个响应
            function _onTubeCellClicked(event, coords, td){
                var tableSettings = _getSettings();
                var tableCtrl = _getTableCtrl();
                var tube = tableCtrl.getDataAtCell(coords.row, coords.col);

                if (tableSettings.isCellStatusEditable && tube.status){
                    switch (tube.status+""){
                        case "3001":
                            tube.status = "3002";
                            break;
                        case "3002":
                            tube.status = "3003";
                            break;
                        case "3003":
                            tube.status = "3004";
                            break;
                        case "3004":
                            tube.status = "3001";
                            break;
                        default:
                            break;
                    }
                    tableCtrl.setDataAtCell(coords.row, coords.col, tube, "auto");
                }

            }

            // 当点击一个单元格时，触发这个响应
            function _onTubeCellClicking(event, coords, td){}

            // 当输入完数据回车时单元格移动的位置。
            function _onEnterMoves() {
                var tableCtrl = _getTableCtrl();
                var rowCount = tableCtrl.countRows();
                var colCount = tableCtrl.countCols();
                var selectedCell = tableCtrl.getSelected();
                var selectedRow = selectedCell[0];
                var selectedCol = selectedCell[1];
                if ($scope.dataBox.sampleTypeCode == 99){
                    // 99类型直接移动到下一行
                    var coord = null;
                    var row = selectedRow + 1;
                    var col = selectedCol;

                    for(;row < rowCount; row++){
                        var nextRowCells = tableCtrl.getDataAtRow(row);
                        var nextCell = _.find(nextRowCells, function(c){return c.flag == 2});
                        if (nextCell && nextCell.length){
                            continue;
                        } else {
                            coord = {row: row - selectedRow, col: col - selectedCol};
                            return coord;
                        }
                    }

                    return {row:0,col:0};
                } else {
                    // 其他的向右移一格
                    var coord = null;
                    var row = selectedRow;
                    var col = selectedCol + 1;
                    for (; row < rowCount; row++){
                        var nextRowCells = tableCtrl.getDataAtRow(selectedRow+coord.row);
                        for(;col < colCount; col++){
                            var nextCell = nextRowCells[col];
                            if (nextCell && nextCell.flag == 2){
                                // 跳过不能编辑的单元格
                                continue;
                            } else {
                                coord = {row: row - selectedRow, col: col - selectedCol};
                                return coord;
                            }
                        }
                        col = 0;
                    }

                    return {row:0,col:0};
                }
            }

            // 修改单元格属性
            function _changeCellProperties(row, col, prop) {
                var tableCtrl = _getTableCtrl();
                if (!tableCtrl){
                    return;
                }
                var cellProperties = {};
                var cellData = tableCtrl.getDataAtCell(row, col);
                if (cellData && cellData.flag == 2) {
                    // 单元格有数据，并且有冻存盒ID，表示该单元格在库里有位置
                    // 该单元格不能编辑
                    cellProperties.editor = false;
                    // 该单元格只读
                    cellProperties.readOnly = true;
                    // 该单元格的样式
                    cellProperties.className = 'a00';
                    // cellProperties.readOnlyCellClassName = 'htDimmed';
                }

                return cellProperties;
            }

            //================输出接口==================//
            // 给Table加载数据
            function _loadData(box, tubes){
                $scope.dataBox = box;
                $scope.dataTubes = tubes;

                if (!box){
                    box = {
                        frozenBoxTypeColumns:10,
                        frozenBoxTypeRows:10
                    };
                }

                var boxCols = +box.frozenBoxTypeColumns;
                var boxRows = +box.frozenBoxTypeRows;

                // 生成列头和行头信息
                var columns = new Array(boxCols);
                columns = _.map(columns, function(e,i){return i+1+''});
                var rows = new Array(boxRows);
                rows = _.map(rows, function(e,i){
                    i = i >= 8 ? i+1 : i;
                    return String.fromCharCode("A".charCodeAt(0) + i);
                });

                // 修改表格的行列信息
                var tableCtrl = _getTableCtrl();
                var tableSettings = _getSettings();
                tableSettings.minCols = boxCols;
                tableSettings.minRows = boxRows;
                tableSettings.rowHeaders = rows;
                tableSettings.colHeaders = columns;
                _changeTubeColumnWidth(tableCtrl, tableSettings);

                if (!tubes || !tubes.length){
                    tableCtrl.clear();
                } else {
                    // 生成空表格数据
                    var gridData = new Array(boxRows);
                    gridData = _.map(gridData, function (e,i) {
                        return _.map(new Array(boxCols), function(m,j){
                            var coord = _convertCoordinateToTubePosition(i,j);
                            return {
                                tubeRows: coord.row,
                                tubeColumns: coord.col
                            };
                        });
                    });
                    // 填充加载的数据
                    _.each(tubes, function(t, i){
                        var pos = _convertTubePositionToCoordinate(t.tubeRows, t.tubeColumns);
                        gridData[pos.row][pos.col] = _.cloneDeep(t);
                    });
                    vm.api.gridData = _.cloneDeep(gridData);
                    vm.api.columnHeaders = columns;
                    vm.api.rowHeaders = rows;
                    tableCtrl.loadData(gridData);
                }
                tableCtrl.render();
            }

            // 刷新Table
            function _refresh(){
                var tableCtrl = _getTableCtrl();
                tableCtrl.render();
            }

            // 回滚数据到初始状态
            function _rollbackToOriginalGridData(){
                var gridData = vm.api.gridData;
                var tableCtrl = _getTableCtrl();
                tableCtrl.loadData(gridData);
                tableCtrl.render();
            }

            // 更新初始数据
            function _updateOriginalGridData(){
                var gridData = _getGridData();
                vm.api.gridData = _.cloneDeep(gridData);
            }

            // 获取选中的管子数据
            function _getSelectedData(){
                return vm.api.selectedTubes;
            }

            // 获取选中的管子DOM元素
            function _getSelectedElements(){
                return vm.api.selectedTubeElements;
            }

            // 获取一个范围中的管子数据
            function _getRangeCellData(row, col, row2, col2){
                var tableCtrl = _getTableCtrl();
                var cells = [];
                row2 = row2 || row;
                col2 = col2 || col;

                var startRow = Math.min(row, row2);
                var endRow = Math.max(row, row2);
                var startCol = Math.min(col, col2);
                var endCol = Math.max(col, col2);

                for (var i = startRow; i <= endRow; ++i ){
                    for (var j = startCol; j <= endCol; ++j){
                        var cell = tableCtrl.getDataAtCell(i,j);
                        cells.push(cell);
                    }
                }

                return cells;
            }

            // 获取一个范围中的管子DOM元素
            function _getRangeCellElements(row, col, row2, col2){
                var tableCtrl = _getTableCtrl();
                var cells = [];
                row2 = row2 || row;
                col2 = col2 || col;

                var startRow = Math.min(row, row2);
                var endRow = Math.max(row, row2);
                var startCol = Math.min(col, col2);
                var endCol = Math.max(col, col2);

                for (var i = startRow; i <= endRow; ++i ){
                    for (var j = startCol; j <= endCol; ++j){
                        var cell = tableCtrl.getCell(i,j);
                        cells.push(cell);
                    }
                }

                return cells;
            }

            // 获取表格中的所有数据
            function _getGridData(){
                var tableCtrl = _getTableCtrl();
                return tableCtrl.getData();
            }

            // 获取表格的配置信息
            function _getSettings(){
                var tableCtrl = _getTableCtrl();
                return vm.settings || tableCtrl.getSettings();
            }

            // 修改配置信息
            function _updateSettings(settings){
                var tableCtrl = _getTableCtrl();
                var tableSettings = _getSettings();
                tableSettings = _.assign(tableSettings, settings);

                if (!tableSettings.isCellValueEditable){
                    tableSettings.editor = false;
                } else {
                    tableSettings.multiSelect = true;
                    tableSettings.editor = "tubeCellInput";
                }
                if (tableSettings.isCellStatusEditable){
                    tableSettings.multiSelect = false;
                    tableSettings.editor = false;
                }

                var settings = tableSettings;
                $(tableCtrl.container).toggleClass("cell_value_editing", !!settings.isCellValueEditable)
                $(tableCtrl.container).toggleClass("cell_status_editing", !!settings.isCellStatusEditable)


                tableCtrl.updateSettings(tableSettings);
            }

            // 是否需要跳格多选
            var _multiSelect = false;
            // 选中指定区域或单元格
            function _selectRangeCell(row, col, row2, col2){
                var tableCtrl = _getTableCtrl();
                if (row.length){
                    _.each(row, function(pos){
                        _multiSelect = true;
                        tableCtrl.selectCell(pos[0], pos[1]);
                    });
                } else {
                    tableCtrl.selectCell(row, col, row2, col2);
                }
            }

            // 全选
            function _selectAll(){
                var rows = vm.api.rowHeaders.length;
                var cols = vm.api.columnHeaders.length;
                _selectRangeCell(0,0,rows-1,cols-1);
            }

            function _exchangePos(srcRow, srcCol, desRow, desCol){
                var srcPos = _convertTubePositionToCoordinate(srcRow, srcCol);
                var desPos = _convertTubePositionToCoordinate(desRow, desCol);
                var tableCtrl = _getTableCtrl();
                var settings = _getSettings();
                var columns = settings.colHeaders;
                var rows = settings.rowHeaders;
                var tubeA = tableCtrl.getDataAtCell(srcPos.row, srcPos.col);
                var tubeB = tableCtrl.getDataAtCell(desPos.row, desPos.col);
                var temp = {};

                if (tubeA.flag == 2){
                    toastr.error("原盒中的管子不能移位。");
                    return;
                }

                tubeA.tubeColumns = desCol;
                tubeA.tubeRows = desRow;
                tubeB.tubeColumns = srcCol;
                tubeB.tubeRows = srcRow;

                tableCtrl.setDataAtCell([
                    [srcPos.row, srcPos.col, {}],
                    [desPos.row, desPos.col, {}],
                    [srcPos.row, srcPos.col, tubeB],
                    [desPos.row, desPos.col, tubeA]]);
                // tableCtrl.setDataAtCell([[srcRow, srcCol, tubeB], [desRow, desCol, tubeA]]);
            }

            function _exchangeSelectedTubePosition(){
                var tubes = _getSelectedData();
                if (tubes.length > 2){
                    toastr.error("选中的管子多于2个。");
                    return;
                }

                if (-1 != _.indexOf(tubes, {flag: 2})){
                    toastr.error("原盒中的管子不能移位。");
                    return;
                }

                _exchangePos(tubes[0].tubeRows, tubes[0].tubeColumns, tubes[1].tubeRows, tubes[1].tubeColumns)
            }

        }

        function  linkFunc(scope,  element,  attrs, vm)  {
            // scope.$watch("dataTubes", function(newOne, oldOne){
            //     console.log(arguments);
            //     if (newOne == oldOne){
            //         return;
            //     }
            //     vm.api.loadData(scope.dataBox, newOne);
            // }, true);
        }


    }
})();
