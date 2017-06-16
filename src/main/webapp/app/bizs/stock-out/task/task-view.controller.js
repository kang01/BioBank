/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskViewController', TaskViewController);

    TaskViewController.$inject = ['$scope','$state','$compile','$stateParams','$uibModal','hotRegisterer','$timeout','DTOptionsBuilder','DTColumnBuilder','toastr','BioBankDataTable',
        'TaskService','SampleUserService','MasterData','BioBankBlockUi','SampleService','entity'];

    function TaskViewController($scope, $state,$compile,$stateParams,$uibModal,hotRegisterer,$timeout,DTOptionsBuilder,DTColumnBuilder,toastr,BioBankDataTable,
                                TaskService,SampleUserService,MasterData,BioBankBlockUi,SampleService,entity) {
        var vm = this;
        var modalInstance;
        vm.task = entity.data;
        vm.usedTime = (vm.task.usedTime/60).toFixed(1);
        if(vm.usedTime < 1){
            vm.usedTime = "小于1小时"
        }else{
            vm.usedTime = vm.usedTime + "小时"
        }
        vm.status  = MasterData.getStatus(vm.task.status);

        vm.takeOver = _fnTakeOver;
        function _fnTakeOver() {
            var obj = {
                applyId:vm.task.stockOutApplyId,
                planId:vm.task.stockOutPlanId,
                taskId:vm.task.id
            };
            $state.go('take-over-new',obj);
            // BioBankBlockUi.blockUiStart();
            // TaskService.takeOver(vm.taskId).success(function (data) {
            //     BioBankBlockUi.blockUiStop();
            //     toastr.success("创建交接单成功!");
            //     $state.go('take-over-edit', {id: data});
            // }).error(function (data) {
            //     toastr.error("创建交接单失败!");
            //     BioBankBlockUi.blockUiStop();
            // })
        }

        //已出库列表
        vm.stockOutSampleInstance = {};
        vm.stockOutSampleOptions = BioBankDataTable.buildDTOption("BASIC", 250, null)
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        // var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            // DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码').withOption("width", "120"),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置').withOption("width", "240"),
            DTColumnBuilder.newColumn('stockOutHandoverTime').withTitle('交接时间').withOption("width", "80"),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", "auto"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "60"),
            // DTColumnBuilder.newColumn(null).withTitle('操作').notSortable().renderWith(actionsHtml),
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            vm.selectAll = false;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }
        function createdRow(row, data, dataIndex) {
            var status = MasterData.getStatus(data.status);
            $('td:eq(6)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }

        //未出库冻存盒列表
        vm.boxOptions = BioBankDataTable.buildDTOption("BASIC", 250, null)
            .withOption('rowCallback', rowCallback);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "120"),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption("width", "120"),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassificationName').withTitle('样本分类').withOption("width", "60"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置').withOption("width", "240"),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", "auto"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
        ];
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td:eq(7)', nRow).html(MasterData.getStatus(oData.status));
            $compile(angular.element(nRow).contents())($scope);
        }

        //获取未出库冻存盒列表
        TaskService.queryTaskBox(vm.task.id).success(function (data) {
            vm.boxOptions.withOption('data', data);
            // vm.boxInstance.rerender();
        });

        //获取已出库冻存盒列表
        TaskService.queryOutputList(vm.task.id).success(function (data) {
            vm.stockOutSampleOptions.withOption('data', data);
            vm.stockOutSampleInstance.rerender();
        });
    }
})();
