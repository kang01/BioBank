/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanListController', PlanListController);

    PlanListController.$inject = ['$scope','$compile','$state','DTOptionsBuilder','DTColumnBuilder','PlanService','BioBankDataTable','MasterData'];

    function PlanListController($scope,$compile,$state,DTOptionsBuilder,DTColumnBuilder,PlanService,BioBankDataTable,MasterData) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('plan-new');
        }
        function _fnStockOutSeach(sSource, aoData, fnCallback, oSettings) {
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
        }
        var t;
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('order', [[0, 'desc' ]])
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                //各列搜索
                var aoPreSearchCols = oSettings.aoPreSearchCols;
                var len = _.filter(aoPreSearchCols,{sSearch:""}).length;
                // 搜索框为空的时候不用timer
                if(!oSettings.oPreviousSearch.sSearch && len == aoPreSearchCols.length){
                    _fnStockOutSeach(sSource, aoData, fnCallback, oSettings);
                }else{
                    if(t){
                        clearTimeout(t);
                    }
                    t=setTimeout(function () {
                        _fnStockOutSeach(sSource, aoData, fnCallback, oSettings);
                    },2000);
                }
            })
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50
                },{
                    type: 'text',
                    width:50
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
                        {value:"1490",label:"已作废"}
                    ]
                },null]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('applyNumber').withTitle('申请单号').withOption("width","80"),
            DTColumnBuilder.newColumn('stockOutPlanCode').withTitle('计划编号').withOption("width","80"),
            DTColumnBuilder.newColumn('planDate').withTitle('计划时间').withOption("width","80"),
            DTColumnBuilder.newColumn('purposeOfSample').withTitle('出库目的').withOption("width","auto"),
            DTColumnBuilder.newColumn('countOfStockOutPlanSample').withTitle('计划样本量').withOption("width","100"),
            DTColumnBuilder.newColumn('countOfStockOutTask').withTitle('出库任务量').withOption("width","100"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width","80"),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function createdRow(row, data, dataIndex) {
            var planStatus = MasterData.getStatus(data.status);
            $('td:eq(6)', row).html(planStatus);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            var html;
            if(full.status == '1401'){
                html = '<button type="button" class="btn btn-default btn-xs" ui-sref="plan-edit({planId:'+ full.id +'})">' +
                    '   <i class="fa fa-edit"></i>' +
                    '</button>&nbsp;';
            }else{
                html = '<button type="button" class="btn btn-default btn-xs" ui-sref="plan-view({planId:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;';
            }
            return html;
        }
    }
})();
