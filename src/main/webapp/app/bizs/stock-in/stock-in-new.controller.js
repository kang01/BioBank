/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController);

    StockInNewController.$inject = ['$timeout','$state','$stateParams', '$scope','$compile','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal',
        'entity','AlertService','StockInService','StockInBoxService','frozenBoxByCodeService','SplitedBoxService','StockInSaveService',
        'SampleTypeService','SampleService','IncompleteBoxService']
    function StockInNewController($timeout,$state,$stateParams,$scope,$compile,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,
                                  entity,AlertService,StockInService,StockInBoxService,frozenBoxByCodeService,SplitedBoxService,StockInSaveService,
                                  SampleTypeService,SampleService,IncompleteBoxService) {
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
            $('td:eq(5)', row).html(isSplit ? '需要分装' : '');
            $('td:eq(6)', row).html(status);
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
            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll(vm.selectAll, vm.selected)">';

            var columns = [
                // DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号'),
                DTColumnBuilder.newColumn('sampleTypeName').withOption("width", "80").withTitle('样本类型'),
                DTColumnBuilder.newColumn('position').withOption("width", "auto").withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withOption("width", "90").withTitle('样本量'),
                DTColumnBuilder.newColumn('isSplit').withOption("width", "100").withTitle('是否分装'),
                DTColumnBuilder.newColumn('status').withOption("width", "80").withTitle('状态'),
                DTColumnBuilder.newColumn("").withOption("width", "120").withTitle('操作').notSortable().renderWith(_fnActionButtonsRender),
                DTColumnBuilder.newColumn('id').notVisible(),
                DTColumnBuilder.newColumn('sampleType').notVisible(),
                DTColumnBuilder.newColumn('frozenBoxRows').notVisible(),
                DTColumnBuilder.newColumn('frozenBoxColumns').notVisible()
            ];

            return columns;
        }

        function _splitABox(code){
            _fnTubeByBoxCode(code);
            vm.loadBox();
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
                            boxes: boxes,
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                vm.dtInstance.rerender();
            });
        }






        //入库完成
        vm.saveStockIn = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/stock-in-info-modal.html',
                controller: 'StockInInfoModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            id: vm.entity.id,
                            stockInDate: vm.entity.stockInDate,
                            storeKeeper1: vm.entity.storeKeeper1,
                            storeKeeper2: vm.entity.storeKeeper2
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                StockInSaveService.saveStockIn(vm.stockInCode).then(function () {
                    AlertService.success("入库完成成功!");
                    _initStockInBoxesTable();
                })
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
            frozenBoxByCodeService.get({code:code},onFrozenSuccess,onError);
            function onFrozenSuccess(data) {
                vm.splittingBox = true;
                initFrozenTube(data.frozenBoxRows);
                vm.box =  data;
                for(var k = 0; k < vm.box.frozenTubeDTOS.length; k++){
                    var tube = vm.box.frozenTubeDTOS[k];
                    vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
                }
            }
            $timeout(function(){
                hotRegisterer.getInstance('my-handsontable').render();
            }, 200);
        }
        //样本类型
        vm.loadBox = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);
        };
        function onSampleTypeSuccess(data) {
            vm.incompleteBoxesList = [];
            vm.sampleTypes = data;
            //未装满不同样本类型的盒子
            for(var i = 0; i < vm.sampleTypes.length-1;i++){
                IncompleteBoxService.query({projectCode:vm.entity.projectCode,sampleTypeCode:vm.sampleTypes[i].sampleTypeCode},onIncompleteBoxesSuccess,onError)
            }
        }
        function onIncompleteBoxesSuccess(data) {
            if(data.length){
                // _fnReplaceBoxCode(data[0]);
                data[0].addTubeCount = 0;
                if(data[0].frozenBoxCode != vm.box.frozenBoxCode){
                    vm.incompleteBoxesList.push(
                        {
                            sampleTypeCode:data[0].sampleType.sampleTypeCode,
                            boxList:data
                        }
                    );
                }

            }
            // console.log(JSON.stringify(vm.incompleteBoxesList))
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        //盒子编码太长时，用星号代替
        vm.replaceBoxCode = function (code) {
            if(code.length > 10){
                return "***"+code.substring(9,-10)
            }
        };
        // function _fnReplaceBoxCode(data) {
        //     if(data.frozenBoxCode.length > 10){
        //         data.frozenBoxCode="***"+data.frozenBoxCode.substring(9,-10)
        //     }
        // }
        //初始管子数
        function initFrozenTube(size) {
            for(var i = 0; i < size; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < size; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        function getTubeRowIndex(row) {
            return row.charCodeAt(0) -65;
        }
        function getTubeColumnIndex(col) {
            return +col -1;
        }

        vm.customRenderer = function (hotInstance, td, row, col, prop, value, cellProperties) {
            if(value != ""){
                //样本类型
                if(value.sampleTypeCode){
                    SampleService.changeSampleType(value.sampleTypeCode,td);
                }
                htm = "<div ng-if='value.sampleCode' style='line-height: 20px'>"+value.sampleCode+"</div>"+
                    "<div ng-if='value.sampleTmpCode' style='line-height: 20px'>"+value.sampleTempCode+"</div>"+
                    "<div  style='display: none'>"+value.sampleTypeCode+"</div>"+
                    "<div  style='display: none'>"+value.status+"</div>"+
                    "<div  style='display: none'>"+value.memo+"</div>"+
                    "<div  style='display: none'>"+value.tubeRows+"</div>"+
                    "<div  style='display: none'>"+value.tubeColumns+"</div>"+
                    "<div ng-if="+value.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"
            }else {
                htm = ""
            }
            td.style.position = 'relative';


            td.innerHTML = htm;
        };
        vm.settings ={
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
            minRows: 10,
            minCols: 10,
            data:vm.frozenTubeArray,
            renderer:vm.customRenderer,
            fillHandle:false,
            stretchH: 'all',
            wordWrap:true,
            colWidths: 90,
            editor: false,
            outsideClickDeselects:false,
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
        vm.boxList = [];
        vm.obox = {};
        //选中要分装样本盒
        vm.sampleBoxSelect = function (item,$event) {
            vm.obox = angular.copy(item);
            vm.obox.stockInFrozenTubeList = [];
            //初始100个管子或者80个管子
            for(var i = 0; i < item.frozenBoxRows; i++){
                tempTubeArray[i] = [];
                for(var j = 0;j < item.frozenBoxColumns; j++){
                    tempTubeArray[i][j] = {
                        tubeColumns: j+1,
                        tubeRows: String.fromCharCode(i+65),
                        frozenTubeCode:'',
                        selectTubeCode:item.frozenBoxCode
                    };
                    vm.obox.stockInFrozenTubeList.push(tempTubeArray[i][j])
                }
            }
            for(var m =0 ; m < vm.obox.stockInFrozenTubeList.length; m++){
                for(var n = 0; n < item.stockInFrozenTubeList.length;n++){
                    var tube = item.stockInFrozenTubeList[n];
                    if(vm.obox.stockInFrozenTubeList[m].tubeRows == tube.tubeRows && vm.obox.stockInFrozenTubeList[m].tubeColumns == +tube.tubeColumns){
                        vm.obox.stockInFrozenTubeList[m] = tube;
                    }
                }
            }
            // console.log(JSON.stringify(vm.obox.stockInFrozenTubeList));
            $($event.target).closest('ul').find('.box-selected').removeClass("box-selected");
            $($event.target).addClass("box-selected");
        };
        //分装操作
        vm.splitBox = function () {
            if(!selectList.length || !vm.obox.stockInFrozenTubeList){
                AlertService.error("请选择被分装的冻存管或者请选择要分装到的盒子！");
                return
            }
            var tubeCount = vm.obox.frozenBoxColumns*vm.obox.frozenBoxRows;
            //盒子剩余数
            var surplusCount =  tubeCount - vm.obox.countOfSample;
            //要分装的管子数
            var selectCount = selectList.length;
            //分装到哪个盒子中的数量
            for(var j = 0; j < vm.incompleteBoxesList.length; j++){
                for(var k = 0; k < vm.incompleteBoxesList[j].boxList.length;k++){
                    if(vm.obox.sampleTypeCode == vm.incompleteBoxesList[j].boxList[k].sampleTypeCode){
                        if( selectCount > surplusCount){
                            vm.incompleteBoxesList[j].boxList[k].addTubeCount = surplusCount;
                        }else{
                            vm.incompleteBoxesList[j].boxList[k].addTubeCount +=selectCount
                        }
                    }
                }
            }
            //清空被分装的盒子数
            for(var k = 0; k < selectList.length; k++){
                vm.frozenTubeArray[getTubeRowIndex(selectList[k].tubeRows)][getTubeColumnIndex(selectList[k].tubeColumns)] = "";
            }
            //分装数据
            for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
                if(!vm.obox.stockInFrozenTubeList[i].frozenTubeCode){
                    // 当盒子中管子剩余数为0时，自动添加第二个盒子
                    if(surplusCount){
                        if(selectList.length){
                            selectList[0].tubeRows = vm.obox.stockInFrozenTubeList[i].tubeRows;
                            selectList[0].tubeColumns = vm.obox.stockInFrozenTubeList[i].tubeColumns;
                            selectList[0].frozenBoxCode = vm.obox.stockInFrozenTubeList[i].selectTubeCode;
                            vm.obox.stockInFrozenTubeList[i].frozenTubeCode = selectList[0].frozenTubeCode;
                            // selectList[0].sampleTempCode = vm.obox.stockInFrozenTubeList[i].selectTubeCode+"-"+selectList[0].tubeRows+selectList[0].tubeColumns;
                            vm.obox.stockInFrozenTubeList[i] = selectList[0];
                            selectList.splice(0,1);
                            surplusCount --;
                        }
                    }
                }
            }
            hotRegisterer.getInstance('my-handsontable').render();

            // console.log(JSON.stringify(vm.obox.stockInFrozenTubeList));
            //删除空管子
            var stockInFrozenTubeList = angular.copy(vm.obox.stockInFrozenTubeList);
            var deleteIndexList = [];
            for(var i = 0; i < stockInFrozenTubeList.length; i++){
                if(!stockInFrozenTubeList[i].frozenTubeCode){
                    deleteIndexList.push(i)
                }
            }
            _.pullAt(stockInFrozenTubeList, deleteIndexList);
            vm.obox.countOfSample = stockInFrozenTubeList.length;
            if(selectList.length){
                var frozenBox = {};
                frozenBox.frozenBoxTypeId= vm.obox.frozenBoxTypeId;
                frozenBox.sampleTypeCode= vm.obox.sampleTypeCode;
                frozenBox.sampleTypes= vm.obox.sampleTypes;
                frozenBox.frozenBoxRows= vm.obox.frozenBoxRows;
                frozenBox.frozenBoxColumns= vm.obox.frozenBoxColumns;
                frozenBox.stockInFrozenTubeList= selectList;
                vm.addBoxModal(frozenBox);
            }
        };
        //保存分装结果
        vm.saveBox = function () {
            var deleteIndexList = [];
            for(var i = 0; i < vm.obox.stockInFrozenTubeList.length; i++){
                if(!vm.obox.stockInFrozenTubeList[i].frozenTubeCode){
                    deleteIndexList.push(i)
                }
            }
            _.pullAt(vm.obox.stockInFrozenTubeList, deleteIndexList);
            var obox = angular.copy(vm.obox);
            vm.boxList.push(obox);
            console.log(JSON.stringify(vm.boxList));
            // SplitedBoxService.saveSplit(vm.stockInCode,vm.box.frozenBoxCode,vm.boxList).then(function (data) {
            //     AlertService.success("分装成功!");
            //     _splitABox(vm.box.frozenBoxCode);
            //     vm.boxList = [];
            // })
        };
        //复原
        vm.recover = function () {
            _fnTubeByBoxCode(vm.box.frozenBoxCode);
            for(var j = 0; j < vm.incompleteBoxesList.length; j++){
                for(var k = 0; k < vm.incompleteBoxesList[j].boxList.length;k++){
                    vm.incompleteBoxesList[j].boxList[k].addTubeCount = 0;
                }
            }
            vm.boxList = [];
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
                        AlertService.success("分装成功!");
                    })
                }
                vm.splittingBox = false;
            });
        };
        //添加分装样本盒
        vm.addBoxModal = function (box) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/add-box-modal.html',
                controller: 'AddBoxModalController',
                controllerAs:'vm',
                size:'90',
                resolve: {
                    items: function () {
                        return {
                            sampleTypes :vm.sampleTypes,
                            box :box || {stockInFrozenTubeList:[]}
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
                //添加分装后的冻存盒，没有添加新的，有的话再添加相同的盒子，相同的最多添加2个
                var index = _.findIndex(vm.incompleteBoxesList,{sampleTypeCode:data.sampleType.sampleTypeCode});
                if(index == -1){
                    var boxTempList  = [];
                    boxTempList.push(data);
                    onIncompleteBoxesSuccess(boxTempList)
                }else{
                    // _fnReplaceBoxCode(data);
                    for(var i = 0; i < vm.incompleteBoxesList.length; i++){
                        if(vm.incompleteBoxesList[i].sampleTypeCode == data.sampleType.sampleTypeCode){
                            if(vm.incompleteBoxesList[i].boxList.length < 2){
                                vm.incompleteBoxesList[i].boxList.push(data);
                                vm.boxList.push(data);
                            }

                        }
                    }
                }
            });
        };
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
