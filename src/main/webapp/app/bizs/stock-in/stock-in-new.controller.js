/**
 * Created by zhuyu on 2017/3/14.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInNewController', StockInNewController);

    StockInNewController.$inject = ['$scope','hotRegisterer','StockInService','StockInBoxService','DTOptionsBuilder','DTColumnBuilder','$uibModal','$state','entity','frozenBoxByCodeService',
        'SampleTypeService','AlertService','FrozenBoxTypesService','FrozenBoxByIdService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','ProjectService','ProjectSitesByProjectIdService'];

    function StockInNewController($scope,hotRegisterer,StockInService,StockInBoxService,DTOptionsBuilder,DTColumnBuilder,$uibModal,$state,entity,frozenBoxByCodeService,
                                          SampleTypeService,AlertService,FrozenBoxTypesService,FrozenBoxByIdService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,ProjectService,ProjectSitesByProjectIdService) {
        var vm = this;
        vm.frozenTubeArray = [];
        vm.load = function () {
            SampleTypeService.query({},onSampleTypeSuccess, onError);
            frozenBoxByCodeService.get({code:'1213243543'},onFrozenSuccess,onError);
        };
        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.load();
        var size = 10;
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
        //样本类型
        function onSampleTypeSuccess(data) {
            vm.sampleTypes = data;
        }
        function onFrozenSuccess(data) {
            vm.box =  data
            for(var k = 0; k < vm.box.frozenTubeDTOS.length; k++){
                var tube = vm.box.frozenTubeDTOS[k];
                vm.frozenTubeArray[getTubeRowIndex(tube.tubeRows)][getTubeColumnIndex(tube.tubeColumns)] = tube;
            }
            console.log(JSON.stringify(vm.frozenTubeArray))
        }
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
        vm.entityBoxes = {};
        vm.splittingBox = null;
        vm.splittedBoxes = {};

        vm.dtInstance = {};



        _initStockInBoxesTable();


        function _initStockInBoxesTable(){
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
                .withOption('sServerMethod','POST')
                .withOption('processing',true)
                .withOption('serverSide',true)
                .withFnServerData(_fnServerData)
                .withPaginationType('full_numbers')
                .withOption('createdRow', _fnCreatedRow)
                .withColumnFilter(_createColumnFilters());

            vm.dtColumns = _createColumns();
        }

        function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
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
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning" ng-click="vm.splitIt('+ full.frozenBoxCode +')">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'
        }
        function _fnRowSelectorRender(data, type, full, meta) {
            return '';
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
                ]
            };

            return filters;
        }
        function _createColumns(){
            var columns = [
                // DTColumnBuilder.newColumn('id').withTitle('id').notVisible(),
                DTColumnBuilder.newColumn("").withTitle('选择').notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒号'),
                DTColumnBuilder.newColumn('sampleType').withTitle('样本类型'),
                DTColumnBuilder.newColumn('position').withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('样本量'),
                DTColumnBuilder.newColumn('isSplit').withTitle('是否分装'),
                DTColumnBuilder.newColumn('status').withTitle('状态'),
                DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(_fnActionButtonsRender)
            ];

            return columns;
        }


        vm.showSplittingPanel = function(){
            return vm.splittingBox && true;
        };
        function getTubeRowIndex(row) {
            return row.charCodeAt(0) -65;
        }
        function getTubeColumnIndex(col) {
            return +col -1;
        }
        var hot;
        vm.settings ={
            colHeaders : ['1','2','3','4','5','6','7','8','9','10'],
            rowHeaders : ['A','B','C','D','E','F','G','H','I','J'],
            minRows: 10,
            minCols: 10,
            data:vm.frozenTubeArray,
            renderer:customRenderer,
            fillHandle:false,
            stretchH: 'all',
            editor: false,
            onAfterSelectionEnd:function (row, col, row2, col2) {
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
        hot = hotRegisterer.getInstance('my-handsontable');
        var htm;
        function customRenderer(hotInstance, td, row, col, prop, value, cellProperties) {
            console.log(value)
            td.style.backgroundColor = 'yellow'
            td.style.position = 'relative';
            htm = "<div ng-if='value.sampleCode'>"+value.sampleCode+"</div>"+
                "<div id='microtubesId' style='display: none'>"+value.sampleCode+"</div>" +
                "<div id='microtubesStatus' style='display: none'>"+value.status+"</div>"+
                "<div id='microtubesRemark' style='display: none'>"+value.memo+"</div>"+
                "<div id='microtubesRow' style='display: none'>"+value.tubeRows+"</div>"+
                "<div id='microtubesCol' style='display: none'>"+value.tubeColumns+"</div>"+
                "<div ng-if="+value.memo+" class='triangle-topright' style='position: absolute;top:0;right: 0;'></div>"

            td.innerHTML = htm;
        }
    }
})();
