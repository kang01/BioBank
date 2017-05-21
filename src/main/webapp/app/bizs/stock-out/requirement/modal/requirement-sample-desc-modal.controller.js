/**
 * Created by gaokangkang on 2017/5/12.
 * 申请样本库存详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementSampleDescModalController', RequirementSampleDescModalController);

    RequirementSampleDescModalController.$inject = ['$scope','$uibModalInstance','$compile','$uibModal','items','DTOptionsBuilder','DTColumnBuilder','RequirementService'];

    function RequirementSampleDescModalController($scope,$uibModalInstance,$compile,$uibModal,items,DTOptionsBuilder,DTColumnBuilder,RequirementService) {
        var vm = this;
        var sampleRequirementId = items.sampleRequirementId;
        function _initSampleRequirementDesc() {
            RequirementService.descSampleRequirement(sampleRequirementId).success(function (data) {
                vm.requirementDesc = data;
            }).error(function (data) {
            })
        }
        _initSampleRequirementDesc();

        vm.dtOptions = DTOptionsBuilder.newOptions()
            .withOption('processing',true)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                RequirementService.queryRequirementList(data, oSettings).then(function (res){
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

        vm.dtColumns = [
            DTColumnBuilder.newColumn('projectSiteCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('projectCode').withTitle('状态'),
            DTColumnBuilder.newColumn('transhipDate').withTitle('样本类型'),
            DTColumnBuilder.newColumn('receiver').withTitle('性别'),
            DTColumnBuilder.newColumn('receiveDate').withTitle('年龄'),
            DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('项目编码'),
            DTColumnBuilder.newColumn('transhipState').withTitle('标签'),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
        ];
        function createdRow(row, data, dataIndex) {
            var transhipState = '';
            var sampleSatisfaction = '';
            switch (data.transhipState){
                case '1001': transhipState = '进行中';break;
                case '1002': transhipState = '待入库';break;
                case '1003': transhipState = '已入库';break;
                case '1004': transhipState = '已作废';break;
            }
            switch (data.sampleSatisfaction){
                case 1: sampleSatisfaction = '非常不满意';break;
                case 2: sampleSatisfaction = '有大量错位';break;
                case 3: sampleSatisfaction = '有少量错位';break;
                case 4: sampleSatisfaction = '有少量空孔';break;
                case 5: sampleSatisfaction = '有大量空管';break;
                case 6: sampleSatisfaction = '有许多空管';break;
                case 7: sampleSatisfaction = '有少量空管';break;
                case 8: sampleSatisfaction = '满意';break;
                case 9: sampleSatisfaction = '较满意';break;
                case 10: sampleSatisfaction = '非常满意';break;
            }
            $('td:eq(5)', row).html(sampleSatisfaction);
            $('td:eq(6)', row).html(transhipState);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-warning" ui-sref="transport-record-edit({id:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;'
        }

        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
