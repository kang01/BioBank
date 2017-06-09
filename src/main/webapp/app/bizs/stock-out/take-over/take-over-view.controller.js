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

        vm.stockOutTakeOver = entity.data;

        //已交接样本
        vm.stockOutSampleOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('data',vm.stockOutTakeOver.handoverFrozenTubes);
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            DTColumnBuilder.newColumn('id').withTitle('No').withOption('width', '30'),
            DTColumnBuilder.newColumn('boxCode').withTitle('临时盒编码').withOption('width', '120'),
            DTColumnBuilder.newColumn('location').withTitle('盒内位置').withOption('width', '80'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption('width', '120'),
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码').withOption('width', '120'),
            DTColumnBuilder.newColumn('sampleType').withTitle('类型').withOption('width', '80'),
            DTColumnBuilder.newColumn('sex').withTitle('性别').withOption('width', '30'),
            DTColumnBuilder.newColumn('age').withTitle('年龄').withOption('width', '30'),
            DTColumnBuilder.newColumn('diseaseType').withTitle('疾病').withOption('width', 'auto'),
        ];

        vm.takeOverPrint = function () {
            window.open ('/api/stock-out-handovers/print/' + vm.stockOutTakeOver.id);
        };


    }
})();
