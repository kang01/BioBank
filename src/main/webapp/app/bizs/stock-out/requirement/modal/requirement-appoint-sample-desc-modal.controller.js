/**
 * Created by gaokangkang on 2017/8/3.
 * 指定样本详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementAppointSampleDescModalController', RequirementAppointSampleDescModalController);

    RequirementAppointSampleDescModalController.$inject = ['$scope','$uibModalInstance','$compile','$uibModal','items','DTOptionsBuilder','DTColumnBuilder','RequirementService','BioBankDataTable'];

    function RequirementAppointSampleDescModalController($scope,$uibModalInstance,$compile,$uibModal,items,DTOptionsBuilder,DTColumnBuilder,RequirementService,BioBankDataTable) {
        var vm = this;
        //样本需求ID
        var sampleRequirementId = items.sampleRequirementId;

        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY")
            .withOption('serverSide',true)
            .withFnServerData(_fnServerData);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型')

        ];
        function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            RequirementService.appointDescSampleRequirement(sampleRequirementId, data, oSettings).then(function (res){
                var json = res.data;
                vm.len = _.filter(res.data.data,{status:'3001'}).length;
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

        // vm.ok = function () {
        //     $uibModalInstance.close();
        // };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
