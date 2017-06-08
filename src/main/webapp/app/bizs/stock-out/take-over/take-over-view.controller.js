/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverViewController', TakeOverViewController);

    TakeOverViewController.$inject = ['$scope','$compile','$uibModal','entity','TakeOverService','DTOptionsBuilder','DTColumnBuilder'];

    function TakeOverViewController($scope,$compile,$uibModal,entity,TakeOverService,DTOptionsBuilder,DTColumnBuilder) {
        var vm = this;

        vm.stockOutTakeOver = entity.data;
        setTimeout(function () {
            vm.stockOutSampleOptions.withOption('data',entity.data.handoverFrozenTubes);

        },500);
        // TakeOverService.queryTakeOverView(vm.stockOutTakeOver.id).success(function (data) {
        //    console.log(JSON.stringify(data))
        //     vm.stockOutTakeOver = data;
        //     vm.stockOutSampleOptions.withOption('data',data.handoverFrozenTubes);
        // });

        //已交接样本
        vm.stockOutSampleOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            // .withOption('info', false)
            // .withOption('paging', false)
            // .withOption('sorting', false)
            // .withScroller()
            // .withOption('scrollY', 300)
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            DTColumnBuilder.newColumn('id').withTitle('No'),
            DTColumnBuilder.newColumn('boxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('location').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('sampleType').withTitle('类型'),
            DTColumnBuilder.newColumn('sex').withTitle('性别'),
            DTColumnBuilder.newColumn('age').withTitle('年龄'),
            DTColumnBuilder.newColumn('diseaseType').withTitle('疾病'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码')
        ];

        vm.takeOverPrint = function () {
            window.open ('/api/stock-out-handovers/print/' + vm.stockOutTakeOver.id);
        };


    }
})();
