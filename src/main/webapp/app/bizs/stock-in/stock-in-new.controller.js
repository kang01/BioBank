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

        vm.showSplittingPanel = function(){
            return vm.splittingBox && true;
        };

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
    }
})();
