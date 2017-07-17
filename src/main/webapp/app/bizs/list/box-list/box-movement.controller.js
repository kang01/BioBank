/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxMovementController', BoxMovementController)
        .controller('ModalInstanceCtrl', ModalInstanceCtrl);

    BoxMovementController.$inject = ['$scope','hotRegisterer','$timeout','$compile','toastr','$state','$stateParams','$uibModal','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService',
        'SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable','SampleUserService','BoxInventoryService','BioBankBlockUi'];
    ModalInstanceCtrl.$inject = ['$uibModalInstance'];

    function BoxMovementController($scope,hotRegisterer,$timeout,$compile,toastr,$state,$stateParams,$uibModal,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,
                                   SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable,SampleUserService,BoxInventoryService,BioBankBlockUi) {
        var vm = this;
        vm.shelfInstance = {};
        vm.selectedInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.selectedBox = $stateParams.selectedBox || [];
        vm.movementFlag = false;
        vm.movement = {
            operatorId1: "",
            operatorId2: "",
            moveAffect: "",
            moveReason: "",
            whetherFreezingAndThawing: false,
            positionMoveRecordDTOS: []
        };
        var projectIds = [];
        vm.selected = {};
        vm.moveOperateFlag = false;
        function _init() {
            // 过滤已上架的冻存盒
            vm.filterNotPutInShelfTubes = function (box) {
                return !box.isPutInShelf && true;
            };
            setTimeout(function () {
                _initShelfDetailsTable();
            }, 200);

            _initSearch();
            if (vm.selectedBox.length) {
                _.forEach(vm.selectedBox, function (box) {
                    box.moveFrozenBoxPosition = '';
                    vm.selected[box.id] = false;
                });
            }
        }

        _init();

        vm.searchShelf = _fnSearchShelf;
        vm.putIn = _fnPutIn;
        vm.saveMovement = _fnSaveMovement;
        vm.search = _fnSearch;
        vm.empty = _fnEmpty;
        vm.close = _fnClose;
        vm.closeMovement = _fnCloseMovement;
        vm.moveOperate = _fnMoveOperate;

        function _fnClose() {
            if (vm.selectedBox.length) {
                var modalInstance = $uibModal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    $state.go("box-inventory");
                }, function () {
                });
            } else {
                $state.go("box-inventory");
            }

        }

        function _fnSearch() {
            if (projectIds.length) {
                for (var i = 0; i < projectIds.length; i++) {
                    var projectCode = _.find(vm.projectOptions, {id: +projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }
            vm.shelfInstance.rerender();
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
            vm.shelfInstance.rerender();
        }

        function _fnSaveMovement() {
            if (vm.movement.operatorId1 == vm.movement.operatorId2) {
                toastr.error("操作员不能重复！");
                return;
            }
            BioBankBlockUi.blockUiStart();
            vm.movement.positionMoveRecordDTOS = _.filter(vm.selectedBox, {isPutInShelf: true});
            BoxInventoryService.saveMovement(vm.movement).success(function (data) {
                vm.movement.id = data.id;
                toastr.success("保存成功!");
                BioBankBlockUi.blockUiStop();
            }).error(function (data) {
                toastr.error(data.message);
                BioBankBlockUi.blockUiStop();
            })
        }

        function _fnCloseMovement() {
            vm.movementFlag = false;
            vm.putInShelfBoxes = {};
            _initShelfDetailsTable();
            var hot = _getShelfDetailsTableCtrl();
            if (hot) {
                hot.loadData([["", "", "", ""]]);
            }
        }

        //移动冻存盒
        vm.selectAll = false;
        vm.toggleAll = toggleAll;
        vm.toggleOne = toggleOne;
        function toggleAll(selectAll, selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
        }

        function toggleOne(selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if (!selectedItems[id]) {
                        vm.selectAll = false;
                        return;
                    }
                }
            }
            vm.selectAll = true;
        }

        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "30"),
            DTColumnBuilder.newColumn(1).withOption("width", "100"),
            DTColumnBuilder.newColumn(2).withOption("width", "60"),
            DTColumnBuilder.newColumn(3).withOption("width", "80"),
            DTColumnBuilder.newColumn(4).withOption("width", "80")

        ];

        //目标冻存架
        function _initSearch() {
            //批注人
            SampleUserService.query({}, onUserSuccess, onError);
            function onUserSuccess(data) {
                vm.userOptions = data;
            }

            vm.userConfig = {
                valueField: 'id',
                labelField: 'userName',
                maxItems: 1
            };
            //剩余
            vm.dto.spaceType = "2";
            //大于
            vm.dto.compareType = "1";
            vm.dto.number = 0;
            //获取项目
            ProjectService.query({}, onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备
            EquipmentService.query({}, onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            //设备类型
            vm.equipmentOptions = [
                {value: "1", label: "冰箱"},
                {value: "2", label: "液氮罐"}
            ];
            //盒子位置
            vm.frozenBoxPlaceConfig = {
                valueField: 'id',
                labelField: 'equipmentCode',
                maxItems: 1,
                onChange: function (value) {
                    if (value) {
                        AreasByEquipmentIdService.query({id: value}, onAreaSuccess, onError);
                    } else {
                        vm.dto.areaId = "";
                        vm.frozenBoxAreaOptions = [
                            {id: "", areaCode: ""}
                        ];
                        vm.dto.shelvesId = "";
                        vm.frozenBoxShelfOptions = [
                            {id: "", supportRackCode: ""}
                        ];
                        $scope.$apply();
                    }
                }
            };
            function onAreaSuccess(data) {
                vm.frozenBoxAreaOptions = data;
                if (vm.frozenBoxAreaOptions.length) {
                    vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                    SupportacksByAreaIdService.query({id: vm.dto.areaId}, onShelfSuccess, onError);
                }

            }

            vm.frozenBoxAreaConfig = {
                valueField: 'id',
                labelField: 'areaCode',
                maxItems: 1,
                onChange: function (value) {
                    if (value) {
                        SupportacksByAreaIdService.query({id: value}, onShelfSuccess, onError);
                    } else {
                        vm.dto.shelvesId = "";
                        vm.frozenBoxShelfOptions = [
                            {id: "", supportRackCode: ""}
                        ];
                        $scope.$apply();
                    }
                }
            };
            //架子
            function onShelfSuccess(data) {
                vm.frozenBoxShelfOptions = data;
                vm.dto.shelvesId = vm.frozenBoxShelfOptions[0].id;
            }

            vm.frozenBoxShelfConfig = {
                valueField: 'id',
                labelField: 'supportRackCode',
                maxItems: 1,
                onChange: function (value) {
                }
            };
            //项目编码
            vm.projectConfig = {
                valueField: 'id',
                labelField: 'projectName',
                searchField: 'projectName',
                onChange: function (value) {
                    vm.projectIds = _.join(value, ',');
                    projectIds = value;
                    vm.dto.projectCodeStr = [];
                }
            };
            //设备类型
            vm.equipmentConfig = {
                valueField: 'value',
                labelField: 'label',
                maxItems: 1,
                onChange: function (value) {
                }
            };
        }

        vm.shelfOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false)
            .withOption('serverSide', true)
            .withFnServerData(function (sSource, aoData, fnCallback, oSettings) {
                var data = {};
                for (var i = 0; aoData && i < aoData.length; ++i) {
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                var searchForm = angular.toJson(vm.dto);
                BoxInventoryService.queryShelfList(data, searchForm).then(function (res) {
                    var json = res.data;
                    vm.equipmentData = res.data.data;
                    var error = json.error || json.sError;
                    if (error) {
                        jqDt._fnLog(oSettings, 0, error);
                    }
                    oSettings.json = json;
                    fnCallback(json);
                }).catch(function (res) {
                    console.log(res);
                    var ret = jqDt._fnCallbackFire(oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR]);

                    if ($.inArray(true, ret) === -1) {
                        if (error == "parsererror") {
                            jqDt._fnLog(oSettings, 0, 'Invalid JSON response', 1);
                        }
                        else if (res.readyState === 4) {
                            jqDt._fnLog(oSettings, 0, 'Ajax error', 7);
                        }
                    }

                    jqDt._fnProcessingDisplay(oSettings, false);
                });
            })
            .withOption('createdRow', createdRow);
        vm.shelfColumns = [
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型').withOption("width", "80"),
            DTColumnBuilder.newColumn('position').withTitle('冻存架位置').withOption("width", "120").renderWith(_fnRowPositionRender),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用').withOption("width", "50"),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余').withOption("width", "50"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "60")
                .withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '0001': status = '运行中';break;
            }
            var countOfUsed = data.countOfUsed;
            var countOfRest = data.countOfRest;
            var total = countOfUsed+countOfRest;
            var progressStyle = "width:"+countOfUsed/total*100+"%";
            var progress = ""+countOfUsed + "/" + total;
            var html;
            html = "<div class='pos-progress'> " +
                "<div class='text'>"+progress+"</div>" +
                "<div class='Bar' style ='"+progressStyle+"'> " +

                " </div> " +
                "</div>";
            $('td:eq(4)', row).html(status);
            $('td:eq(2)', row).html(html);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            var position = "'" + full.position + "'";
            return '<button type="button" class="btn btn-xs" ng-click="vm.moveOperate('+position+')">' +
                '   移入' +
                '</button>&nbsp;';
        }
        function _fnRowPositionRender(data, type, full, meta) {
            var position = "'" + full.position + "'";
            var html = '';
            html = '<a ng-click="vm.searchShelf(' + position + ')">' + full.position + '</a>';
            return html;
        }

        function _fnSearchShelf(position) {
            vm.moveOperateFlag = false;
            vm.movementFlag = true;
            _queryShelfDesc(position);
        }
        function _queryShelfDesc(position) {
            BoxInventoryService.queryShelfDesc(position).success(function (data) {
                vm.shelf = data;
                var boxList = _.filter(vm.selectedBox,{'moveFrozenBoxPosition':position});
                for(var i = 0; i< boxList.length; i++){
                    vm.shelf.frozenBoxDTOList.push(boxList[i]);
                }
                _fnLoadHandSonTable(vm.shelf);
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
        function _fnMoveOperate(position) {
            vm.moveOperateFlag = true;
            _queryShelfDesc(position);
        }
        //移入
        function _fnPutIn() {
            var countOfCols = vm.shelf.supportRackColumns;
            var countOfRows = vm.shelf.supportRackRows;
            var tableCtrl = _getShelfDetailsTableCtrl();
            var startEmptyPos = tableCtrl.getSelected();
            var cellRow = startEmptyPos[0];
            var cellCol = startEmptyPos[1];
            for (var i in vm.selected) {
                if (vm.selected[i]) {
                    vm.selectAll = false;
                    var box = _.find(vm.selectedBox, {id: +i});
                    if (!box) {
                        continue;
                    }
                    // 从选中的位置，开始上架，先行后列
                    for (; cellCol < countOfCols && !box.isPutInShelf; ++cellCol) {
                        for (; cellRow < countOfRows && !box.isPutInShelf; ++cellRow) {
                            var cellData = tableCtrl.getDataAtCell(cellRow, cellCol);
                            if (cellData && cellData.isEmpty) {
                                // 单元格为空可以上架
                                // 标记盒子已经上架
                                vm.selected[i] = false;
                                box.isPutInShelf = true;
                                box.rowsInShelf = cellData.rowsInShelf;
                                box.columnsInShelf = cellData.columnsInShelf;
                                box.supportRackId = cellData.supportRackId;
                                //+"."+cellData.columnsInShelf+cellData.rowsInShelf
                                box.moveFrozenBoxPosition = vm.shelf.position;
                                box.frozenBoxId = box.id;

                                cellData.frozenBoxCode = box.frozenBoxCode;
                                cellData.isEmpty = false;
                                break;
                            }
                        }
                        if (box.isPutInShelf) {
                            break;
                        }
                        cellRow = 0;
                    }
                }
            }
            tableCtrl.render();
            vm.selectedInstance.rerender();
        }
        // 需要保存的盒子上架信息
        vm.putInShelfBoxes = {};
        function _fnLoadHandSonTable(shelf) {
            var boxes = shelf.frozenBoxDTOList;
            // 架子上的行数
            var countOfRows = shelf.supportRackRows || 4;
            // 架子上的列数
            var countOfCols = shelf.supportRackColumns || 4;
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
            for (var i = 0; i < countOfCols; ++i) {
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
            for (var i = 0; i < countOfCols; ++i) {
                // 再行
                for (var j = 0; j < countOfRows; ++j) {
                    arrayBoxes[j] = arrayBoxes[j] || [];
                    var pos = {columnsInShelf: String.fromCharCode(charCode + i), rowsInShelf: j + 1 + ""};
                    // 从已入库的盒子中查询架子中该位置的盒子
                    var boxesInShelf = _.filter(boxes, pos);
                    if (boxesInShelf.length) {
                        arrayBoxes[j][i] = boxesInShelf[0];
                    } else {
                        var boxesPos = vm.putInShelfBoxes[shelf.id];
                        // 从已上架的盒子中查询架子中该位置的盒子
                        boxesInShelf = _.filter(boxesPos || [], pos);
                        if (boxesInShelf.length) {
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
                            if (!emptyPos) {
                                emptyPos = {row: j, col: i};
                            }
                        }
                    }

                    // 该位置的位置信息
                    arrayBoxes[j][i].supportRackCode = shelf.supportRackCode;
                    arrayBoxes[j][i].areaCode = shelf.areaCode;
                    arrayBoxes[j][i].equipmentCode = shelf.equipmentCode;
                    arrayBoxes[j][i].supportRackId = shelf.id;
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
            if (emptyPos) {
                // tableCtrl.selectCell(0, 0);
                tableCtrl.selectCell(emptyPos.row, emptyPos.col);
            }
            if(vm.moveOperateFlag){
                _fnPutIn();
            }
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
                if (cellData && cellData.frozenBoxCode) {
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

        // 获取上架位置列表的控制实体
        function _getShelfDetailsTableCtrl() {
            vm.shelfDetailsTableCtrl = hotRegisterer.getInstance('my-handsontable');
            return vm.shelfDetailsTableCtrl;
        }

        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
    function ModalInstanceCtrl($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();

