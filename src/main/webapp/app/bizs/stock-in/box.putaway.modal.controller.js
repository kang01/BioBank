/**
 * Created by gaoyankang on 2017/4/4.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxPutAwayModalController', BoxPutAwayModalController);

    BoxPutAwayModalController.$inject = ['hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','AlertService','$q','$timeout','items',
        'frozenBoxByCodeService', 'FrozenPosService','AreasByEquipmentIdService','EquipmentService','SupportRackType','StockInBoxService'];

    function BoxPutAwayModalController(hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,AlertService,$q,$timeout,items,
        frozenBoxByCodeService,FrozenPosService,AreasByEquipmentIdService,EquipmentService,SupportRackType,StockInBoxService) {

        var vm = this;

        // 输入参数stockInCode, boxIds, boxes
        vm.items = items;
        // 入库单编码
        vm.stockInCode = items.stockInCode;
        // 需要上架的盒子
        vm.frozenBoxes = [];
        // 当前选中的架子
        vm.selectedShelf = null;
        // 选中的要上架的盒子
        vm.selectedBox = {};
        // 所有的架子类型
        vm.shelfTypes = [];
        // 需要保存的盒子上架信息
        vm.putInShelfBoxes = {};

        _loadInitializeDataFromServer(items.boxIds);
        _initPageEvents();
        _initShelvesList();
        _initStockInBoxes();
        _initShelfDetailsTable();

        // 加载页面上的初始化数据
        function _loadInitializeDataFromServer(boxCodes){
            // 获取所有冻存架类型
            var promiseForShelfType = SupportRackType.query({}, function(data, headers){
                vm.shelfTypes = data;
            }, onError).$promise;
            // 获取所有冻存设备
            var promiseForEquipment = EquipmentService.query({}, function(data){
                vm.equipmentsOptions = data;
            }, onError).$promise;

            // vm.frozenBoxes = items.boxes;
            // _.forEach(vm.frozenBoxes, function(box){vm.selectedBox[box.frozenBoxCode] = false;});
            var promiseForFrozenBox = StockInBoxService.getStockInBoxByCodes(boxCodes).then(function(res){
                vm.frozenBoxes = res.data;
                _.forEach(vm.frozenBoxes, function(box){
                    vm.selectedBox[box.frozenBoxCode] = false;
                });
            }, onError);
            $q.all([promiseForShelfType, promiseForEquipment, promiseForFrozenBox]).then(function(data){
            });
        }
        // 初始化页面上的通用控件的事件响应
        function _initPageEvents(){
            vm.cancel = _cancelModal;
            vm.ok = function () {
                _saveBoxPositions();
            };
            // 判断确定按钮是否可以使用
            vm.canBeSave = function(){
                return true && !(vm.putInShelfBoxes && Object.keys(vm.putInShelfBoxes).length);
            };
            // 盒子上架
            vm.putInShelf = _putInShelf;
            // 复原
            vm.rollback = _rollback;
        }
        // 初始化选择冻存架功能的相关控件
        function _initShelvesList(){
            vm.shelves = [];
            vm.selectShelf = _selectShelf;

            // 初始化冻存设备下拉控件
            vm.equipmentsOptions = [];
            vm.equipmentsOptionsConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    AreasByEquipmentIdService.query({id:value}, function (data){
                        vm.areasOptions = data;
                        vm.shelves.length = 0;
                    }, onError);
                    for(var i = 0; i < vm.equipmentsOptions.length; i++){
                        if(value == vm.equipmentsOptions[i].id){
                            vm.equipmentCode = vm.equipmentsOptions[i].equipmentCode;
                        }
                    }
                }
            };
            // 初始化冻存区下拉控件
            vm.areasOptions = [];
            vm.areasOptionsConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    for(var i = 0; i < vm.areasOptions.length; i++){
                        if(value == vm.areasOptions[i].id){
                            vm.areaCode = vm.areasOptions[i].areaCode;
                        }
                    }
                    FrozenPosService.getIncompleteShelves(vm.equipmentCode, vm.areaCode).then(function(res){
                        vm.shelves = res.data;
                    }, onError);
                }
            };

            vm.dtShelvesListOptions = DTOptionsBuilder.newOptions()
                .withDOM("t").withScroller().withOption('scrollY', 338);

            // 选中一个冻存架
            function _selectShelf($event, shelf){
                var element = $($event.target);
                var table = element.closest('table');
                $(".rowLight", table).removeClass('rowLight');
                element.addClass('rowLight');

                vm.selectedShelf = shelf;

                _loadShelfDetails(shelf);
            }

            // 向服务端加载冻存架详情
            function _loadShelfDetails(shelf){
                if (shelf == null || typeof shelf === 'undefined'){
                    shelf = vm.selectedShelf;
                }
                var shelfType = _.filter(vm.shelfTypes, {id: shelf.supportRackTypeId})[0];
                shelfType = shelfType || {};

                FrozenPosService.getFrozenBoxesByPosition(shelf.equipmentCode, shelf.areaCode, shelf.supportRackCode).then(function(res){
                    var boxes = res.data;
                    if (boxes == null || typeof boxes === 'undefined' || !boxes.length){
                        boxes = [];
                    }
                    // 架子上的行数
                    var countOfRows = shelfType.supportRackRows || 4;
                    // 架子上的列数
                    var countOfCols = shelfType.supportRackColumns || 4;
                    // 架子定位列表的控制对象
                    var tableCtrl = _getShelfDetailsTableCtrl();
                    // 架子定位列表的总宽度
                    var tableWidth = $(tableCtrl.container).width();
                    // 架子定位列表的配置信息
                    var settings = vm.shelfDetailsTableSettings;
                    // 架子定位列表 行头 的宽度
                    var rowHeaderWidth = settings.rowHeaderWidth;
                    // 架子定位列表每列的宽度
                    var colWidth = (tableWidth - rowHeaderWidth) / countOfCols;

                    // 创建架子定位列表的 列头
                    var columns = [];
                    var charCode = 'A'.charCodeAt(0);
                    for(var i = 0; i < countOfCols; ++i){
                        var col = {
                            data: 0,
                            title: String.fromCharCode(charCode + i)
                            // readOnly: true,
                        };
                        columns.push(col);
                    }

                    // 创建架子定位列表的定位数据
                    var arrayBoxes = [];
                    var emptyPos = null;
                    // 先列
                    for(var i = 0; i < countOfCols; ++i){
                        // 再行
                        for(var j = 0; j < countOfRows; ++j){
                            arrayBoxes[j] = arrayBoxes[j] || [];
                            var pos = {columnsInShelf: String.fromCharCode(charCode + i), rowsInShelf: j+1+""};
                            // 从已入库的盒子中查询架子中该位置的盒子
                            var boxesInShelf = _.filter(boxes, pos);
                            if (boxesInShelf.length){
                                arrayBoxes[j][i] = boxesInShelf[0];
                            } else {
                                var boxesPos = vm.putInShelfBoxes[shelf.id];
                                // 从已上架的盒子中查询架子中该位置的盒子
                                boxesInShelf = _.filter(boxesPos||[], pos);
                                if (boxesInShelf.length){
                                    arrayBoxes[j][i] = boxesInShelf[0];
                                } else {
                                    // 该位置没有任何盒子
                                    arrayBoxes[j][i] = {
                                        frozenBoxId: null,
                                        frozenBoxCode: "",
                                        columnsInShelf: String.fromCharCode(charCode + i),
                                        rowsInShelf: j + 1 + "",
                                        isEmpty: true
                                    };
                                    if (!emptyPos){
                                        emptyPos = {row:j,col:i};
                                    }
                                }
                            }

                            // 该位置的位置信息
                            arrayBoxes[j][i].supportRackCode = shelf.supportRackCode;
                            arrayBoxes[j][i].areaCode = shelf.areaCode;
                            arrayBoxes[j][i].equipmentCode = shelf.equipmentCode;
                            arrayBoxes[j][i].rowNO = j;
                            arrayBoxes[j][i].colNO = i;
                        }
                    }

                    // 修改架子定位列表的配置信息
                    settings.width = tableWidth;
                    settings.height = 380;
                    settings.minRows = countOfRows;
                    settings.minCols = countOfCols;
                    settings.colWidths = colWidth;
                    settings.manualColumnResize = colWidth;
                    settings.columns = columns;

                    // 更新架子定位列表并选中第一个空位置
                    tableCtrl.loadData(arrayBoxes);
                    if (emptyPos){
                        // tableCtrl.selectCell(0, 0);
                        tableCtrl.selectCell(emptyPos.row, emptyPos.col);
                    }
                }, onError);
            }

        }
        // 初始化要被上架的冻存盒列表
        function _initStockInBoxes(){
            // 过滤已上架的冻存盒
            vm.filterNotPutInShelfBoxes = function(box){
                return !box.isPutInShelf && true;
            };

            // 全选或取消全选冻存盒
            vm.toggleAll = function (selectAll, selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            };

            // 更新全选选项
            vm.toggleOne = function (selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAllBox = false;
                            return;
                        }
                    }
                }
                vm.selectAllBox = true;
            };

            vm.dtBoxesListColumns = [
                DTColumnBuilder.newColumn(0).withOption("width", "30").notSortable(),
                DTColumnBuilder.newColumn(1).withOption("width", "80"),
                DTColumnBuilder.newColumn(2).withOption("width", "80"),
                DTColumnBuilder.newColumn(3).withOption("width", "auto")
            ];
            vm.dtBoxesListOptions = DTOptionsBuilder.newOptions()
                .withDOM("t").withOption('paging', false).withOption('order', [[1, 'asc' ]]).withScroller().withOption('scrollY', 338);
            vm.dtBoxesListInstance = {};
            $timeout(function(){
                if (vm.dtBoxesListInstance.rerender){
                    vm.dtBoxesListInstance.rerender();
                }
            },200);
        }
        // 初始化冻存架上架位置的列表控件
        function _initShelfDetailsTable(){
            vm.shelfDetailsTableCtrl = null;
            vm.shelfDetailsTableSettings = {
                // 点击表格外部时，表格内选项仍然能够选中
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,

                // 表格内单元格只能单选
                multiSelect: false,

                // 显示行和列的Title
                showColHeaders: true,
                showRowHeaders: true,

                // 默认每列的宽度
                colWidths: 30,
                // 行头的宽度
                rowHeaderWidth: 30,

                // 默认行列Title
                rowHeaders: ['1','2','3','4'],
                colHeaders: ['A','B','C','D'],

                // 默认显示的行和列的数量
                minRows: 4,
                minCols: 4,

                // 默认显示的数据
                data: [["","","",""]],

                // 默认表格占用的宽高，超过范围显示滚动条
                // width: 584,
                height: 380,
                // 是否自动拉伸单元格
                stretchH: 'all',

                fillHandle:false,
                editor: false,

                // 单元格的渲染函数
                renderer:_customRenderer,
                // Enter键单元格位置的移动函数
                enterMoves: _moveCursorWhenPressEnterKey,
                // 根据单元格的Data修改单元格的属性
                cells: _changeCellProperties,
                // 单元格选中时的处理函数
                afterSelection: _selectingCell
            };

            // 初始刷新表格中的内容
            $timeout(function(){
                var hot = _getShelfDetailsTableCtrl();
                if (hot){
                    // hot.clear();
                    hot.loadData([["","","",""]]);
                    // hot.render();
                }
            },200);

            // 当一个单元格被选中
            function _selectingCell(r, c, r2, c2, preventScrolling){
                var hot = _getShelfDetailsTableCtrl();
                if (!hot){
                    return;
                }
                var cellProperties = hot.getCellMeta(r, c);
                if (cellProperties.readOnly === true || cellProperties.isEmpty === false){
                    // todo:: 撤销单元格的选中，单现在没找到解决方法。
                    return false;
                    // preventScrolling.value = true;
                }
            }

            // 修改单元格属性
            function _changeCellProperties(row, col, prop) {
                var hot = _getShelfDetailsTableCtrl();
                if (!hot){
                    return;
                }

                var cellProperties = {};
                var cellData = hot.getDataAtCell(row, col);
                cellProperties.isEmpty = true;
                if (cellData && cellData.frozenBoxId) {
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

            // 移动焦点到下一个位置
            function _moveCursorWhenPressEnterKey(){
                // 先行后列，行满时再去下一列，列满时回到，本列第一行

                var tableCtrl = _getShelfDetailsTableCtrl();
                var selectedPos = tableCtrl.getSelected();
                var row = selectedPos[0];
                var col = selectedPos[1];
                var rowOffset = 1;
                var colOffset = 0;

                if (row >= tableCtrl.countRows()){
                    rowOffset = -row;
                    colOffset = 1;
                }

                if (col >= tableCtrl.countCols()){
                    colOffset = 0;
                }

                return {row: rowOffset, col: colOffset};
            }

            // 渲染单元格
            function _customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
                var hot = _getShelfDetailsTableCtrl();
                var cellData = value;
                if (hot){
                    cellData = hot.getDataAtCell(row, col);
                    // console.log(cellData);
                }
                if (cellProperties.readOnly){
                    $(td).addClass('htDimmed');
                    $(td).addClass('htReadOnly');
                } else {
                    $(td).removeClass('htDimmed');
                    $(td).removeClass('htReadOnly');
                }

                var $div = $("<div/>").addClass(cellProperties && cellProperties.className ? cellProperties.className : "");
                if (value){
                    $div.html(value.frozenBoxCode)
                        .data("frozenBoxId", value.frozenBoxId)
                        .data("frozenBoxCode", value.frozenBoxCode)
                        .data("columnsInShelf", value.columnsInShelf)
                        .data("rowsInShelf", value.rowsInShelf)
                        .data("countOfSample", value.countOfSample);
                }

                $(td).html("");
                $(td).append($div);
            }
        }


        // 公共方法
        // 服务端错误处理
        function onError(error) {
            AlertService.error(error.data.message);
        }
        // 关闭窗口
        function _closeModal(){
            $uibModalInstance.close();
        }
        // 取消窗口
        function _cancelModal(){
            $uibModalInstance.dismiss('cancel');
        }
        // 盒子上架
        function _putInShelf(){
            var shelf = vm.selectedShelf;
            var shelfType = _.filter(vm.shelfTypes, {id: shelf.supportRackTypeId})[0] || {};
            var countOfRows = shelfType.supportRackRows || 4;
            var countOfCols = shelfType.supportRackColumns || 4;

            var tableCtrl = _getShelfDetailsTableCtrl();
            // [startRow, startCol, endRow, endCol].
            var startEmptyPos = tableCtrl.getSelected();
            var cellRow = startEmptyPos[0];
            var cellCol = startEmptyPos[1];

            for(var i in vm.selectedBox){
                var boxes = vm.putInShelfBoxes[shelf.id] || {};
                vm.putInShelfBoxes[shelf.id] = boxes;
                if (vm.selectedBox[i]){
                    // 遍历选中的冻存盒，i是冻存盒的Code
                    var box = _.filter(vm.frozenBoxes, {frozenBoxCode: i})[0];
                    if (!box){
                        continue;
                    }

                    // 从选中的位置，开始上架，先行后列
                    for (; cellCol < countOfCols && !box.isPutInShelf; ++cellCol){
                        for(; cellRow < countOfRows && !box.isPutInShelf; ++cellRow){
                            var cellData = tableCtrl.getDataAtCell(cellRow, cellCol);
                            if (cellData && cellData.isEmpty){
                                // 单元格为空可以上架
                                // 标记盒子已经上架
                                vm.selectedBox[i] = false;
                                box.isPutInShelf = true;
                                cellData.frozenBoxCode = box.frozenBoxCode;
                                cellData.isEmpty = false;

                                // 记录盒子的上架信息
                                boxes[box.frozenBoxCode] = {
                                    frozenBoxId: box.frozenBoxId,
                                    frozenBoxCode: box.frozenBoxCode,

                                    rowsInShelf: cellData.rowsInShelf,
                                    columnsInShelf: cellData.columnsInShelf,
                                    supportRackCode: cellData.supportRackCode,
                                    areaCode: cellData.areaCode,
                                    equipmentCode: cellData.equipmentCode,

                                    rowNO: cellRow,
                                    colNO: cellCol
                                };
                                break;
                            }
                        }
                        if (box.isPutInShelf){
                            break;
                        }
                        cellRow = 0;
                    }

                }
            }

            tableCtrl.render();
            vm.dtBoxesListInstance.rerender();
        }
        function _rollback(){
            var tableCtrl = _getShelfDetailsTableCtrl();
            var shelf = vm.selectedShelf;
            for(var shelfId in vm.putInShelfBoxes){
                var boxes = vm.putInShelfBoxes[shelfId];
                for (var code in boxes){
                    var boxPos = boxes[code];
                    var box = _.filter(vm.frozenBoxes, {frozenBoxCode: code})[0];
                    box.isPutInShelf = undefined;
                    vm.selectedBox[code] = false;
                    vm.selectAllBox = false;

                    if (shelf.id == shelfId){
                        var cellData = tableCtrl.getDataAtCell(boxPos.rowNO, boxPos.colNO);
                        cellData.frozenBoxId = null;
                        cellData.frozenBoxCode = "";
                        cellData.isEmpty = true;
                    }

                    delete boxes[code];
                }
            }

            tableCtrl.render();
            vm.dtBoxesListInstance.rerender();
        }
        // 保存盒子上架的位置
        function _saveBoxPositions(){
            if (vm.putInShelfBoxes){
                var promises = [];
                for (var id in vm.putInShelfBoxes){
                    var boxes = vm.putInShelfBoxes[id];
                    for(var code in boxes){
                        var boxPos = boxes[code];
                        var boxCode = boxPos.frozenBoxCode;
                        var posData = {
                            equipmentCode: boxPos.equipmentCode,
                            areaCode: boxPos.areaCode,
                            supportRackCode: boxPos.supportRackCode,
                            rowsInShelf: boxPos.rowsInShelf,
                            columnsInShelf: boxPos.columnsInShelf
                        };

                        promises.push(StockInBoxService.saveBoxPosition(vm.stockInCode, boxCode, posData));
                    }
                }

                $q.all(promises).then(function(res){
                    vm.putInShelfBoxes = [];
                    _closeModal();
                });
            }
        }
        // 获取上架位置列表的控制实体
        function _getShelfDetailsTableCtrl(){
            vm.shelfDetailsTableCtrl = hotRegisterer.getInstance('shelfDetailsTable');
            return vm.shelfDetailsTableCtrl;
        }




    }
})();
