/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverDetailController', TakeOverDetailController);

    TakeOverDetailController.$inject = ['$scope','$state','$stateParams','$uibModal','$compile','DTOptionsBuilder','DTColumnBuilder','toastr',
        'TakeOverService','SampleUserService','StockOutService','entity','MasterData'];

    function TakeOverDetailController($scope,$state,$stateParams,$uibModal,$compile,DTOptionsBuilder,DTColumnBuilder,toastr,
                                      TakeOverService,SampleUserService,StockOutService,entity,MasterData) {
        var vm = this;
        var modalInstance;
        //样本交接Modal
        vm.takeOverModal = _fnTakeOverModal;

        vm.dto = {
            id: null,
            handoverCode: '',
            receiverName: '',
            receiverPhone: '',
            receiverOrganization: '',
            handoverPersonId: '',
            handoverPersonName: '',
            handoverTime: new Date(),
            status: '2101',
            memo: '',
            stockOutTaskId: 0,
            stockOutPlanId: 0,
            stockOutApplyId: 0
        };
        vm.application = null;
        var handoverStatus = MasterData.takeOverStatus;
        if (entity){
            vm.dto = entity;
            vm.dto.handoverTime = new Date(entity.handoverTime);
            for(var i = 0; i < handoverStatus.length; i++){
                if(entity.status == handoverStatus[i].id){
                    vm.statusName = handoverStatus[i].name;
                }
            }
            if(entity.status ){

            }
        }

        _initTakeoverEditors();
        _initFrozenBoxTable();


        function _initTakeoverEditors(){
            //接收人
            SampleUserService.query({},onReceiverSuccess, onError)
            function onReceiverSuccess(data) {
                vm.loginOptions = data;
            }
            //交付人
            vm.loginConfig = {
                valueField:'id',
                labelField:'userName',
                maxItems: 1
            };


            // 初始化出库申请下拉控件
            vm.applicationOptions = [];
            StockOutService.getApplications().then(function(res){
                vm.applicationOptions = res.data;

                if (vm.dto.stockOutApplyId){
                    vm.application = _.find(vm.applicationOptions, {id:vm.dto.stockOutApplyId});
                }
            }, onError);
            vm.applicationOptionsConfig = {
                valueField:'id',
                labelField:'applyCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.dto.stockOutPlanId = null;
                    vm.dto.stockOutTaskId = null;
                    if (value){
                        vm.application = _.find(vm.applicationOptions, {id:value});
                        StockOutService.getPlans(value).then(function (data){
                                vm.planOptions = data;
                                vm.taskOptions.length = 0;
                            }, onError
                        );
                        vm.stockOutApplyCode = (_.find(vm.applicationOptions, {id:value})||{}).applyCode;
                        if (vm.dtInstance && vm.dtInstance.rerender){
                            vm.dtInstance.rerender();
                        }
                    } else {
                        vm.planOptions.length = 0;
                        vm.taskOptions.length = 0;
                        vm.stockOutApplyCode = "";
                        vm.stockOutPlanCode = "";
                        vm.stockOutTaskCode = "";
                        vm.dtInstance.DataTable.clear().draw();
                    }

                }
            };
            // 初始化出库计划下拉控件
            vm.planOptions = [];
            vm.planOptionsConfig = {
                valueField:'id',
                labelField:'stockOutPlanCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.dto.stockOutTaskId = null;
                    if (value){
                        StockOutService.getTasks(value).then(function (data){
                                vm.taskOptions = data;
                            }, onError
                        );
                        vm.stockOutPlanCode = (_.find(vm.planOptions, {id:value})||{}).stockOutPlanCode;
                    } else {
                        vm.taskOptions.length = 0;
                        vm.stockOutPlanCode = "";
                        vm.stockOutTaskCode = "";
                    }
                }
            };

            vm.taskOptions = [];
            vm.taskOptionsConfig = {
                valueField:'id',
                labelField:'stockOutTaskCode',
                maxItems: 1,
                onChange:function (value) {
                    vm.dto.stockOutTaskId = null;
                    if (value){
                        vm.stockOutTaskCode = (_.find(vm.taskOptions, {id:value})||{}).stockOutTaskCode;
                    } else {
                        vm.stockOutTaskCode = "";
                    }
                }
            };

            vm.datePickerOpenStatus = {};
            vm.openCalendar = function (date) {
                vm.datePickerOpenStatus[date] = true;
            };

            vm.canBePrint = function(){
                if (vm.dto.handoverTime && vm.dto.handoverPersonId && vm.dto.receiverName && vm.dto.receiverPhone && vm.dto.receiverOrganization && vm.dto.stockOutApplyId){
                    for(var i in vm.selected){
                        if (vm.selected[i] == true){
                            return true;
                        }
                    }
                }

                return false;
            };
            vm.canBeTakeOver = function(){
                return vm.canBePrint();
            };

            vm.save = function (){
                TakeOverService.saveTakeoverInfo(vm.dto).then(function(res){
                    toastr.success("交接信息以保存!");
                    vm.dto = res.data;
                }, onError);
            };

            vm.print = function (){

            };


        }

        function _initFrozenBoxTable(){

            vm.dtInstance = {};
            vm.selectAllBox = false;
            vm.selected = {};

            // 全选或取消全选冻存盒
            vm.toggleAll = function (selectAll, selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            };

            // 更新全选选项
            vm.toggleOne = function (selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAllBox = false;
                            return;
                        }
                    }
                }
                vm.selectAllBox = true;
            };

            var titleHtml = '<input type="checkbox" ng-model="vm.selectAllBox" ng-click="vm.toggleAll(vm.selectAllBox, vm.selected)">';

            vm.dtOptions = DTOptionsBuilder.newOptions()
                .withDOM("t").withOption('scrollY', 280)
                .withOption('processing',true)
                .withOption('serverSide',true)
                .withOption('headerCallback', _fnCreatedHeader)
                .withOption('createdRow', _fnCreatedRow)
                .withFnServerData(_fnServerData)
            ;
            vm.dtColumns = [
                DTColumnBuilder.newColumn('').withTitle(titleHtml).withOption("width", "30").notSortable().renderWith(_fnActionsSelectHtml),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
                DTColumnBuilder.newColumn('status').withTitle('状态'),
                DTColumnBuilder.newColumn('position').withTitle('暂存区'),
                DTColumnBuilder.newColumn('applyCode').withTitle('出库申请'),
                DTColumnBuilder.newColumn('planCode').withTitle('出库计划'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('样本量'),
                DTColumnBuilder.newColumn('memo').withTitle('备注'),
                // DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "30").notSortable().renderWith(_fnActionsHtml)
                DTColumnBuilder.newColumn('delegate').withTitle('委托方'),

                DTColumnBuilder.newColumn('taskCode').notVisible(),
                DTColumnBuilder.newColumn('applyId').notVisible(),
                DTColumnBuilder.newColumn('planId').notVisible(),
                DTColumnBuilder.newColumn('taskId').notVisible(),

            ];

            var headerCompiled = false;
            function _fnCreatedHeader(header){
                if (!headerCompiled) {
                    // Use this headerCompiled field to only compile header once
                    headerCompiled = true;
                    $compile(angular.element(header).contents())($scope);
                }
            }
            function _fnCreatedRow(row, data, dataIndex) {
                var status = '';
                switch (data.status){
                    case '1001': status = '进行中';break;
                    case '1002': status = '已交接';break;
                }

                var samplQty = ' / 100';
                samplQty = (data.countOfSample || 0) + samplQty;
                $('td:eq(2)', row).html(status);
                $('td:eq(7)', row).html(samplQty);
                $compile(angular.element(row).contents())($scope);
            }
            function _fnActionsHtml(data, type, full, meta) {
                return '<button type="button" class="btn btn-default btn-xs" ui-sref="take-over-view({id:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;'
            }
            function _fnActionsSelectHtml(data, type, full, meta) {
                vm.selected[full.id] = false;
                var html = '';
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.id + '\']" ng-click="vm.toggleOne(vm.selected)">';
                return html;
            }
            function _fnServerData( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                if (!vm.dto.stockOutApplyId) {
                    data = {
                        "draw" : data.draw,
                        "recordsTotal" : 0,
                        "recordsFiltered" : data.length,
                        "data": []
                    };
                    oSettings.json = data;
                    fnCallback(data);
                    return;
                }
                _.each(data.columns, function(col){
                    switch (col.data){
                        case "applyId":
                            col.search.value = col.search.value || vm.dto.stockOutApplyId || "";
                            break;
                        case "planId":
                            col.search.value = col.search.value || vm.dto.stockOutPlanId || "";
                            break;
                        case "taskId":
                            col.search.value = col.search.value || vm.dto.stockOutTaskId || "";
                            break;
                    }
                });
                var jqDt = this;
                TakeOverService.queryWaitingTakeOverFrozenBoxesList(vm.dto.stockOutApplyId, data, oSettings).then(function (res){
                    var json = res.data;
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

            vm.searchBox = function(pos, boxCode){
                var table = vm.dtInstance.DataTable;
                table
                    .column(1)
                    .search(boxCode)
                    .column(3)
                    .search(pos)
                    .draw();

                vm.filterPos = pos;
                vm.filterBoxCode = boxCode;
            };

        }


        function _fnTakeOverModal(){
            var table = vm.dtInstance.DataTable;
            var boxes = [];

            table.data().each( function (d) {
                for (var i in vm.selected){
                    if (vm.selected[i]){
                        boxes.push(angular.clone(d));
                    }
                }
            });

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/take-over/modal/take-over-modal.html',
                controller: 'TakeOverModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    stockOutTakeOver: vm.dto,
                    stockOutApplication: vm.application,
                    stockOutBoxes: boxes
                }
            });

            modalInstance.result.then(function (data) {
                $state.go("take-over-list");
            });
        }

        function onError(error) {
            toastr.error(error.message);
        }
    }
})();
