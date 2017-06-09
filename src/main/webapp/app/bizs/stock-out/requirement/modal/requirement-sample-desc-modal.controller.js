/**
 * Created by gaokangkang on 2017/5/12.
 * 申请样本库存详情
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

                setTimeout(function () {
                    vm.dtOptions.withOption('data', vm.requirementDesc.frozenTubeList);
                },500)
            }).error(function (data) {
            })
        }
        _initSampleRequirementDesc();

        vm.dtOptions = BioBankDataTable.buildDTOption("BASIC",302)
            .withOption('createdRow', createdRow);

        vm.dtColumns = [
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
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
                diseaseType += "脂质血　"
            }
            if(data.isHemolysis){
                diseaseType += "溶血　"
            }
            $('td:eq(1)', row).html(sampleState);
            $('td:eq(6)', row).html(diseaseType);
            $compile(angular.element(row).contents())($scope);
        }

        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
