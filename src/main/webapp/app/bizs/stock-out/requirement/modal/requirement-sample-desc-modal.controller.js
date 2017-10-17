/**
 * Created by gaokangkang on 2017/5/12.
 * 申请样本库存核对详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementSampleDescModalController', RequirementSampleDescModalController);

    RequirementSampleDescModalController.$inject = ['$scope','$uibModalInstance','$compile','$uibModal','items','DTOptionsBuilder','DTColumnBuilder','RequirementService','BioBankDataTable'];

    function RequirementSampleDescModalController($scope,$uibModalInstance,$compile,$uibModal,items,DTOptionsBuilder,DTColumnBuilder,RequirementService,BioBankDataTable) {
        var vm = this;
        var sampleRequirementId = items.sampleRequirementId;
        //打印详情
        vm.printRequirementDesc = _fnPrintRequirementDesc;
        function _fnPrintRequirementDesc() {
            window.open ('/api/stock-out-requirements/print/' + sampleRequirementId);
        }
        function _initSampleRequirementDesc() {
            RequirementService.descSampleRequirement(sampleRequirementId).success(function (data) {
                vm.requirementDesc = data;
            }).error(function (data) {
            });
            // RequirementService.descSampleList(sampleRequirementId).success(function (data) {
                // if(vm.requirementDesc.sex == "M"){
                //     vm.requirementDesc.sex = "男";
                // } else if(vm.requirementDesc.sex == "F"){
                //     vm.requirementDesc.sex = "女";
                // }else{
                //     vm.requirementDesc.sex = "不详";
                // }
                // //正常的样本
                // vm.len = _.filter(vm.requirementDesc.frozenTubeList,{status:'3001'}).length;
                // setTimeout(function () {
                //     vm.dtOptions.withOption('data', vm.requirementDesc.frozenTubeList);
                // },500);
            // })
        }
        _initSampleRequirementDesc();

        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY")
            .withOption('createdRow', createdRow)
            .withOption('serverSide',true)
            .withFnServerData(_fnServerData);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('一维编码'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('sex').withTitle('性别'),
            DTColumnBuilder.newColumn('age').withTitle('年龄'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码'),
            DTColumnBuilder.newColumn('diseaseTypeId').withTitle('疾病'),
            DTColumnBuilder.newColumn('sampleUsedTimes').withTitle('使用次数'),
            DTColumnBuilder.newColumn('memo').withTitle('批注')

        ];
        function createdRow(row, data, dataIndex) {
            var sampleState = '';
            var diseaseType = '';
            var sex = '';
            switch (data.sex){
                case 'M': sex = '男';break;
                case 'F': sex = '女';break;
                case 'null': sex = '不详';break;
            }
            switch (data.status){
                case '3001': sampleState = '正常';break;
                case '3002': sampleState = '空管';break;
                case '3003': sampleState = '空孔';break;
                case '3004': sampleState = '异常';break;
            }
            switch (data.diseaseTypeId){
                case 1: diseaseType = 'AMI';break;
                case 2: diseaseType = 'PCI';break;
                case 3: diseaseType = '不详';break;
            }
            if(data.isBloodLipid){
                diseaseType += "脂质血　";
            }
            if(data.isHemolysis){
                diseaseType += "溶血　";
            }
            $('td:eq(2)', row).html(sampleState);
            $('td:eq(4)', row).html(sex);
            $('td:eq(7)', row).html(diseaseType);
            $compile(angular.element(row).contents())($scope);
        }
        function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
            var data = {};
            for(var i=0; aoData && i<aoData.length; ++i){
                var oData = aoData[i];
                data[oData.name] = oData.value;
            }
            var jqDt = this;
            RequirementService.descSampleList(sampleRequirementId, data, oSettings).then(function (res){
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
        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
