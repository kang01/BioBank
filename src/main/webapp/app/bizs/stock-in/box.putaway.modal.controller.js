/**
 * Created by gaoyankang on 2017/4/4.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxPutAwayModalController', BoxPutAwayModalController)

    BoxPutAwayModalController.$inject = ['hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModalInstance','$uibModal','AlertService','$q','$timeout','items',
        'frozenBoxByCodeService', 'FrozenPosService','AreasByEquipmentIdService','EquipmentService','SupportRackType'];

    function BoxPutAwayModalController(hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModalInstance,$uibModal,AlertService,$q,$timeout,items,
        frozenBoxByCodeService,FrozenPosService,AreasByEquipmentIdService,EquipmentService,SupportRackType) {

        var vm = this;
        console.log(items);

        vm.frozenTubeArray = [];
        vm.items = items;
        vm.frozenBoxes = [];
        vm.selectedShelf = null;
        vm.selectedBox = {};
        vm.shelfTypes = [];
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            $uibModalInstance.close();
        };

        var promiseForShelfType = SupportRackType.query({}, function(data, headers){
            vm.shelfTypes = data;
        }, onError).$promise;
        var promiseForEquipment = EquipmentService.query({}, function(data){
            vm.equipmentsOptions = data;
        }, onError).$promise;
        // var promiseForFrozenBox = frozenBoxByCodeService.queryByCodes(items.boxIds).then(function(res){
        //     vm.frozenBoxes = res.data;
        // }, onError);
        //
        // $q.all([promiseForShelfType, promiseForEquipment, promiseForFrozenBox]).then(function(data){
        // });
        _getFrozenBoxes();

        _initShelvesList();
        _initStockInBoxes();
        _initShelfDetailsTable();


        function onError(error) {
            AlertService.error(error.data.message);
        }

        function _selectShelf($event, shelf){
            var element = $($event.target);
            var table = element.closest('table');
            $(".rowLight", table).removeClass('rowLight');
            element.addClass('rowLight');

            vm.selectedShelf = shelf;

            _loadShelfDetails(shelf);
        }
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

                var countOfRows = shelfType.supportRackRows || 4;
                var countOfCols = shelfType.supportRackColumns || 4;
                var tableCtrl = _getShelfDetailsTableCtrl();
                var tableWidth = $(tableCtrl.container).width();
                var settings = vm.shelfDetailsTableSettings;
                var rowHeaderWidth = settings.rowHeaderWidth;
                var colWidth = (tableWidth - rowHeaderWidth) / countOfCols;

                var columns = [];
                var charCode = 'A'.charCodeAt(0);
                for(var i = 0; i < countOfCols; ++i){
                    var col = {
                        data: 0,
                        title: String.fromCharCode(charCode + i),
                        // readOnly: true,
                    };
                    columns.push(col);
                }

                var arrayBoxes = [];
                var emptyPos = null;
                for(var i = 0; i < countOfCols; ++i){
                    for(var j = 0; j < countOfRows; ++j){
                        arrayBoxes[j] = arrayBoxes[j] || [];
                        var index = i * countOfRows + j;
                        var boxesInShelf = _.filter(boxes, {columnsInShelf: String.fromCharCode(charCode + i), rowsInShelf: j+""});
                        if (boxesInShelf.length){
                            arrayBoxes[j][i] = boxesInShelf[0];
                        } else {
                            arrayBoxes[j][i] = {
                                frozenBoxId: null,
                                frozenBoxCode: "",
                                columnsInShelf: String.fromCharCode(charCode + i),
                                rowsInShelf: j + "",
                                countOfSample: 0,
                                isEmpty: true,
                            };
                            if (!emptyPos){
                                emptyPos = {row:j,col:i};
                            }
                        }
                    }
                }

                settings.width = tableWidth;
                settings.height = 380;
                settings.minRows = countOfRows;
                settings.minCols = countOfCols;
                settings.colWidths = colWidth;
                settings.manualColumnResize = colWidth;
                settings.columns = columns;

                tableCtrl.loadData(arrayBoxes);
                if (emptyPos){
                    // tableCtrl.selectCell(0, 0);
                    tableCtrl.selectCell(emptyPos.row, emptyPos.col);
                }
            }, onError);
        }
        function _getFrozenBoxes(boxIds){
            vm.frozenBoxes = items.boxes;
            _.forEach(vm.frozenBoxes, function(box){vm.selectedBox[box.frozenBoxCode] = false;});
        }

        function _initShelvesList(){
            vm.equipmentsOptions = [];
            vm.areasOptions = [];
            vm.shelves = [];
            vm.selectShelf = _selectShelf;

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
                            vm.equipmentCode = vm.equipmentsOptions[i].equipmentCode
                        }
                    }
                }
            };
            vm.areasOptionsConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    for(var i = 0; i < vm.areasOptions.length; i++){
                        if(value == vm.areasOptions[i].id){
                            vm.areaCode = vm.areasOptions[i].areaCode
                        }
                    }
                    FrozenPosService.getIncompleteShelves(vm.equipmentCode, vm.areaCode).then(function(res){
                        vm.shelves = res.data;
                    }, onError);
                }
            };

            vm.dtShelvesListOptions = DTOptionsBuilder.newOptions()
                .withDOM("t").withScroller().withOption('scrollY', 338);

        }

        function _initStockInBoxes(){
            vm.filterNotPutInShelfBoxes = function(box){
                return !box.isPutInShelf && true;
            };

            vm.toggleAll = function (selectAll, selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            };
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
                DTColumnBuilder.newColumn(0).notSortable(),
                DTColumnBuilder.newColumn(1),
                DTColumnBuilder.newColumn(2),
                DTColumnBuilder.newColumn(3)
            ];
            vm.dtBoxesListOptions = DTOptionsBuilder.newOptions()
                .withDOM("t").withScroller().withOption('scrollY', 338);
            vm.dtBoxesListInstance = {};
            $timeout(function(){
                vm.dtBoxesListInstance.rerender();
            },200);
        }

        function _initShelfDetailsTable(){
            vm.shelfDetailsTableCtrl = null;
            vm.shelfDetailsTableSettings = {
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,
                multiSelect: false,
                showColHeaders: true,
                showRowHeaders: true,
                colWidths: 30,
                rowHeaderWidth: 30,
                rowHeaders: ['1','2','3','4'],
                colHeaders: ['A','B','C','D'],
                minRows: 4,
                minCols: 4,
                data: [["","","",""]],
                // width: 584,
                height: 380,
                fillHandle:false,
                stretchH: 'all',
                editor: false,
                renderer:_customRenderer,
                enterMoves: _moveCursorWhenPressEnterKey,
                cells: _changeCellProperties,
                afterSelection: _selectingCell
            };

            $timeout(function(){
                _getShelfDetailsTableCtrl().render();
            },200);

            function _selectingCell(r, c, r2, c2, preventScrolling){
                var hot = _getShelfDetailsTableCtrl();
                if (!hot){
                    return;
                }
                var cellProperties = hot.getCellMeta(r, c);
                if (cellProperties.readOnly === true || cellProperties.isEmpty === false){
                    return false;
                    // preventScrolling.value = true;
                }
            }
            function _changeCellProperties(row, col, prop) {
                var hot = _getShelfDetailsTableCtrl();
                if (!hot){
                    return;
                }

                var cellProperties = {};
                var cellData = hot.getDataAtCell(row, col);
                cellProperties.isEmpty = true;
                if (cellData && cellData.frozenBoxId) {
                    cellProperties.editor = false;
                    cellProperties.readOnly = true;
                    cellProperties.className = 'a00';
                    cellProperties.readOnlyCellClassName = 'htDimmed';
                    cellProperties.isEmpty = false;
                } else {
                    cellProperties.isEmpty = cellData && cellData.isEmpty;
                }

                return cellProperties;
            }
            function _moveCursorWhenPressEnterKey(){
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
        function _getShelfDetailsTableCtrl(){
            vm.shelfDetailsTableCtrl = hotRegisterer.getInstance('shelfDetailsTable');
            return vm.shelfDetailsTableCtrl;
        }


        vm.putInShelfBoxes = {};
        vm.putInShelf = _putInShelf;
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
                    var box = null; //_.filter(vm.frozenBoxes, {frozenBoxCode: i})[0];
                    for(var j = 0; j<vm.frozenBoxes.length; ++j){
                        if (i == vm.frozenBoxes[j].frozenBoxCode){
                            box = vm.frozenBoxes[j];
                            break;
                        }
                    }
                    if (!box){
                        continue;
                    }

                    for (; cellCol < countOfCols && !box.isPutInShelf; ++cellCol){
                        for(; cellRow < countOfRows && !box.isPutInShelf; ++cellRow){
                            var cellData = tableCtrl.getDataAtCell(cellRow, cellCol);
                            if (cellData && cellData.isEmpty){
                                box.isPutInShelf = true;
                                cellData.frozenBoxCode = box.frozenBoxCode;
                                cellData.isEmpty = false;

                                boxes[box.frozenBoxCode] = {
                                    frozenBoxId: box.frozenBoxId,
                                    frozenBoxCode: box.frozenBoxCode,
                                    rowsInShelf: cellData.rowsInShelf,
                                    columnsInShelf: cellData.columnsInShelf,
                                    rowNO: cellRow,
                                    colNO: cellCol
                                };

                                vm.selectedBox[i] = false;
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



    }
})();
