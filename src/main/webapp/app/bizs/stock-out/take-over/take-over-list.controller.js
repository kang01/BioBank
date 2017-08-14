/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverListController', TakeOverListController);

    TakeOverListController.$inject = ['$scope','$compile','$state','DTOptionsBuilder','DTColumnBuilder','TakeOverService','BioBankDataTable'];

    function TakeOverListController($scope,$compile,$state,DTOptionsBuilder,DTColumnBuilder,TakeOverService,BioBankDataTable) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('take-over-new');
        }

        // vm.dtOptions = DTOptionsBuilder.newOptions()
        //     .withDOM("<'row mt-0 mb-10'<'col-xs-6' > <'col-xs-6' f> r> t <'row'<'col-xs-6'i> <'col-xs-6'p>>")
        //     .withOption('processing',true)
        //     .withPaginationType('full_numbers')
        //     .withOption('serverSide',true)
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('serverSide',true)
            .withFnServerData(fnServerData)
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    // 交接单编码
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    // 申请单编码
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    // 用途
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    // 交接样本
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    // 交接时间
                    type: 'Datepicker',
                    bRegex: true,
                    bSmart: true
                }, {
                    // 接收方
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    // 交付人
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    // 状态
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: [
                        {value:'2101',label:"进行中"},
                        {value:"2102",label:"已交接"},
                        {value:"2190",label:"已作废"}
                    ]
                }]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('handoverCode').withTitle('交接单编码'),
            DTColumnBuilder.newColumn('applyCode').withTitle('申请单编号'),
            DTColumnBuilder.newColumn('usage').withTitle('出库用途'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('交接样本'),
            DTColumnBuilder.newColumn('receiveDate').withTitle('交接时间'),
            DTColumnBuilder.newColumn('receiver').withTitle('接收方'),
            DTColumnBuilder.newColumn('deliverName').withTitle('交付人'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];

        function fnServerData ( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            TakeOverService.queryTakeOverList(data, oSettings).then(function (res){
                var json = res.data;
                var error = json.error || json.sError;
                if ( error ) {
                    jqDt._fnLog( oSettings, 0, error );
                }
                oSettings.json = json;
                fnCallback( json );
            }, function(res){
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
        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '2101': status = '进行中';break;
                case '2102': status = '已交接';break;
                case '2190': status = '已作废';break;
            }
            $('td:eq(7)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            if(full.status == '2190'){
                return "";
            }
            if (full.status != '2102'){
                return '<a type="button" class="btn btn-default btn-xs" ui-sref="take-over-edit({id:'+ full.id +'})">' +
                    '   <i class="fa fa-pencil"></i>' +
                    '</a>&nbsp;';
            }
            return '<a type="button" class="btn btn-default btn-xs" ui-sref="take-over-view({id:'+ full.id +'})">' +
                '   <i class="fa fa-eye"></i>' +
                '</a>&nbsp;';
        }
    }
})();
