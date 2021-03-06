/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentMovementController', EquipmentMovementController);

    EquipmentMovementController.$inject = ['$scope','$compile','$state','$uibModal','$stateParams','hotRegisterer','toastr','DTColumnBuilder','ProjectService','EquipmentAllService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable','SampleUserService','Principal','BioBankBlockUi'];

    function EquipmentMovementController($scope,$compile,$state,$uibModal,$stateParams,hotRegisterer,toastr,DTColumnBuilder,ProjectService,EquipmentAllService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable,SampleUserService,Principal,BioBankBlockUi) {
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
        vm.closeFlag = false;
        //单次移入的数据
        var singleRecoverDataArray = [];
        //全部移入的数据
        var totalRecoverDataArray = [];

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
                    equipment.isPutInShelf = false;
                    equipment.supportRackOldId = equipment.id;
                    vm.selected[equipment.id] = false;
                });
            }
            Principal.identity().then(function(account) {
                vm.account = account;
                vm.movement.operatorId1 = vm.account.id;
            });

            _queryTargetEquipment();
        }
        _init();

        function _queryTargetEquipment() {
            var data = {
                draw:1,
                length:-1
            };
            var searchForm = angular.toJson(vm.dto);
            EquipmentInventoryService.queryAreaList(data,searchForm).success(function (res){
                vm.equipmentData = res.data;
                vm.dtOptions.withOption("data",vm.equipmentData);
            })
        }
        vm.searchArea = _fnSearchArea;
        vm.searchEquipment = _fnSearchEquipment;
        //撤销
        vm.recover = _fnRecover;
        //移入操作
        vm.putIn = _fnPutIn;
        //保存移位
        vm.saveMovement = _fnSaveMovement;
        //搜索架子
        vm.search = _fnSearch;
        //清空
        vm.empty = _fnEmpty;
        vm.close = _fnClose;
        vm.closeMovement = _fnCloseMovement;

        function _fnSearch() {
            if(projectIds.length){
                for(var i = 0; i <projectIds.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }
            _queryTargetEquipment();
            // vm.dtInstance.rerender();
        }
        function _fnEmpty() {
            vm.dto = {};
            vm.dto.projectCodeStr = [];
            projectIds = [];
            vm.projectCodeStr = [];
            vm.arrayBoxCode = [];
            //1:已用 2：剩余
            vm.dto.spaceType = "2";
            //1:大于 2:大于等于 3:等于 4:小于
            vm.dto.compareType = "2";
            vm.dto.number = 0;
            _queryTargetEquipment();
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
                vm.dtInstance.rerender();
                var selectedFinish =  _.filter(vm.selectedEquipment, {isPutInShelf: true});
                for(var i = 0; i < selectedFinish.length;i++){
                    selectedFinish[i].saveFinishFlag = true;
                }
                var len = _.filter(selectedFinish, {saveFinishFlag: true}).length;
                if(len == vm.selectedEquipment.length){
                    vm.closeFlag = true;
                }else{
                    vm.closeFlag = false;
                }
            }).error(function (data) {
                toastr.error(data.message);
                BioBankBlockUi.blockUiStop();
            })
        }
        function _fnClose() {
            if(!vm.closeFlag){
                var modalInstance = $uibModal.open({
                    backdrop:"static",
                    size:"sm",
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
        function _fnCloseMovement() {
            vm.movementFlag = false;
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
        vm.selectedOptions = BioBankDataTable.buildDTOption("NORMALLY", 436, 10)
            .withOption('order', [[1,'asc']])
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('searching', false);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn(0).withOption("width", "30").withOption('searchable',false).notSortable(),
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
            //1:已用 2：剩余
            vm.dto.spaceType = "2";
            //1:大于 2:大于等于 3:等于 4:小于
            vm.dto.compareType = "2";

            vm.dto.number = 0;
            //获取项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备编码
            EquipmentAllService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);
                // vm.frozenBoxPlaceOptions = data;
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
                vm.frozenBoxAreaOptions.push({id:"",areaCode:""});
                vm.dto.areaId = "";
                // if(vm.frozenBoxAreaOptions.length){
                //     vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                // }

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
        vm.dtOptions = BioBankDataTable.buildDTOption("ORDINARY", null, 10)
            .withOption('searching', false)
            .withOption('order', [[1,'asc']])
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
            // .withOption('serverSide',false)
            // .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
            //     var data = {};
            //     for(var i=0; aoData && i<aoData.length; ++i){
            //         var oData = aoData[i];
            //         data[oData.name] = oData.value;
            //     }
            //     var jqDt = this;
            //     var searchForm = angular.toJson(vm.dto);
            //     EquipmentInventoryService.queryAreaList(data,searchForm).then(function (res){
            //         var json = res.data;
            //         vm.equipmentData = res.data.data;
            //         var error = json.error || json.sError;
            //         if ( error ) {
            //             jqDt._fnLog( oSettings, 0, error );
            //         }
            //         oSettings.json = json;
            //         fnCallback( json );
            //     }).catch(function(res){
            //         console.log(res);
            //         var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );
            //
            //         if ( $.inArray( true, ret ) === -1 ) {
            //             if ( error == "parsererror" ) {
            //                 jqDt._fnLog( oSettings, 0, 'Invalid JSON response', 1 );
            //             }
            //             else if ( res.readyState === 4 ) {
            //                 jqDt._fnLog( oSettings, 0, 'Ajax error', 7 );
            //             }
            //         }
            //
            //         jqDt._fnProcessingDisplay( oSettings, false );
            //     });
            // })

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
            var equipmentList = _.filter(vm.selectedEquipment,{'moveShelfPosition':data.position});
            var status = '';
            switch (data.status){
                case '0001': status = '运行中';break;
            }
            var countOfUsed = data.countOfUsed + equipmentList.length;
            var countOfRest = data.countOfRest - equipmentList.length;
            var total = data.countOfUsed + data.countOfRest;
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
            $('td:eq(3)', row).html(countOfRest);
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
            //是否列表移入
            vm.moveOperateFlag = false;
            vm.movementFlag = true;
            _queryAreaById(equipmentId,areaId);
        }
        //moveOperateFlag true列表移入操作
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
                            vm.rack.supportRackDTOS[j].flag = 2;
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
            var supportRack =  _.chunk(racks, 10);
            // 架子上的行数
            var countOfRows = supportRack.length;
            // 架子上的列数
            var countOfCols = 10;
            //vm.moveOperateFlag为true的时候是列表移入
            if(vm.moveOperateFlag){
                // 创建架子定位列表的定位数据
                var arrayRack = [];
                var emptyPos = null;
                for(var i = 0; i < countOfRows; i++){
                    arrayRack[i] = [];
                    for(var j = 0,len = supportRack[i].length; j < len;j++){
                        arrayRack[i][j] = supportRack[i][j];
                        if(!supportRack[i][j].flag){
                            if (!emptyPos) {
                                emptyPos = {row: i, col: j};
                            }
                        }
                    }
                }
                vm.handsonTableArray = arrayRack;
                if(emptyPos){
                    _fnPutIn(emptyPos);
                }else{
                    toastr.error("无可移入的空位置!");
                }
            }else{
                // 架子定位列表的控制对象
                var tableCtrl = _getTableCtrl();
                // 创建架子定位列表的定位数据
                var arrayRack = [];
                var emptyPos = null;
                for(var i = 0; i < countOfRows; i++){
                    arrayRack[i] = [];
                    for(var j = 0,len = supportRack[i].length; j < len;j++){
                        arrayRack[i][j] = supportRack[i][j];
                        if(!supportRack[i][j].flag){
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
            }
        }
        //撤销
        function _fnRecover() {
            if(totalRecoverDataArray.length){
                vm.selectAll = false;
                var recoverData = totalRecoverDataArray.pop();
                for(var i = 0; i < vm.selectedEquipment.length; i++){
                    for(var id in recoverData){
                        if(vm.selectedEquipment[i].id == +id){
                            vm.selectedEquipment[i] = recoverData[id];
                        }
                    }
                }
                // console.log(JSON.stringify(vm.selectedEquipment));
                // vm.selectedInstance.rerender();
                vm.dtInstance.rerender();
                _queryAreaById(vm.rack.equipmentId,vm.rack.id);
            }else{
                toastr.error("无内容撤消！")
            }

        }
        //移入
        function _fnPutIn(emptyPos) {
            var racks = vm.rack.supportRackDTOS;
            var supportRack =  _.chunk(racks, 10);
            singleRecoverDataArray = [];
            var cellRow;
            var cellCol;
            // var countOfCols = supportRack.length;
            var countOfRows = supportRack.length;
            //为true时，列表移入
            if(vm.moveOperateFlag){
                cellRow = emptyPos.row;
                cellCol = emptyPos.col;
                _fnPutInOperate();
                vm.dtInstance.rerender();
            }else{
                var tableCtrl = _getTableCtrl();
                var startEmptyPos = tableCtrl.getSelected();
                if(startEmptyPos){
                    cellRow = startEmptyPos[0];
                    cellCol = startEmptyPos[1];
                    _fnPutInOperate();
                    tableCtrl.render();
                }else{
                    toastr.error("无可移入的空位置!");
                }

            }
            function _fnPutInOperate() {
                for (var i in vm.selected) {
                    if (vm.selected[i]) {
                        vm.selectAll = false;
                        var equipment = _.find(vm.selectedEquipment, {id: +i});
                        if (!equipment) {
                            continue;
                        }
                        // 从选中的位置，开始上架，先列后行
                        for (; cellRow < countOfRows && !equipment.isPutInShelf; ++cellRow) {
                            for (cellCol = 0; cellCol < supportRack[cellRow].length && !equipment.isPutInShelf; ++cellCol) {
                                if(vm.moveOperateFlag){
                                    var cellData = vm.handsonTableArray[cellRow][cellCol];
                                }else{
                                    var cellData = tableCtrl.getDataAtCell(cellRow, cellCol);
                                }
                                if (cellData && !cellData.flag) {
                                    var supportRackId = equipment.supportRackId;
                                    var areaId = equipment.areaId;
                                    var supportRackCode = equipment.supportRackCode;
                                    // 单元格为空可以上架
                                    // 标记区域已经上架
                                    vm.selected[i] = false;
                                    equipment.isPutInShelf = true;
                                    equipment.supportRackId = cellData.id;
                                    equipment.areaId = cellData.areaId;
                                    equipment.moveShelfPosition = vm.rack.position;
                                    equipment.supportRackOldId = equipment.id;
                                    equipment.supportRackCode = cellData.supportRackCode;
                                    cellData.flag = 2;

                                    //撤销操作
                                    var obj = angular.copy(equipment);
                                    obj.supportRackId  = supportRackId;
                                    obj.areaId = areaId;
                                    obj.supportRackCode = supportRackCode;
                                    obj.moveShelfPosition = "";
                                    obj.isPutInShelf = false;
                                    //单次移入的数据
                                    if(singleRecoverDataArray.length){
                                        var len = _.filter(singleRecoverDataArray,{id:+obj.id}).length;
                                        if(!len){
                                            singleRecoverDataArray.push(obj);
                                        }
                                    }else{
                                        singleRecoverDataArray.push(obj);
                                    }

                                    break;
                                }
                            }
                            if (equipment.isPutInShelf) {
                                break;
                            }
                        }
                    }
                }
                var oRecoverData = _.keyBy(singleRecoverDataArray,'id');
                if(totalRecoverDataArray.length){
                    for(var i = 0; i < totalRecoverDataArray.length; i++){
                        for(var id in oRecoverData){
                            if(!totalRecoverDataArray[i].hasOwnProperty(id)){
                                totalRecoverDataArray.push(oRecoverData);
                                break;
                            }
                        }
                        break;
                    }

                }else{
                    totalRecoverDataArray.push(oRecoverData);
                }
            }
            // vm.selectedInstance.rerender();
        }
        function _initHandsonTablePanel(){
            vm.handsonTableArray = [];//初始管子数据二位数组

            initHandsonTable(1,13);

            vm.settings = {
                rowHeaders : ['R','R','R','R'],
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
                if(cellProperties.flag == 2){
                    $(td).addClass('preassemble');
                }else{
                    $(td).removeClass('preassemble');
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
                if (cellData && cellData.flag == 1) {
                    // 单元格有数据，并且有区域ID，表示该单元格在库里有位置
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
                if (cellData && cellData.flag == 2) {
                    cellProperties.flag = 2;
                    cellProperties.className = 'a00';
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
