/**
 * Created by gaokangkang on 2017/5/12.
 * 计划中出库任务列表详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanTaskDescModalController', PlanTaskDescModalController);

    PlanTaskDescModalController.$inject = ['$uibModalInstance','$uibModal','items','DTColumnBuilder','DTOptionsBuilder','PlanService','BioBankDataTable'];

    function PlanTaskDescModalController($uibModalInstance,$uibModal,items,DTColumnBuilder,DTOptionsBuilder,PlanService,BioBankDataTable) {
        var vm = this;
        var taskId = items.taskId;
        //盒子列表
        vm.dtOptions =  BioBankDataTable.buildDTOption("NORMALLY", 250, 8)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;

                PlanService.queryTaskBoxesDes(taskId,data).then(function (res){
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
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码'),
            DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('一维编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('position').withTitle('冻存位置'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('出库样本数量').notSortable(),
            DTColumnBuilder.newColumn('id').notVisible()
        ];

        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
