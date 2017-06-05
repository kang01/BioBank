/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskViewController', TaskViewController);

    TaskViewController.$inject = ['$scope','$compile','$stateParams','$uibModal','hotRegisterer','$timeout','DTOptionsBuilder','DTColumnBuilder','toastr',
        'TaskService','SampleUserService','MasterData','BioBankBlockUi','SampleService','entity'];

    function TaskViewController($scope,$compile,$stateParams,$uibModal,hotRegisterer,$timeout,DTOptionsBuilder,DTColumnBuilder,toastr,
                                TaskService,SampleUserService,MasterData,BioBankBlockUi,SampleService,entity) {
        var vm = this;
        var modalInstance;
        vm.task = entity;

        //已出库列表
        vm.taskStockOutModal = _fnTaskStockOutModal;
        vm.stockOutSampleInstance = {};
        vm.stockOutSampleOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('createdRow', createdRow)
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        // var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.stockOutSampleColumns = [
            // DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('stockOutHandoverTime').withTitle('出库交接时间'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('盒内样本量'),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
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




        function actionsHtml(data, type, full, meta) {
            return '<button type="button" ng-disabled="!vm.task.stockOutHeadId1 || !vm.task.stockOutHeadId2" class="btn btn-warning btn-sm" ng-if="'+full.status+'== 1701" ng-click="vm.taskStockOutModal('+ full.id +')">' +
                '出库' +
                '</button> &nbsp;'+
            '<button type="button" class="btn btn-warning btn-sm"  ng-click="vm.commentModal(2,'+ full.id +')">' +
                '批注' +
                '</button>'
        }
        //出库
        function _fnTaskStockOutModal(frozenBoxIds) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/task/modal/task-stock-out-modal.html',
                controller: 'TaskStockOutModalController',
                controllerAs: 'vm',
                size: '90',
                resolve: {
                    items: function () {
                        return {
                            frozenBoxIds:frozenBoxIds || vm.strBoxIds,
                            taskId:vm.taskId
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {
                _fnQueryStockOutList();
            });
        }

        function _fnQueryStockOutList() {
            //获取已出库列表
            TaskService.queryOutputList(vm.taskId).success(function (data) {
                vm.stockOutSampleOptions.withOption('data', data);
                vm.stockOutSampleInstance.rerender();
            })
        }
        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }





        //未出库冻存盒列表
        vm.boxOptions = DTOptionsBuilder.newOptions()
            .withPaginationType('full_numbers')
            .withOption('info', false)
            .withOption('paging', false)
            .withOption('sorting', false)
            .withScroller()
            .withOption('scrollY', 398)
            .withOption('rowCallback', rowCallback);
        vm.boxColumns = [
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "50"),
            DTColumnBuilder.newColumn('sampleClassificationName').withTitle('样本分类').withOption("width", "50"),
            DTColumnBuilder.newColumn('position').withTitle('冻存盒位置'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('出库样本量').withOption("width", "50"),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
        ];
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td:eq(8)', nRow).html(MasterData.getStatus(aData.status));
            $compile(angular.element(nRow).contents())($scope);
        }

    }
})();
