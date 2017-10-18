/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverViewController', TakeOverViewController);

    TakeOverViewController.$inject = ['$scope','$compile','$uibModal','entity','TakeOverService','DTOptionsBuilder','DTColumnBuilder','BioBankDataTable'];

    function TakeOverViewController($scope,$compile,$uibModal,entity,TakeOverService,DTOptionsBuilder,DTColumnBuilder,BioBankDataTable) {
        var vm = this;

        vm.stockOutTakeOver = entity;

        //已交接样本
        vm.stockOutSampleOptions = BioBankDataTable.buildDTOption("NORMALLY", 0,10)
            .withOption('processing',true)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                TakeOverService.queryTakeOverView(vm.stockOutTakeOver.id,data).then(function (res){
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
            .withPaginationType('full_numbers')
            .withOption('createdRow', createdRow);

        vm.stockOutSampleColumns = [
            // DTColumnBuilder.newColumn('id').withTitle('No').withOption('width', '30'),
            DTColumnBuilder.newColumn('boxCode').withTitle('临时盒编码').withOption('width', '120'),
            DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('一维编码').withOption('width', '120'),
            DTColumnBuilder.newColumn('location').withTitle('盒内位置').withOption('width', '80'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption('width', '120'),
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码').withOption('width', '140'),
            DTColumnBuilder.newColumn('sampleType').withTitle('类型').withOption('width', '80'),
            DTColumnBuilder.newColumn('sex').withTitle('性别').withOption('width', '50'),
            DTColumnBuilder.newColumn('age').withTitle('年龄').withOption('width', '50'),
            DTColumnBuilder.newColumn('diseaseType').withTitle('疾病').withOption('width', '120'),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption('width', 'auto')
            // DTColumnBuilder.newColumn('memo').withTitle('备注').withOption('width', 'auto')
        ];
        function createdRow(row, data, dataIndex) {
            var sex = '';
            switch (data.sex){
                case 'M': sex = '男';break;
                case 'F': sex = '女';break;
                case 'null': sex = '不详';break;
            }
            // var planStatus = '';
            // switch (data.status){
            //     case '1401': planStatus = '进行中';break;
            //     case '1402': planStatus = '已完成';break;
            //     case '1490': planStatus = '已作废';break;
            // }
            $('td:eq(6)', row).html(sex);
            $compile(angular.element(row).contents())($scope);
        }



        vm.takeOverPrint = function () {
            window.open ('/api/stock-out-handovers/print/' + vm.stockOutTakeOver.id);
        };


    }
})();
