/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskViewController', TaskViewController);

    TaskViewController.$inject = ['$scope','$state','$compile','$stateParams','$uibModal','hotRegisterer','$timeout','DTOptionsBuilder','DTColumnBuilder','toastr',
        'TaskService','SampleUserService','MasterData','BioBankBlockUi','SampleService','entity'];

    function TaskViewController($scope, $state,$compile,$stateParams,$uibModal,hotRegisterer,$timeout,DTOptionsBuilder,DTColumnBuilder,toastr,
                                TaskService,SampleUserService,MasterData,BioBankBlockUi,SampleService,entity) {
        var vm = this;
        var modalInstance;
        vm.task = entity;

        vm.takeOver = _fnTakeOver;
        function _fnTakeOver() {
            BioBankBlockUi.blockUiStart();
            TaskService.takeOver(vm.taskId).success(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.success("创建交接单成功!");
                $state.go('take-over-edit', {id: data});
            }).error(function (data) {
                toastr.error("创建交接单失败!");
                BioBankBlockUi.blockUiStop();
            })
        };

        //已出库列表
        vm.stockOutSampleInstance = {};
        vm.stockOutSampleOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('scrollY', 250)
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
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('stockOutHandoverTime').withTitle('出库交接时间'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
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
        vm.boxOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('scrollY', 250)
            .withOption('rowCallback', rowCallback);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "120"),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption("width", "120"),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassificationName').withTitle('样本分类').withOption("width", "60"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
        ];
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td:eq(8)', nRow).html(MasterData.getStatus(aData.status));
            $compile(angular.element(nRow).contents())($scope);
        }

        //获取未出库冻存盒列表
        TaskService.queryTaskBox(vm.taskId).success(function (data) {
            vm.boxOptions.withOption('data', data);
            vm.boxInstance.rerender();
        });

        //获取已出库冻存盒列表
        TaskService.queryOutputList(vm.taskId).success(function (data) {
            vm.stockOutSampleOptions.withOption('data', data);
            vm.stockOutSampleInstance.rerender();
        })
    }
})();
