/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController);

    StockInNewController.$inject = ['$timeout','$state','$stateParams', '$scope','$compile','hotRegisterer','DTOptionsBuilder','DTColumnBuilder','$uibModal',
        'entity','AlertService','StockInService','StockInBoxService','frozenBoxByCodeService',
        'SampleTypeService','SampleService','IncompleteBoxService']
    function StockInNewController($timeout,$state,$stateParams,$scope,$compile,hotRegisterer,DTOptionsBuilder,DTColumnBuilder,$uibModal,
                                  entity,AlertService,StockInService,StockInBoxService,frozenBoxByCodeService,
                                  SampleTypeService,SampleService,IncompleteBoxService) {
        var vm = this;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar; //时间
        vm.entity = {
            stockInCode: '1234567890',
            transhipCode: '1234567890',
            projectCode: '1234567890',
            projectSiteCode: '1234567890',
            receivedDate: '2017-03-31',
            receiver: '竹羽',
            stockInDate: '2017-03-31',
            storeKeeper1: '竹羽',
            storeKeeper2: '景福',
            status: '7001',
        };
        vm.stockInCode = vm.entity.stockInCode;
        vm.entityBoxes = {};
        vm.splittingBox = null;
        vm.splittedBoxes = {};
        vm.dtInstance = {};



        _initStockInBoxesTable();

        function _initStockInBoxesTable(){
            vm.selectedStockInBoxes = {};
            vm.selected = {};
            vm.selectAll = false;

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

            vm.searchSomething = function(term){
                var table = vm.dtInstance.DataTable;
                table
                    .column(0)
                    .search(term)
                    .draw();
            };

            vm.dtOptions = DTOptionsBuilder.fromSource({"url": ajaxUrl,"dataSrc": "data"})
                .withDOM("<'row'<'col-xs-6' TB><'col-xs-6' f>r>t<'row'<'col-xs-6'i><'col-xs-6'p>>")
                .withDisplayLength(6)
                .withBootstrap()
                .withBootstrapOptions({
                    pagination: {
                        classes: {
                            ul: 'pagination pagination-sm'
                        }
                    }
                })
                // Add Table tools compatibility
                .withButtons([
                    {
                        text: '批量上架',
                        className: 'btn btn-default',
                        key: '1',
                        action: function (e, dt, node, config) {
                            alert('Button activated');
                        }
                    }
                ])
                .withOption('sServerMethod','POST')
                .withOption('processing',true)
                .withOption('serverSide',true)
                .withFnServerData(_fnServerData)
                .withPaginationType('full_numbers')
                .withOption('createdRow', _fnCreatedRow)
                .withColumnFilter(_createColumnFilters());

            vm.dtColumns = _createColumns();

            vm.splitIt = _splitABox;
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
            // 2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废
            switch (data.status){
                case '2001': status = '新建'; break;
                case '2002': isSplit ? status = '待分装' : status = '待入库'; break;
                case '2003': status = '已分装'; break;
                case '2004': status = '已入库'; break;
                case '2005': status = '已作废'; break;
            }
            $('td:eq(5)', row).html(isSplit ? '需要分装' : '');
            $('td:eq(6)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            // console.log(vm.splitIt, vm.putInShelf);
            return '<button type="button" class="btn btn-xs btn-warning" ng-click="vm.splitIt('+ full.frozenBoxCode +')">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;' +
                '<button type="button" class="btn btn-xs btn-error" ng-click="vm.putInShelf('+ full.id +')">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;';

        }
        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            return '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
        }

        function _createColumnFilters(){
            var filters = {
                aoColumns: [
                    null,
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    null
                ]
            };

            return filters;
        }
        function _createColumns(){
            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll(vm.selectAll, vm.selected)">';

            var columns = [
                // DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
                DTColumnBuilder.newColumn("").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号'),
                DTColumnBuilder.newColumn('sampleType').withTitle('样本类型'),
                DTColumnBuilder.newColumn('position').withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('样本量'),
                DTColumnBuilder.newColumn('isSplit').withTitle('是否分装'),
                DTColumnBuilder.newColumn('status').withTitle('状态'),
                DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(_fnActionButtonsRender),
                DTColumnBuilder.newColumn('id').notVisible(),
                DTColumnBuilder.newColumn('sampleType').notVisible(),
                DTColumnBuilder.newColumn('frozenBoxRows').notVisible(),
                DTColumnBuilder.newColumn('frozenBoxColumns').notVisible(),
            ];

            return columns;
        }

        function _splitABox(code){
            frozenBoxByCodeService.get({code:'23432'},onFrozenSuccess,onError);
            function onFrozenSuccess(data) {
                vm.splittingBox = true;
                vm.box =  data;
                for(var k = 0; k < vm.box.frozenTubeDTOS.length; k++){
                    var tube = vm.box.frozenTubeDTOS[k];
                    vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
                }

                $timeout(function(){
                    hotRegisterer.getInstance('my-handsontable').render();
                }, 200);
            }
        }

        function _putInShelf(boxIds){
            if (typeof boxIds !== "object"){
                boxIds = [boxIds];
            } else {
                boxIds = [];
                for(var id in vm.selected){
                    if(vm.selected[id]) {
                        boxIds.push(id);
                    }
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
                            boxIds: boxIds
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
            });
        }








        vm.isShowSplittingPanel = function(){
            return vm.splittingBox && true;
        };






        vm.frozenTubeArray = [];//初始管子的单元格
        vm.incompleteBoxesList = []; //分装后的样本类型盒子，未装满样本的盒子
        var tempTubeArray = [];//选中未满样本盒子的临时数据，需要操作管子
        var selectList = [];//选择单元格的管子数据
        var modalInstance;
        var size = 10;
        var htm;
        vm.loadBox = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);
        };
        //样本类型
        function onSampleTypeSuccess(data) {
            vm.sampleTypes = data;
            for(var i = 0; i < vm.sampleTypes.length;i++){
                IncompleteBoxService.query({projectCode:"12345",sampleType:vm.sampleTypes[i].sampleTypeCode},onIncompleteBoxesSuccess,onError)
            }
        }
        function onIncompleteBoxesSuccess(data) {
            vm.incompleteBoxesList.push(data[0]);
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.loadBox();

        function initFrozenTube(size) {
            for(var i = 0; i < size; i++){
                vm.frozenTubeArray[i] = [];
                for(var j = 0;j < size; j++){
                    vm.frozenTubeArray[i][j] = "";
                }
            }
        }
        initFrozenTube(size);

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
                htm = "<div ng-if='value.sampleCode'>"+value.sampleCode+"</div>"+
                    "<div ng-if='value.sampleTmpCode'>"+value.sampleTempCode+"</div>"+
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
            editor: false,
            onAfterSelectionEnd:function (row, col, row2, col2) {
                // console.log(this)
                // console.log($(this.getData(row,col,row2,col2)))
                vm.selectCell = $(this.getData(row,col,row2,col2));
                // if(vm.selectCell.length){
                for(var i = 0; i < vm.selectCell.length; i++ ){
                    for (var j = 0; j < vm.selectCell[i].length; j++){
                        // console.log(JSON.stringify(vm.selectCell[i][j]))
                        selectList.push(vm.selectCell[i][j])
                    }
                }
                // }
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

        //上架操作
        vm.putAway = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/box-putaway-modal.html',
                controller: 'BoxPutAwayModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            transhipId :"aaaa"
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
            });
        };
        //选择分装后的样本盒
        var tubeList = [];
        vm.sampleBoxSelect = function (item,$event) {

            for(var i = 0; i < item.frozenBoxRows; i++){
                tempTubeArray[i] = [];
                for(var j = 0;j < item.frozenBoxColumns; j++){
                    tempTubeArray[i][j] = {
                        tubeColumns: j+1,
                        tubeRows: String.fromCharCode(i+65),
                        frozenTubeCode:''
                    };
                    tubeList.push(tempTubeArray[i][j])
                }
            }
            for(var k = 0; k <item.stockInFrozenTubeList.length; k++){
                var tube = item.stockInFrozenTubeList[k];
                if(tubeList[k].tubeRows == tube.tubeRows && tubeList[k].tubeColumns == tube.tubeColumns){
                    tubeList[k] = tube
                }
            }
            // console.log(JSON.stringify(tubeList));



            $($event.target).closest('ul').find('.box-selected').removeClass("box-selected");
            $($event.target).addClass("box-selected");
        };
        var emptyList = [];
        //分装操作
        vm.splitBox = function () {
            vm.addTubeCount = selectList.length;
            for(var i = 0; i < tubeList.length; i++){
                if(!tubeList[i].frozenTubeCode){
                    for(var j = 0; j < selectList.length; j++){
                        tubeList[i] = selectList[j];
                        selectList.splice(j,1)
                    }
                }
            }

            // console.log(JSON.stringify(tubeList));

        };
        vm.editBox = function () {
            _splitABox();
        }
        vm.addBoxModal = function () {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-in/add-box-modal.html',
                controller: 'AddBoxModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            sampleTypes :vm.sampleTypes
                        }
                    }
                }
            });
            modalInstance.result.then(function (data) {
            });
        }
        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
