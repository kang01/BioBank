/**
 * Created by gaokangkang on 2017/9/27.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanViewController', PlanViewController);

    PlanViewController.$inject = ['$scope','$compile','$state','$stateParams','toastr','$uibModal','DTOptionsBuilder','DTColumnBuilder','PlanService','BioBankBlockUi','BioBankDataTable'];

    function PlanViewController($scope,$compile,$state,$stateParams,toastr,$uibModal,DTOptionsBuilder,DTColumnBuilder,PlanService,BioBankBlockUi,BioBankDataTable) {
        var vm = this;
        vm.taskInstance = {};
        vm.plan = {};

        if($stateParams.planId){
            vm.planId = $stateParams.planId;
            PlanService.queryPlanSampleInfo(vm.planId).success(function (data) {
                vm.plan = data;
            }).then(function () {

            });
        }


        //任务列表
        vm.taskOptions = BioBankDataTable.buildDTOption("NORMALLY", 500, 5)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                PlanService.queryTaskList(vm.planId,data).then(function (res){
                    vm.selectAll = false;
                    vm.selected = {};
                    var json = res.data;
                    var error = json.error || json.sError;
                    if ( error ) {
                        jqDt._fnLog( oSettings, 0, error );
                    }
                    oSettings.json = json;
                    fnCallback( json );
                }).catch(function(res){
                    // console.log(res);
                    //
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
            .withOption('createdRow', createdRow);


        vm.taskColumns = [
            DTColumnBuilder.newColumn('stockOutTaskCode').withTitle('任务编码'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn('countOfFrozenBox').withTitle('冻存盒数').notSortable(),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本数'),
            DTColumnBuilder.newColumn('createDate').withTitle('创建日期'),
            DTColumnBuilder.newColumn('stockOutDate').withTitle('出库日期'),
            DTColumnBuilder.newColumn('operators').withTitle('操作员'),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];

        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '1601': status = '待出库';break;
                case '1602': status = '进行中';break;
                case '1603': status = '已出库';break;
                case '1604': status = '异常出库';break;
                case '1690': status = '已作废';break;
            }
            $('td:eq(1)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return  '<button  class="btn btn-xs" ng-click="vm.taskDescModal('+full.id+')"><i class="fa fa-eye"></i></button>&nbsp;' +
                '<button class="btn btn-xs" ng-if="'+full.status+'==1601" ng-click="vm.taskDelModal('+full.id+')"><i class="fa  fa-trash-o"></i></button>&nbsp;'+
                '<button class="btn btn-xs" ng-if="'+full.status+'==1601" ng-click="vm.startTask('+full.id+')"><i class="fa fa-play"></i></button>'
        }

    }

})();
