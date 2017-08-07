/**
 * Created by zhuyu on 2017/3/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInController', StockInController);

    StockInController.$inject = ['$scope', '$compile', 'Principal', 'StockInService', 'ParseLinks', 'AlertService', '$state', 'pagingParams', 'paginationConstants', 'JhiLanguageService','DTOptionsBuilder','DTColumnBuilder'];

    function StockInController($scope, $compile, Principal, StockInService, ParseLinks, AlertService, $state, pagingParams, paginationConstants, JhiLanguageService,DTOptionsBuilder,DTColumnBuilder) {
        var vm = this;
        vm.addRecord = _fnAddRecord;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        // vm.transition = transition;

        vm.dtInstance = {};
        vm.message = '';

        _initJqueryDataTable();

        function _initJqueryDataTable(){
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

            vm.dtOptions = DTOptionsBuilder.fromSource({"url": 'api/temp/res/stock-in',"dataSrc": "data"})
                .withOption('order', [[0, 'desc' ]])
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
            StockInService.getJqDataTableValues(data, oSettings).then(function (res){
                var json = res.data;
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
        }
        function _fnCreatedRow(row, data, dataIndex) {
            var transportCodes = _.replace(data.transhipCode, /,/g, ', ');;

            var status = '';
            switch (data.status){
                case '7001': status = '进行中'; break;
                case '7002': status = '已入库'; break;
                case '7090': status = '已作废'; break;
            }
            $('td:eq(1)', row).html(transportCodes);
            $("td:eq(6)", row).text([data.storeKeeper1, data.storeKeeper2].join("; "));
            $('td:eq(9)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnActionButtonsRender(data, type, full, meta) {
            if(full.status == '7090'){
                return '';
            }else{
                if(full.transhipCode){
                    if(full.status == '7002'){
                        return '<button type="button" class="btn btn-xs" ui-sref="stock-in-edit({id:'+ full.id +'})">' +
                            '   <i class="fa fa-eye"></i>' +
                            '</button>&nbsp;';
                    }else{
                        return '<button type="button" class="btn btn-xs" ui-sref="stock-in-edit({id:'+ full.id +'})">' +
                            '   <i class="fa fa-edit"></i>' +
                            '</button>&nbsp;';
                    }

                }else{
                    if(full.status == '7002'){
                        return '<button type="button" class="btn btn-xs" ui-sref="stock-in-add-box-edit({id:'+ full.id +'})">' +
                            '   <i class="fa fa-eye"></i>' +
                            '</button>&nbsp;';
                    }else{
                        return '<button type="button" class="btn btn-xs" ui-sref="stock-in-add-box-edit({id:'+ full.id +'})">' +
                            '   <i class="fa fa-edit"></i>' +
                            '</button>&nbsp;';
                    }

                }

            }


        }

        function _createColumnFilters(){
            var filters = {
                aoColumns: [
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true,iFilterLength:3},
                    {type: 'text',bRegex: true,bSmart: true},
                    {type: 'text',bRegex: true,bSmart: true},
                    {
                        type: 'select',
                        bRegex: true,
                        bSmart: true,
                        values: [
                            {value:'7001',label:"进行中"},
                            {value:"7002",label:"已入库"}
                        ]
                    }

                ]
            };

            return filters;
        }
        function _createColumns(){
            var columns = [
                DTColumnBuilder.newColumn('stockInCode').withTitle('入库编码').withOption('width','100'),
                DTColumnBuilder.newColumn('transhipCode').withTitle('转运编码').withOption('width','auto'),
                DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点').withOption('width','70'),
                DTColumnBuilder.newColumn('projectCode').withTitle('项目编号').withOption('width','70'),
                DTColumnBuilder.newColumn('recordDate').withTitle('创建日期').withOption('width','90'),
                DTColumnBuilder.newColumn('stockInDate').withTitle('入库日期').withOption('width','90'),
                DTColumnBuilder.newColumn('storeKeeper1').withTitle('库管员').withOption('width','90'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('样本数量').withOption('width','80'),
                DTColumnBuilder.newColumn('countOfBox').withTitle('冻存盒数量').withOption('width','100'),
                DTColumnBuilder.newColumn('status').withTitle('状态').withOption('width','80'),
                DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(_fnActionButtonsRender).withOption('width','40')
            ];

            return columns;
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        function _fnAddRecord() {
            $state.go("stock-in-new")
        }


    }
})();
