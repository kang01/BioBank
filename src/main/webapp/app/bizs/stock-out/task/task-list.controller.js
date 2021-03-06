/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskListController', TaskListController);

    TaskListController.$inject = ['$scope','$compile','$state','DTOptionsBuilder','DTColumnBuilder','TaskService','MasterData','BioBankDataTable'];

    function TaskListController($scope,$compile,$state,DTOptionsBuilder,DTColumnBuilder,TaskService,MasterData,BioBankDataTable) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('task-new');
        }

        var columnValues = _.map(MasterData.taskStatus, function(t){
            return { value:t.id,label:t.name };
        });
        function _fnStockOutSeach(sSource, aoData, fnCallback, oSettings) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            TaskService.queryTaskList(data, oSettings).then(function (res){
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
            .withOption('order', [['0', 'desc']])
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
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: false,
                    width:50
                }, {
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: columnValues
                }]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('stockOutTaskCode').withTitle('任务编码'),
            DTColumnBuilder.newColumn('applyCode').withTitle('申请编码'),
            DTColumnBuilder.newColumn('stockOutPlanCode').withTitle('计划编码'),
            DTColumnBuilder.newColumn('delegateName').withTitle('委托方').withOption('width',"300"),
            DTColumnBuilder.newColumn('stockOutDate').withTitle('出库时间'),
            DTColumnBuilder.newColumn('purposeOfSample').withTitle('出库目的'),
            DTColumnBuilder.newColumn('countOfStockOutSample').withTitle('出库任务量'),
            DTColumnBuilder.newColumn('countOfHandOverSample').withTitle('已交接样本'),
            DTColumnBuilder.newColumn('handOverTimes').withTitle('交接次数'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
        ];
        function createdRow(row, data, dataIndex) {
            var status = MasterData.getStatus(data.status);
            $('td:eq(9)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            var html;
            if(full.status == '1601'){
                html =
                    '<button type="button" class="btn btn-xs" ui-sref="task-edit({taskId:'+ full.id +'})">' +
                    '   <i class="fa fa-play"></i>' +
                    '</button>&nbsp;';
            }else if(full.status == '1603'|| full.status == '1604' || full.status == '1690'){
                html =
                    '<button type="button" class="btn btn-xs" ui-sref="task-view({taskId:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;';
            }else{
                html =
                    '<button type="button" class="btn btn-xs" ui-sref="task-edit({taskId:'+ full.id +'})">' +
                    '   <i class="fa fa-edit"></i>' +
                    '</button>&nbsp;';
            }


            return html;
        }
    }
})();
