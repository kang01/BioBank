/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanListController', PlanListController);

    PlanListController.$inject = ['$scope','$compile','$state','DTOptionsBuilder','DTColumnBuilder','PlanService','BioBankDataTable'];

    function PlanListController($scope,$compile,$state,DTOptionsBuilder,DTColumnBuilder,PlanService,BioBankDataTable) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('plan-new');
        }

        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                PlanService.queryPlanList(data, oSettings).then(function (res){
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
            })
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50,
                    iFilterLength:3
                },{
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true,
                    iFilterLength:3
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: false
                }, {
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: [
                        {value:'1401',label:"进行中"},
                        {value:"1402",label:"已完成"},
                        {value:"1403",label:"已作废"}
                    ]
                },null]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('applyNumber').withTitle('申请单号'),
            DTColumnBuilder.newColumn('stockOutPlanCode').withTitle('计划编号'),
            DTColumnBuilder.newColumn('planDate').withTitle('计划时间'),
            DTColumnBuilder.newColumn('purposeOfSample').withTitle('出库目的'),
            DTColumnBuilder.newColumn('countOfStockOutPlanSample').withTitle('计划样本量'),
            DTColumnBuilder.newColumn('countOfStockOutTask').withTitle('出库任务量'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function createdRow(row, data, dataIndex) {
            var planStatus = '';
            switch (data.status){
                case '1401': planStatus = '进行中';break;
                case '1402': planStatus = '已完成';break;
                case '1403': planStatus = '已作废';break;
            }
            $('td:eq(6)', row).html(planStatus);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-xs" ui-sref="plan-edit({planId:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;';
        }
    }
})();
