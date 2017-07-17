/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentMovementController', EquipmentMovementController);

    EquipmentMovementController.$inject = ['$scope','$compile','$state','$uibModal','$stateParams','hotRegisterer','toastr','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable','SampleUserService','RequirementService','BioBankBlockUi'];

    function EquipmentMovementController($scope,$compile,$state,$uibModal,$stateParams,hotRegisterer,toastr,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable,SampleUserService,RequirementService,BioBankBlockUi) {
        var vm = this;
        vm.dtInstance = {};
        vm.selectedInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.selectedEquipment = $stateParams.selectedEquipment||[];
        vm.movement = {
            operatorId1:"",
            operatorId2:"",
            moveAffect:"",
            moveReason:"",
            whetherFreezingAndThawing:false,
            positionMoveRecordDTOS:[]
        };
        vm.selected = {};
        //列表中移入
        vm.moveOperateFlag = false;
        var projectIds = [];
        function _init() {
            // 过滤已上架的冻存盒
            vm.filterNotPutInShelfTubes = function (equipment) {
                return !equipment.isPutInShelf && true;
            };
            _initSearch();
            _initHandsonTablePanel();
            if(vm.selectedEquipment.length){
                _.forEach(vm.selectedEquipment, function(equipment){
                    equipment.moveShelfPosition = "";
                    vm.selected[equipment.id] = false;
                });
            }
        }
        _init();
        //移入操作
        vm.searchArea = _fnSearchArea;
        vm.searchEquipment = _fnSearchEquipment;
        vm.putIn = _fnPutIn;
        vm.saveMovement = _fnSaveMovement;
        vm.search = _fnSearch;
        vm.empty = _fnEmpty;
        vm.close = _fnClose;
        function _fnSearch() {
            if(projectIds.length){
                for(var i = 0; i <projectIds.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }
            vm.dtInstance.rerender();
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
            vm.dtInstance.rerender();
        }
        function _fnSaveMovement() {
            if(vm.movement.operatorId1 == vm.movement.operatorId2){
                toastr.error("操作员不能重复！");
                return;
            }
            vm.movement.positionMoveRecordDTOS = _.filter(vm.selectedEquipment, {isPutInShelf: true});
            BioBankBlockUi.blockUiStart();
            EquipmentInventoryService.saveMovement(vm.movement).success(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.success("保存成功!");
            }).error(function (data) {
                toastr.error(data.message);
                BioBankBlockUi.blockUiStop();
            })
        }
        function _fnClose() {
            if(vm.selectedEquipment.length){
                var modalInstance = $uibModal.open({
                    templateUrl: 'myModalContent.html',
                    controller: 'ModalInstanceCtrl',
                    controllerAs: 'vm'
                });
                modalInstance.result.then(function () {
                    $state.go("equipment-inventory");
                }, function () {
                });
            }else{
                $state.go("equipment-inventory");
            }

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
            // vm.selectedEquipment = [];
            // for (var id in selectedItems) {
            //     if (selectedItems.hasOwnProperty(id)) {
            //         if(selectedItems[id]) {
            //             var obj = _.find(vm.equipmentData,{id:+id});
            //             vm.selectedEquipment.push(obj);
            //         }
            //     }
            // }
            // vm.selectedLen = vm.selectedEquipment.length;
            // vm.selectedOptions.withOption('data', vm.selectedEquipment);
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
        vm.selectedColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "30"),
            DTColumnBuilder.newColumn(1).withOption("width", "100"),
            DTColumnBuilder.newColumn(2).withOption("width", "100"),
            DTColumnBuilder.newColumn(3).withOption("width", "100")
        ];

        //目标冻存架
        function _initSearch() {
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
            //剩余
            vm.dto.spaceType = "2";
            //大于
            vm.dto.compareType = "1";
            vm.dto.number = 0;
            //获取项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备编码
            EquipmentService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            //设备编码
            vm.frozenBoxPlaceConfig = {
                valueField:'id',
                labelField:'equipmentCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                    }else{
                        vm.dto.areaId = "";
                        vm.frozenBoxAreaOptions = [
                            {id:"",areaCode:""}
                        ];
                        $scope.$apply();
                    }
                }
            };
            function onAreaSuccess(data) {
                vm.frozenBoxAreaOptions = data;
                if(vm.frozenBoxAreaOptions.length){
                    vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                }

            }
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                }
            };
            //设备类型
            vm.equipmentOptions = [
                {value:"1",label:"冰箱"},
                {value:"2",label:"液氮罐"}
            ];
            //项目编码
            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                searchField:'projectName',
                onChange:function(value){
                    vm.projectIds = _.join(value, ',');
                    projectIds = value;
                    vm.dto.projectCodeStr = [];
                }
            };
            //设备类型
            vm.equipmentConfig = {
                valueField:'value',
                labelField:'label',
                maxItems: 1,
                onChange:function(value){
                }
            };

        }
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false)
            .withOption('order', [[1,'asc']])
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                var searchForm = angular.toJson(vm.dto);
                EquipmentInventoryService.queryAreaList(data,searchForm).then(function (res){
                    var json = res.data;
                    vm.equipmentData = res.data.data;
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
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        vm.dtColumns = [
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型').withOption("width", "100"),
            DTColumnBuilder.newColumn('position').withTitle('区域位置').withOption("width", "100").renderWith(_fnRowPositionRender),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用').withOption("width", "50"),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余').withOption("width", "60"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "60"),
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
        function _fnRowPositionRender(data, type, full, meta) {
            var html = '';
            html = '<a ng-click="vm.searchEquipment('+full.equipmentId+','+full.id+')">'+full.position+'</a>';
            return html;
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-xs" ng-click="vm.searchArea('+full.equipmentId+','+full.id+')">' +
                '   移入' +
            '</button>&nbsp;';
        }
        function _fnSearchEquipment(equipmentId,areaId) {
            vm.movementFlag = true;
            _queryAreaById(equipmentId,areaId);
        }
        function _fnSearchArea(equipmentId,areaId) {
            vm.moveOperateFlag = true;
            _queryAreaById(equipmentId,areaId);
        }
        function _queryAreaById(equipmentId,areaId) {
            EquipmentInventoryService.queryRack(equipmentId,areaId).success(function (data) {
                vm.rack = data;
                var equipmentList = _.filter(vm.selectedEquipment,{'moveShelfPosition':vm.rack.position});
                for(var i = 0; i < equipmentList.length; i++){
                    for(var j = 0; j < vm.rack.supportRackDTOS.length;j++){
                        var position1 = equipmentList[i].moveShelfPosition+"."+equipmentList[i].supportRackCode;
                        var position2 = vm.rack.position+"."+vm.rack.supportRackDTOS[j].supportRackCode;
                        if(position1 == position2){
                            vm.rack.supportRackDTOS[j].flag = 1;
                        }
                    }
                }
                _fnLoadRack(vm.rack);
            }).error(function (data) {
                toastr.error(data.message);
            })
        }
        // 需要保存的架子上架信息
        vm.putInShelfAreas = {};
        function _fnLoadRack(rack){
            var racks = rack.supportRackDTOS;
            // 架子上的行数
            var countOfRows = 1;
            // 架子上的列数
            var countOfCols = racks.length || 13;
            // // 架子定位列表的控制对象
            var tableCtrl = _getTableCtrl();
            // 创建架子定位列表的定位数据
            var arrayRack = [];
            var emptyPos = null;
            for(var i = 0; i < countOfRows; i++){
                arrayRack[i] = [];
                for(var j = 0; j < countOfCols;j++){
                    arrayRack[i][j] = racks[j];
                    if(!racks[j].flag){
                        if (!emptyPos) {
                            emptyPos = {row: i, col: j};
                        }
                    }
                }
            }
            // 更新架子定位列表并选中第一个空位置
            tableCtrl.loadData(arrayRack);
            if (emptyPos) {
                tableCtrl.selectCell(emptyPos.row, emptyPos.col);
            }
            if(vm.moveOperateFlag){
                _fnPutIn();
            }
        }
        //移入
        function _fnPutIn() {
            var countOfCols = vm.rack.supportRackDTOS.length;
            var countOfRows = 1;
            var tableCtrl = _getTableCtrl();
            var startEmptyPos = tableCtrl.getSelected();
            var cellRow = startEmptyPos[0];
            var cellCol = startEmptyPos[1];

            for (var i in vm.selected) {
                if (vm.selected[i]) {
                    vm.selectAll = false;
                    var equipment = _.find(vm.selectedEquipment, {id: +i});
                    if (!equipment) {
                        continue;
                    }
                    // 从选中的位置，开始上架，先行后列
                    for (; cellCol < countOfCols && !equipment.isPutInShelf; ++cellCol) {
                        for (; cellRow < countOfRows && !equipment.isPutInShelf; ++cellRow) {
                            var cellData = tableCtrl.getDataAtCell(cellRow, cellCol);
                            if (cellData && !cellData.flag) {
                                // 单元格为空可以上架
                                // 标记区域已经上架
                                vm.selected[i] = false;
                                equipment.isPutInShelf = true;
                                equipment.supportRackId = cellData.id;
                                equipment.areaId = cellData.areaId;
                                equipment.moveShelfPosition = vm.rack.position;
                                equipment.supportRackOldId = equipment.id;
                                equipment.supportRackCode = cellData.supportRackCode;
                                cellData.flag = 1;
                                break;
                            }
                        }
                        if (equipment.isPutInShelf) {
                            break;
                        }
                    }
                }
            }
            tableCtrl.render();
            vm.selectedInstance.rerender();
        }
        function _initHandsonTablePanel(){
            vm.handsonTableArray = [];//初始管子数据二位数组

            initHandsonTable(1,13);

            vm.settings = {
                rowHeaders : ['R'],
                outsideClickDeselectsCache: false,
                outsideClickDeselects: false,
                data:vm.handsonTableArray,
                minRows: 1,
                minCols: 1,
                fillHandle:false,
                stretchH: 'all',
                autoWrapCol:true,
                wordWrap:true,
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
                var hot = _getTableCtrl();
                var cellData = value;
                if (hot){
                    cellData = hot.getDataAtCell(row, col);
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
                    $div.html(value.supportRackCode);
                        // .data("frozenBoxId", value.frozenBoxId)
                        // .data("frozenBoxCode", value.frozenBoxCode)
                        // .data("columnsInShelf", value.columnsInShelf)
                        // .data("rowsInShelf", value.rowsInShelf)
                        // .data("countOfSample", value.countOfSample);
                }

                $(td).html("");
                $(td).append($div);

            }
            // 修改单元格属性
            function _changeCellProperties(row, col, prop) {
                var hot = _getTableCtrl();
                if (!hot){
                    return;
                }
                var cellProperties = {};
                var cellData = hot.getDataAtCell(row, col);
                cellProperties.flag = 0;
                if (cellData && cellData.flag) {
                    // 单元格有数据，并且有冻存盒ID，表示该单元格在库里有位置
                    // 该单元格不能被使用
                    cellProperties.flag = 1;
                    // 该单元格不能编辑
                    cellProperties.editor = false;
                    // 该单元格只读
                    cellProperties.readOnly = true;
                    // 该单元格的样式
                    cellProperties.className = 'a00';
                    // cellProperties.readOnlyCellClassName = 'htDimmed';
                } else {
                    cellProperties.flag = cellData && cellData.flag;
                }

                return cellProperties;
            }


            function initHandsonTable(row,col) {
                for(var i = 0; i < row; i++){
                    vm.handsonTableArray[i] = [];
                    for(var j = 0;j < col; j++){
                        vm.handsonTableArray[i][j] = "";
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
        // 获取上架位置列表的控制实体
        function _getTableCtrl(){
            vm.shelfDetailsTableCtrl = hotRegisterer.getInstance('shelfDetailsTable');
            return vm.shelfDetailsTableCtrl;
        }
        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
})();
