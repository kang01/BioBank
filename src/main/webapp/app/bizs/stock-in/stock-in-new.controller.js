/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController)
        .controller('RescindPutAwayModalController', RescindPutAwayModalController);

    StockInNewController.$inject = ['$timeout','blockUI','$state','$stateParams', '$scope','$compile','toastr','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal',
        'entity','StockInService','StockInBoxService','StockInBoxByCodeService','SplitedBoxService','StockInSaveService',
        'SampleTypeService','SampleService','IncompleteBoxService','RescindPutAwayService'];
    RescindPutAwayModalController.$inject = ['$uibModalInstance'];
    function StockInNewController($timeout,blockUI,$state,$stateParams,$scope,$compile,toastr,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,
                                  entity,StockInService,StockInBoxService,StockInBoxByCodeService,SplitedBoxService,StockInSaveService,
                                  SampleTypeService,SampleService,IncompleteBoxService,RescindPutAwayService) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.entity = entity;
        vm.stockInCode = vm.entity.stockInCode;
        vm.entityBoxes = {};
        vm.splittingBox = null;
        vm.splittedBoxes = {};
        vm.dtInstance = {};
        var modalInstance;



        _initStockInBoxesTable();

        function _initStockInBoxesTable(){
            vm.selectedStockInBoxes = {};
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
            vm.toggleOne = function (selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAll = false;
                            return;
                        }
                    }
                }
                vm.selectAll = true;
            };

            var ajaxUrl = 'api/temp/res/stock-in-boxes/stock-in/' + vm.entity.stockInCode;
            vm.dtInstanceCallback = function(instance){
                vm.dtInstance = instance;
            };

            vm.dtOptions = DTOptionsBuilder.fromSource({"url": ajaxUrl,"dataSrc": "data"})
            // 设置Table的DOM布局
                .withDOM("<'row'<'col-xs-6' TB> <'col-xs-6' f> r> t <'row'<'col-xs-6'i> <'col-xs-6'p>>")
                // 设置表格每页显示的行数
                .withDisplayLength(6)
                // 设置Tool button
                .withButtons([
                    {
                        text: '<i class="fa fa-sign-in"></i> 批量上架',
                        className: 'btn btn-default',
                        key: '1',
                        action: _fnActionPutInShelfButton
                    }
                ])
                // 执行Header内容的Compile
                .withOption('headerCallback', function(header) {
                    if (!vm.headerCompiled) {
                        // Use this headerCompiled field to only compile header once
                        vm.headerCompiled = true;
                        $compile(angular.element(header).contents())($scope);
                    }
                })
                // 将数据加载的请求方法从GET改为POST
                .withOption('sServerMethod','POST')
                // 显示正在处理的字样
                .withOption('processing',true)
                // 数据从服务器加载
                .withOption('serverSide',true)
                // 分页类型
                .withPaginationType('full_numbers')
                // 设置默认排序
                .withOption('order', [[1, 'asc' ]])
                // 指定数据加载方法
                .withFnServerData(_fnServerData)
                // 每行的渲染
                .withOption('createdRow', _fnCreatedRow)
                // 定义每个列过滤选项
                .withColumnFilter(_createColumnFilters());

            // 表格中每个列的定义
            vm.dtColumns = _createColumns();

            // 分装按钮
            vm.splitIt = _splitABox;
            // 上架按钮
            vm.putInShelf = _putInShelf;
            //撤销上架
            vm.rescindInShelf = _rescindInShelf;
        }

        function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            data["stockInCode"] = vm.stockInCode;
            var jqDt = this;
            StockInBoxService.getJqDataTableValues(data, oSettings).then(function (res){
                vm.selectAll = false;
                vm.selected = {};

                var json = res.data;
                var error = json.error || json.sError;
                if ( error ) {
                    jqDt._fnLog( oSettings, 0, error );
                }
                oSettings.json = json;
                for(var i = 0; json.data && i < json.data.length; ++i){
                    vm.entityBoxes[json.data[i].frozenBoxCode] = json.data[i];
                }
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
        }
        function _fnCreatedRow(row, data, dataIndex) {
            var status = '';
            var isSplit = data.isSplit || 0;
            // var sampleType = data.sampleType && data.sampleType.sampleTypeName || '';
            // 2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废
            switch (data.status){
                case '2001': status = '新建'; break;
                case '2002': isSplit ? status = '待分装' : status = '待入库'; break;
                case '2003': status = '已分装'; break;
                case '2004': status = '已入库'; break;
                case '2005': status = '已作废'; break;
                case '2006': status = '已上架'; break;
            }
            // $('td:eq(2)', row).html(sampleType);
            $('td:eq(6)', row).html(isSplit ? '需要分装' : '');
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            // console.log(vm.splitIt, vm.putInShelf);
            var buttonHtml = "";
            if (full.status == "2002"){
                if (full.isSplit){
                    buttonHtml += '<button type="button" class="btn btn-xs btn-warning" ng-click="vm.splitIt(\''+ full.frozenBoxCode +'\')">' +
                       '   <i class="fa fa-sitemap"></i> 分装' +
                        '</button>';
                } else {
                    buttonHtml += '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf(\''+ full.frozenBoxCode +'\')">' +
                        '   <i class="fa fa-sign-in"></i> 上架' +
                        '</button>';
                }
            }
            if(full.status == "2006"){
                buttonHtml += '<button type="button" class="btn btn-xs btn-error" ng-click="vm.rescindInShelf(\''+ full.frozenBoxCode +'\')">' +
                    '   <i class="fa fa-sitemap"></i> 撤销上架' +
                    '</button>';
            }

            return buttonHtml;
            // return '<button type="button" class="btn btn-xs btn-warning" ng-click="vm.splitIt(\''+ full.frozenBoxCode +'\')">' +
            //     '   <i class="fa fa-sitemap"></i> 分装' +
            //     '</button>&nbsp;' +
            //     '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf(\''+ full.frozenBoxCode +'\')">' +
            //     '   <i class="fa fa-sign-in"></i> 上架' +
            //     '</button>';
        }
        function _fnRowSelectorRender(data, type, full, meta) {
            // todo::已上架状态的盒子不应该再被选中
            vm.selected[full.frozenBoxCode] = false;
            var html = '';
            if (full.status == "2002" && !full.isSplit){
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.frozenBoxCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
        }
        function _fnActionPutInShelfButton(e, dt, node, config){
            _putInShelf();
        }
        // function _fnFrozenBoxCodeRender(data, type, full, meta) {
        //     return '<a ng-click="vm.splitIt('+ full.frozenBoxCode +')">'+full.frozenBoxCode+'</a>';
        // }

        function _createColumnFilters(){
            var filters = {
                aoColumns: [
                    null,
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {
                        type: 'select',
                        bRegex: true,
                        bSmart: true,
                        values: [
                            {value:0,label:"否"},
                            {value:1,label:"是"}
                        ]
                    },
                    {
                        type: 'select',
                        bRegex: true,
                        bSmart: true,
                        values: [
                            {value:'2001',label:"新建"},
                            {value:'2002',label:"待分装"},
                            {value:'2003',label:"已分装"},
                            {value:"2004",label:"已入库"},
                            {value:"2005",label:"已作废"},
                            {value:"2006",label:"已上架"}
                        ]
                    },
                    null
                ]
            };

            return filters;
        }
        function _createColumns(){
            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

            var columns = [
                // DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号'),
                DTColumnBuilder.newColumn('sampleTypeName').withOption("width", "80").withTitle('样本类型'),
                DTColumnBuilder.newColumn('sampleClassificationName').withOption("width", "80").withTitle('样本分类'),
                DTColumnBuilder.newColumn('position').withOption("width", "auto").withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withOption("width", "90").withTitle('样本量'),
                DTColumnBuilder.newColumn('isSplit').withOption("width", "100").withTitle('是否分装'),
                DTColumnBuilder.newColumn('status').withOption("width", "80").withTitle('状态'),
                DTColumnBuilder.newColumn("").withOption("width", "120").withTitle('操作').notSortable().renderWith(_fnActionButtonsRender),
                DTColumnBuilder.newColumn('id').notVisible(),
                // DTColumnBuilder.newColumn('sampleType').notVisible(),
                // DTColumnBuilder.newColumn('frozenBoxRows').notVisible(),
                // DTColumnBuilder.newColumn('frozenBoxColumns').notVisible()
            ];

            return columns;
        }

        function _splitABox(code){
            _fnQuerySampleType();
            _fnTubeByBoxCode(code);

        }
        function _putInShelf(boxIds){
            var boxes = [];
            var table = vm.dtInstance.DataTable;
            if (boxIds && typeof boxIds !== "object"){
                boxIds = [boxIds];
                table.data().each( function (d) {
                    if (boxIds == d.frozenBoxCode){
                        boxes.push(d);
                    }
                });
            } else {
                boxIds = [];
                for(var id in vm.selected){
                    if(!vm.selected[id]) {
                        continue;
                    }
                    boxIds.push(id);
                    table.data().each( function (d) {
                        if (id == d.frozenBoxCode){
                            boxes.push(d)
                        }
                    });
                }

            }

            if (typeof boxIds === "undefined" || !boxIds.length){
                return;
            }

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/box-putaway-modal.html',
                controller: 'BoxPutAwayModalController',
                controllerAs:'vm',
                // size:'lg',
                size:'90',
                resolve: {
                    items: function () {
                        return {
                            stockInCode: vm.stockInCode,
                            boxIds: boxIds,
                            boxes: boxes
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                vm.headerCompiled = false;
                vm.dtInstance.rerender();
            });
        }
        function _rescindInShelf(boxCode) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/rescind-putaway-modal.html',
                controller: 'RescindPutAwayModalController',
                controllerAs:'vm'
            });
            modalInstance.result.then(function (data) {
                RescindPutAwayService.rescindPutAway(vm.entity.stockInCode,boxCode).then(function (data) {
                    vm.headerCompiled = false;
                    vm.dtInstance.rerender();
                })
            });

        }



        var blockUiMessage = "处理中……";
        function _blockUiStart(message) {
            blockUI.start(message);
        }
        function _blockUiStop() {
            $timeout(function() {
                blockUI.stop();
            }, 1000);
        }
        //入库完成
        vm.saveStockIn = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/stock-in-info-modal.html',
                controller: 'StockInInfoModalController',
                controllerAs:'vm',
                resolve: {
                    items: function () {
                        return {
                            id: vm.entity.id,
                            stockInCode: vm.entity.stockInCode,
                            stockInDate: vm.entity.stockInDate,
                            storeKeeper1: vm.entity.storeKeeper1,
                            storeKeeper2: vm.entity.storeKeeper2
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                $state.go('stock-in');
            })
        };
        vm.isShowSplittingPanel = function(){
            return vm.splittingBox && true;
        };


        vm.frozenTubeArray = [];//初始管子的单元格
        vm.incompleteBoxesList = []; //分装后的样本类型盒子，未装满样本的盒子
        var tempTubeArray = [];//选中未满样本盒子的临时数据，需要操作管子
        var selectList = [];//选择单元格的管子数据
        var size = 10;
        var htm;
        //根据盒子编码取管子
        function _fnTubeByBoxCode(code) {
            StockInBoxByCodeService.get({code:code},onFrozenSuccess,onError);
            function onFrozenSuccess(data) {
                vm.box =  data;
                vm.splittingBox = true;
                //获取样本分类
                _fnQueryProjectSampleClass(vm.entity.projectId,vm.box.sampleType.id);
                var settings = {
                    minCols: +vm.box.frozenBoxType.frozenBoxTypeColumns,
                    minRows: +vm.box.frozenBoxType.frozenBoxTypeRows
                };
                var tubesInTable = [];
                var colHeaders = [];
                var rowHeaders = [];
                for(var i = 0; i < settings.minRows; i++){
                    var pos = {tubeRows: String.fromCharCode('A'.charCodeAt(0) + i), tubeColumns: 1 + ""};
                    if(i > 7){
                        pos.tubeRows = String.fromCharCode('A'.charCodeAt(0) + i+1)
                    }
                    rowHeaders.push(pos.tubeRows);
                    var tubes = [];
                    for(var j = 0; j < settings.minCols;j++){
                        pos.tubeColumns = j + 1 + "";
                        if (colHeaders.length < settings.minCols){
                            colHeaders.push(pos.tubeColumns);
                        }
                        var tubeInBox = _.filter(vm.box.frozenTubeDTOS, pos)[0];
                        var tube = _createTubeForTableCell(tubeInBox, vm.box, pos);
                        tubes.push(tube);
                    }
                    tubesInTable.push(tubes);
                }
                vm.frozenTubeArray = tubesInTable;
                settings.rowHeaders = rowHeaders;
                settings.colHeaders = colHeaders;
                setTimeout(function () {
                    hotRegisterer.getInstance('my-handsontable').updateSettings(settings);
                    hotRegisterer.getInstance('my-handsontable').loadData(tubesInTable);
                    hotRegisterer.getInstance('my-handsontable').render();
                },500);
                //取未满盒子
                _fnIncompleteBox();
            }

        }
        var customRenderer = function (hotInstance, td, row, col, prop, value, cellProperties) {
            if(value){
                if(value.memo && value.memo != " "){
                    cellProperties.comment = value.memo;
                }
                //样本类型
                if(value.sampleClassificationId){
                    SampleService.changeSampleType(value.sampleClassificationId,td,vm.projectSampleTypeOptions,1);
                }else{
                    if(vm.sampleTypeOptions){
                        SampleService.changeSampleType(value.sampleTypeId,td,vm.sampleTypeOptions,2);
                    }
                }
                //样本状态 status3001：正常，3002：空管，3003：空孔；3004：异常
                if(value.status){
                    changeSampleStatus(value.status,row,col,td,cellProperties)
                }
                htm = "<div ng-if='value.sampleCode' style='line-height: 20px'>"+value.sampleCode+"</div>"+
                    "<div ng-if='value.sampleTmpCode && !value.sampleCode' style='line-height: 20px'>"+value.sampleTempCode+"</div>"
            }else {
                htm = ""
            }
            td.style.position = 'relative';


            td.innerHTML = htm;
        };
        var operateColor;
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
                td.style.color = '#ffffff'
            }
            //异常
            if(sampleStatus == 3004){
                td.style.backgroundColor = 'red';
                td.style.border = '3px solid red;margin:-3px';
            }
        }
        vm.settings ={
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','J','K'],
            minRows: 10,
            minCols: 10,
            renderer:customRenderer,
            fillHandle:false,
            stretchH: 'all',
            wordWrap:true,
            colWidths: 90,
            editor: false,
            outsideClickDeselects:false,
            comments: true,
            onAfterSelectionEnd:function (row, col, row2, col2) {
                selectList = [];
                vm.selectCell = $(this.getData(row,col,row2,col2));
                for(var i = 0; i < vm.selectCell.length; i++ ){
                    for (var j = 0; j < vm.selectCell[i].length; j++){
                        if(vm.selectCell[i][j]){
                            selectList.push(vm.selectCell[i][j])
                        }
                    }
                }
            },
            enterMoves:function () {
                var hotMoves = hotRegisterer.getInstance('my-handsontable');
                var selectedCol = hotMoves.getSelected()[1];
                if(selectedCol + 1 < hotMoves.countCols()){
                    return{row:0,col:1}
                } else{
                    return{row:1,col:-selectedCol}
                }
            },
            cells: function (row, col, prop) {
                var cellProperties = {};

                // if (hot2.getDataAtRowProp(row, prop) === 'Nissan') {
                //     cellProperties.editor = false;
                // } else {
                //     cellProperties.editor = 'text';
                // }
                //
                // return cellProperties;
            }
        };
        // 创建一个对象用于管子Table的控件
        function _createTubeForTableCell(tubeInBox, box, pos){
            var tube = {
                id: null,
                sampleCode: "",
                sampleTempCode: "",
                sampleTypeId: box.sampleType.id,
                frozenBoxId: box.id,
                frozenBoxCode: box.frozenBoxCode,
                status: "",
                memo: "",
                tubeRows: pos.tubeRows,
                tubeColumns: pos.tubeColumns
            };
            if(box.sampleClassification){
                tube.sampleClassificationId = box.sampleClassification.sampleClassificationId
            }
            if (tubeInBox){
                tube.id = tubeInBox.id;
                tube.sampleCode = tubeInBox.sampleCode;
                tube.sampleTempCode = tubeInBox.sampleTempCode;
                tube.sampleTypeId = tubeInBox.sampleType.id;
                tube.status = tubeInBox.status;
                tube.memo = tubeInBox.memo;
                if(tubeInBox.sampleClassification){
                    tube.sampleClassificationId = tubeInBox.sampleClassification.sampleClassificationId;
                }
            }

            return tube;
        }
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['sampleTypeId'], ['esc']);
            });
        }
        //不同项目下的样本分类
        function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.projectSampleTypeOptions = data;
                if(sampleTypeId == 5){
                    for(var k = 0; k < data.length; k++){
                        for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                            for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                                if(data[k].columnsNumber == j+1){
                                    vm.frozenTubeArray[i][j].sampleClassificationId = data[k].sampleClassificationId
                                    vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                                }
                            }
                        }
                        for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                            if(vm.box.frozenTubeDTOS[m].tubeColumns == data[k].columnsNumber){
                                vm.box.frozenTubeDTOS[m].sampleClassification.id = data[k].sampleClassificationId
                            }
                        }
                    }

                    vm.box.sampleClassificationId = "";
                }else{
                    if(!vm.box.sampleClassification){
                        vm.box.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    }
                    for (var i = 0; i < vm.frozenTubeArray.length; i++) {
                        for (var j = 0; j < vm.frozenTubeArray[i].length; j++) {
                            vm.frozenTubeArray[i][j].sampleClassificationId = vm.box.sampleClassification.id;
                            vm.frozenTubeArray[i][j].sampleTypeId = sampleTypeId;
                        }
                    }
                    for(var m = 0; m < vm.box.frozenTubeDTOS.length; m++){
                        if(vm.box.sampleClassification){
                            vm.box.frozenTubeDTOS[m].sampleClassification.id = vm.box.sampleClassification.id
                        }
                    }
                }

                hotRegisterer.getInstance('my-handsontable').render()
            });
        }
        //取未装满的盒子
        function _fnIncompleteBox() {
            vm.incompleteBoxesList = [];
            IncompleteBoxService.query({frozenBoxCode:vm.box.frozenBoxCode,stockInCode:vm.entity.stockInCode},onIncompleteBoxesSuccess,onError)
        }

        function onIncompleteBoxesSuccess(data) {

            for(var i = 0; i < data.length; i++){
                var boxList = [];
                //盒子编码太长时，用星号代替
                    if(data[i].frozenBoxCode.length > 10){
                        data[i].copyBoxCode = _fnReplaceBoxCode(data[i].frozenBoxCode);
                    }else{
                        data[i].copyBoxCode = data[i].frozenBoxCode;
                    }
                data[i].addTubeCount = 0;
                if(data[i].sampleClassification){
                    data[i].backColor = data[i].sampleClassification.backColor;
                    data[i].sampleTypeName = data[i].sampleClassification.sampleClassificationName;
                    vm.sampleTypeClassId = data[i].sampleClassification.sampleClassificationId || data[i].sampleClassification.id;
                }else{
                    data[i].backColor = data[i].sampleType.backColor;
                    data[i].sampleTypeName = data[i].sampleType.sampleTypeName;
                }
                boxList.push(data[i]);
                if(data[i].sampleClassification){
                    vm.incompleteBoxesList.push({
                        sampleTypeId:data[i].sampleClassification.id || data[i].sampleClassification.sampleClassificationId,
                        boxList:boxList
                    })
                }else{
                    vm.incompleteBoxesList.push({
                        sampleTypeId:data[i].sampleTypeId,
                        boxList:boxList
                    })
                }

            }
            // if(data.length){
            //     if(data[0].sampleClassification){
            //         data[0].backColor = data[0].sampleClassification.backColor;
            //         data[0].sampleTypeName = data[0].sampleClassification.sampleClassificationName;
            //         vm.sampleTypeClassId = data[0].sampleClassification.sampleClassificationId || data[0].sampleClassification.id;
            //     }else{
            //         data[0].backColor = data[0].sampleType.backColor;
            //         data[0].sampleTypeName = data[0].sampleType.sampleTypeName;
            //     }
            //     data[0].frozenBoxTypeId = data[0].frozenBoxType.id;
            //     vm.frozenBoxType = data[0].frozenBoxType;
            //     data[0].sampleTypeId = data[0].sampleType.id;
            //     data[0].sampleClassificationId = data[0].sampleClassification.sampleClassificationId || data[0].sampleClassification.id;
            //     var boxList = [];
            //     boxList.push(data[0]);
            //     data[0].addTubeCount = 0;
            //     //盒子编码太长时，用星号代替
            //     if(data[0].frozenBoxCode.length > 10){
            //         data[0].copyBoxCode = _fnReplaceBoxCode(data[0].frozenBoxCode);
            //     }else{
            //         data[0].copyBoxCode = data[0].frozenBoxCode;
            //     }
            //     if(data[0].frozenBoxCode != vm.box.frozenBoxCode){
            //         if(data[0].sampleClassificationId){
            //             vm.incompleteBoxesList.push(
            //                 {
            //                     sampleTypeId:data[0].sampleClassificationId,
            //                     boxList:boxList
            //                 }
            //             );
            //         }else{
            //             vm.incompleteBoxesList.push(
            //                 {
            //                     sampleTypeId:data[0].sampleTypeId,
            //                     boxList:boxList
            //                 }
            //             );
            //         }
                    vm.incompleteBoxesList  = _.orderBy(vm.incompleteBoxesList, ['sampleTypeId'], ['esc']);
                    // console.log(JSON.stringify(vm.incompleteBoxesList))
            //     }
            // }
        }

        function onError(error) {
            toastr.error(error.data.message);
        }
        //盒子编码太长时，用星号代替
        function _fnReplaceBoxCode(code) {
            code = "***"+code.substring(code.length-10);
            return code;
        }
        //初始管子数
        function initFrozenTube(row,col) {
            for(var i = 0; i < +row; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < +col; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        function getTubeRowIndex(row) {
            if(row.charCodeAt(0) -65 > 7){
                return row.charCodeAt(0) -66;
            }else {
                return row.charCodeAt(0) -65;

            }
        }
        function getTubeColumnIndex(col) {
            return +col -1;
        }
        vm.boxList = [];
        vm.obox = {
            stockInFrozenTubeList:[]
        };
        var tubeList = [];
        //选中要分装样本盒
        vm.sampleBoxSelect = function (item,$event) {
            tubeList = [];
            vm.frozenBoxCode = item.frozenBoxCode;
            if(vm.frozenBoxCode ){
                tubeList = item.stockInFrozenTubeList
            }
            vm.obox = angular.copy(item);
            $($event.target).closest('ul').find('.box-selected').removeClass("box-selected");
            $($event.target).addClass("box-selected");
        };
        //分装操作
        vm.splitBox = function () {
            var rowCount = +vm.box.frozenBoxType.frozenBoxTypeRows;
            var colCount = +vm.box.frozenBoxType.frozenBoxTypeColumns;
            vm.obox.stockInFrozenTubeList = [];
            //初始100个管子或者80个管子
            for(var i = 0; i < rowCount; i++){
                tempTubeArray[i] = [];
                for(var j = 0;j < colCount; j++){
                    tempTubeArray[i][j] = {
                        tubeColumns: j+1,
                        tubeRows: String.fromCharCode(i+65),
                        frozenBoxCode:'',
                        selectTubeCode:vm.obox.frozenBoxCode
                    };
                    vm.obox.stockInFrozenTubeList.push(tempTubeArray[i][j])
                }
            }
            //把已有管子放进去
            for(var m =0 ; m < vm.obox.stockInFrozenTubeList.length; m++){
                for(var n = 0; n < tubeList.length;n++){
                    var tube = tubeList[n];
                    if(vm.obox.stockInFrozenTubeList[m].tubeRows == tube.tubeRows && vm.obox.stockInFrozenTubeList[m].tubeColumns == +tube.tubeColumns){
                        vm.obox.stockInFrozenTubeList[m] = tube;
                    }
                }
            }
            if(!selectList.length || !vm.frozenBoxCode ){
                modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'app/bizs/stock-in/stock-in-splittingBox-message-modal.html',
                    controller: 'SplittingBoxMessageController',
                    controllerAs:'vm'
                });
                return
            }
            //总管子数
            var tubeCount = colCount*rowCount;
            //剩余管子数
            var surplusCount =  tubeCount - vm.obox.countOfSample;
            //选中的被分装的管子数
            var selectCount = selectList.length;
            //分装到哪个盒子中的数量
            if( selectCount <= surplusCount){
                vm.obox.addTubeCount  += selectCount
            }else{
                if(surplusCount != 0){
                    vm.obox.addTubeCount  = surplusCount;
                }
            }
            for(var i = 0; i < vm.incompleteBoxesList.length; i++){
                var incompleteBoxes = vm.incompleteBoxesList[i];
                for(var j = 0; j < incompleteBoxes.boxList.length;j++ ){
                    if(vm.obox.frozenBoxCode == incompleteBoxes.boxList[j].frozenBoxCode){
                        incompleteBoxes.boxList[j].addTubeCount = vm.obox.addTubeCount;
                    }
                }
            }
            //清空被分装的盒子数
            for(var k = 0; k < selectList.length; k++){
                vm.frozenTubeArray[getTubeRowIndex(selectList[k].tubeRows)][getTubeColumnIndex(selectList[k].tubeColumns)] = "";
            }
            hotRegisterer.getInstance('my-handsontable').render();
            //分装数据
            for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
                if(!vm.obox.stockInFrozenTubeList[i].frozenBoxCode){
                    // 当盒子中管子剩余数为0时，自动添加第二个盒子
                    if(surplusCount){
                        if(selectList.length){
                            // selectList[0].tubeRows = vm.obox.stockInFrozenTubeList[i].tubeRows;
                            // selectList[0].tubeColumns = vm.obox.stockInFrozenTubeList[i].tubeColumns;
                            // selectList[0].frozenBoxCode = vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            vm.obox.stockInFrozenTubeList[i].frozenBoxCode = vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            vm.obox.stockInFrozenTubeList[i].id = selectList[0].id;
                            // vm.obox.stockInFrozenTubeList[i].frozenBoxCode = selectList[0].frozenBoxCode;
                            delete vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            selectList.splice(0,1);
                        }else{
                            break;
                        }
                    }

                }
            }
            //删除空管子
            var deleteIndexList = [];
            for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
                if(!vm.obox.stockInFrozenTubeList[i].frozenBoxCode){
                    deleteIndexList.push(i)
                }
            }
            _.pullAt(vm.obox.stockInFrozenTubeList, deleteIndexList);
            vm.obox.countOfSample = vm.obox.stockInFrozenTubeList.length;
            tubeList = angular.copy(vm.obox.stockInFrozenTubeList);
            if(vm.obox.countOfSample == tubeCount && selectList.length){
                var frozenBox = {};
                frozenBox.frozenBoxTypeId= vm.obox.frozenBoxType.id;
                frozenBox.sampleTypeId= vm.obox.sampleType.id;
                frozenBox.sampleType= vm.obox.sampleType;
                frozenBox.sampleClassificationId= vm.obox.sampleClassificationId;
                frozenBox.stockInFrozenTubeList= selectList;

                vm.addBoxModal(frozenBox);
            }
            var obox = angular.copy(vm.obox);

            if(!vm.boxList.length){
                vm.boxList.push(obox);
            }else{
                for(var i = 0; i < vm.boxList.length; i++){
                    if(obox.frozenBoxId == vm.boxList[i].frozenBoxId){
                        if(vm.boxList.length > 1){
                            vm.boxList[1].stockInFrozenTubeList = obox.stockInFrozenTubeList;

                        }else{
                            vm.boxList[0].stockInFrozenTubeList = obox.stockInFrozenTubeList;
                        }
                        break;
                    }else{
                        vm.boxList.push(obox);
                        break;
                    }
                }
            }
        };
        //保存分装结果
        var boxArray = [];
        vm.saveBox = function () {

            // for(var i = 0; i < vm.incompleteBoxesList.length; i++){
            //     for(var j = 0; j < vm.incompleteBoxesList[i].boxList.length;j++){
            //         boxArray.push(vm.incompleteBoxesList[i].boxList[j])
            //     }
            // }
            //
            for(var i = 0; i < vm.boxList.length; i++){
                vm.boxList[i].sampleTypeId = vm.boxList[i].sampleTypeId || vm.boxList[i].sampleType.id;
                vm.boxList[i].frozenBoxTypeId = vm.boxList[i].frozenBoxTypeId || vm.boxList[i].frozenBoxType.id;
                if(!vm.boxList[i].sampleClassificationId){
                    vm.boxList[i].sampleClassificationId =  vm.boxList[i].sampleClassification.id;
                }
                delete vm.boxList[i].sampleType;
                delete vm.boxList[i].frozenBoxType;
                delete vm.boxList[i].sampleClassification;
                delete vm.boxList[i].addTubeCount;
                delete vm.boxList[i].copyBoxCode;
                delete vm.boxList[i].countOfSample;
                delete vm.boxList[i].backColor;
                delete vm.boxList[i].sampleTypeName;
            }
            _blockUiStart(blockUiMessage);
            SplitedBoxService.saveSplit(vm.stockInCode,vm.box.frozenBoxCode,vm.boxList).success(function (data) {
                _blockUiStop();
                toastr.success("分装成功!");
                vm.headerCompiled = false;
                vm.dtInstance.rerender();
                _splitABox(vm.box.frozenBoxCode);
                vm.boxList = [];
                vm.frozenBoxCode = "";
                $(".box-selected").removeClass("box-selected");
            }).error(function (data) {
                _blockUiStop();
                toastr.error(data.message)
            })
        };
        //复原
        vm.recover = function () {
            // vm.incompleteBoxesList = [];
            _fnTubeByBoxCode(vm.box.frozenBoxCode);
            // for(var j = 0; j < vm.incompleteBoxesList.length; j++){
            //     for(var k = 0; k < vm.incompleteBoxesList[j].boxList.length;k++){
            //         vm.incompleteBoxesList[j].boxList[k].addTubeCount = 0;
            //     }
            // }
            vm.boxList = [];
            tubeList = [];
            selectList = [];
            vm.frozenBoxCode = "";

            $(".box-selected").removeClass("box-selected");
        };
        //关闭
        vm.closeBox = function () {

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/stock-in-close-splittingBox-modal.html',
                controller: 'CloseSplittingBoxController',
                controllerAs:'vm',
                size:'sm'
            });
            modalInstance.result.then(function (flag) {
                if(flag && vm.boxList.length) {

                    SplitedBoxService.saveSplit(vm.stockInCode, vm.box.frozenBoxCode, vm.boxList).then(function (data) {
                        toastr.success("分装成功!");
                    })
                }
                vm.splittingBox = false;
            });
        };
        //添加分装样本盒
        vm.addBoxModal = function (box) {
            // var sampleTypesList = angular.copy(vm.sampleTypes);
            // if(!box){
            //     var delIndex = [];
            //     for(var i = 0; i < vm.sampleTypes.length; i++){
            //         for (var j = 0; j < vm.incompleteBoxesList.length; j++){
            //             if(vm.sampleTypes[i].sampleTypeCode == vm.incompleteBoxesList[j].sampleTypeCode){
            //                 delIndex.push(i)
            //
            //             }
            //         }
            //     }
            //     _.pullAt(sampleTypesList,delIndex);
            // }
            // if(!sampleTypesList.length){
            //     return;
            // }

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/add-box-modal.html',
                controller: 'AddBoxModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            projectId:vm.entity.projectId,
                            box :box || {stockInFrozenTubeList:[]},
                            incompleteBoxes: vm.incompleteBoxesList,
                            sampleTypeId:vm.box.sampleType.id,
                            sampleTypeClassId:vm.sampleTypeClassId || "",
                            frozenBoxTypeId:vm.box.frozenBoxType.id
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {
                if(data){
                    selectList = [];
                    //添加分装后的冻存盒，没有添加新的，有的话再添加相同的盒子，相同的最多添加2个
                    var index;
                    if(data.sampleClassificationId){
                        index = _.findIndex(vm.incompleteBoxesList,{sampleTypeId:data.sampleClassificationId});
                    }else{
                        index = _.findIndex(vm.incompleteBoxesList,{sampleTypeId:data.sampleTypeId});
                    }
                    if(index == -1){
                        var boxTempList  = [];
                        boxTempList.push(data);
                        onIncompleteBoxesSuccess(boxTempList)
                    }else{
                        //盒子编码太长时，用星号代替
                        if(data.frozenBoxCode.length > 10){
                            data.copyBoxCode = _fnReplaceBoxCode(data.frozenBoxCode);
                        }else{
                            data.copyBoxCode = data.frozenBoxCode;
                        }
                        data.addTubeCount = data.countOfSample;
                        data.countOfSample = 0;
                        data.backColor = data.sampleType.backColor;
                        data.sampleTypeName = data.sampleClassification.sampleClassificationName;
                        for(var i = 0; i < vm.incompleteBoxesList.length; i++){
                            if(vm.incompleteBoxesList[i].sampleTypeId == data.sampleType.id){
                                if(vm.incompleteBoxesList[i].boxList.length < 2){
                                    vm.incompleteBoxesList[i].boxList.push(data);
                                    vm.boxList.push(data);
                                }

                            }
                        }
                        tubeList = [];
                        vm.frozenBoxCode = "";
                        $(".box-selected").removeClass("box-selected");
                    }
                }else{
                    //复原被分装的剩余管子数
                    for(var k = 0; k < selectList.length; k++){
                        vm.frozenTubeArray[getTubeRowIndex(selectList[k].tubeRows)][getTubeColumnIndex(selectList[k].tubeColumns)] = selectList[k];
                    }
                    // $timeout(function(){
                    hotRegisterer.getInstance('my-handsontable').render();
                    // },500);
                }

            });
        };
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
    function RescindPutAwayModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
