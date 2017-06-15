/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverDetailController', TakeOverDetailController)
        .controller('TakeOverCancellationModalController', TakeOverCancellationModalController);

    TakeOverDetailController.$inject = ['$scope','$state','$stateParams','$uibModal','$compile','DTOptionsBuilder','DTColumnBuilder','toastr','BioBankDataTable',
        'TakeOverService','SampleUserService','StockOutService','entity','MasterData'];
    TakeOverCancellationModalController.$inject = ['$uibModalInstance'];
    function TakeOverDetailController($scope,$state,$stateParams,$uibModal,$compile,DTOptionsBuilder,DTColumnBuilder,toastr,BioBankDataTable,
                                      TakeOverService,SampleUserService,StockOutService,entity,MasterData) {
        var vm = this;
        var modalInstance;
        //样本交接Modal
        vm.takeOverModal = _fnTakeOverModal;
        //作废
        vm.cancellation = _fnCancellation;

        var applyId = $stateParams.applyId;
        var planId = $stateParams.planId;
        var taskId = $stateParams.taskId;
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
            if(entity.handoverTime){
                vm.dto.handoverTime = new Date(entity.handoverTime);
            }else{
                vm.dto.handoverTime = new Date();
            }
            vm.statusName = MasterData.getStatus(entity.status);
            if(entity.stockOutApplyId){
                _fnGetPlans(entity.stockOutApplyId);
            }
            if(entity.stockOutPlanId){
                _fnGetTasks(entity.stockOutPlanId);
            }
        }

        _initTakeoverEditors();
        _initFrozenBoxTable();
        //出库计划
        function _fnGetPlans(value) {
            if (value){
                vm.application = _.find(vm.applicationOptions, {id:value});
                StockOutService.getPlans(value).then(function (res){
                    vm.planOptions = res.data;
                    if(vm.planOptions.length){
                        vm.dto.stockOutPlanId = vm.planOptions[0].id;
                    }
                    // vm.taskOptions.length = 0;
                    if(vm.dto.stockOutPlanId){
                        _fnGetTasks(vm.dto.stockOutPlanId);
                    }
                }, onError);
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
        //出库任务
        function _fnGetTasks(value) {
            if (value){
                StockOutService.getTasks(value).then(function (res){
                        vm.taskOptions = res.data;
                        if(vm.taskOptions.length){
                            vm.dto.stockOutTaskId = vm.taskOptions[0].id;
                        }
                        // $scope.$apply();
                    }, onError
                );
                vm.stockOutPlanCode = (_.find(vm.planOptions, {id:value})||{}).stockOutPlanCode;
            } else {
                vm.taskOptions.length = 0;
                vm.stockOutPlanCode = "";
                vm.stockOutTaskCode = "";
            }
        }

        function _initTakeoverEditors(){
            //交付人
            SampleUserService.query({},onReceiverSuccess, onError);
            function onReceiverSuccess(data) {
                vm.loginOptions = data;
                if(entity.handoverPersonId){
                    vm.dto.handoverPersonName = _.filter(vm.loginOptions,{id:+entity.handoverPersonId})[0].userName;
                }
            }
            //交付人
            vm.loginConfig = {
                valueField:'id',
                labelField:'userName',
                maxItems: 1,
                onChange:function (value) {
                    vm.dto.handoverPersonName = _.filter(vm.loginOptions,{id:+value})[0].userName;
                }
            };


            // 初始化出库申请下拉控件
            vm.applicationOptions = [];
            StockOutService.getApplications().then(function(res){
                vm.applicationOptions = res.data;
                if(!vm.dto.stockOutApplyId){
                    vm.dto.stockOutApplyId = vm.applicationOptions[0].id;
                }
                if(applyId){
                    vm.dto.stockOutApplyId = applyId;
                    _fnGetPlans(applyId);
                }
                if(planId){
                    _fnGetTasks(planId);
                }
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
                    _fnGetPlans(value);

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
                    _fnGetTasks(value);

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
            vm.takeOverPrint = function () {
                window.open ('/api/stock-out-handovers/print/' + vm.dto.id);
            };
            vm.save = function (){
                TakeOverService.saveTakeoverInfo(vm.dto).then(function(res){
                    vm.dto = res.data;
                    vm.dto.handoverTime = new Date(res.data.handoverTime);
                    vm.statusName = MasterData.getStatus(res.data.status);
                    if(!takeOverFlag){
                        toastr.success("交接信息已保存成功!");
                    }else{
                        modalInstance = $uibModal.open({
                            animation: true,
                            templateUrl: 'app/bizs/stock-out/take-over/modal/take-over-modal.html',
                            controller: 'TakeOverModalController',
                            controllerAs:'vm',
                            size:'lg',
                            resolve: {
                                items:{
                                    stockOutTakeOver: angular.copy(vm.dto),
                                    stockOutApplication: vm.application,
                                    stockOutBoxes: null,
                                    boxIdsStr:boxIdsStr
                                }

                            }
                        });
                        modalInstance.result.then(function (data) {
                            takeOverFlag = false;
                            $state.go("take-over-list");
                        },function (data) {
                            takeOverFlag = false;
                        });
                    }

                }, onError);
            };

            vm.print = function (){

            };


        }
        var boxIds=[];
        var boxIdsStr;
        function _initFrozenBoxTable(){

            vm.dtInstance = {};
            vm.selectAllBox = false;
            vm.selected = {};

            // 全选或取消全选冻存盒
            vm.toggleAll = function (selectAll, selectedItems) {
                boxIds = [];
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                        if(selectedItems[id]){
                            boxIds.push(id);
                            boxIdsStr = boxIds.join(",");
                        }
                    }
                }
            };

            // 更新全选选项
            vm.toggleOne = function (selectedItems) {
                boxIds = [];
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAllBox = false;
                            return;
                        }else{
                            boxIds.push(id);
                            boxIdsStr = boxIds.join(",");
                        }
                    }
                }
                vm.selectAllBox = true;
            };

            var titleHtml = '<input type="checkbox" ng-model="vm.selectAllBox" ng-click="vm.toggleAll(vm.selectAllBox, vm.selected)">';

            vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10,  "tr<'row'<'col-sm-5'i><'col-sm-7'p>>")
                .withOption('order',[[1,'asc']])
                .withOption('serverSide',true)
                .withOption('headerCallback', _fnCreatedHeader)
                .withOption('createdRow', _fnCreatedRow)
                .withFnServerData(_fnServerData)
            ;
            vm.dtColumns = [
                DTColumnBuilder.newColumn('').withTitle(titleHtml).withOption("width", "30").notSortable().renderWith(_fnActionsSelectHtml),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码').withOption("width", 120),
                DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", 50),
                DTColumnBuilder.newColumn('position').withTitle('暂存区').withOption("width", 120),
                DTColumnBuilder.newColumn('applyCode').withTitle('出库申请').withOption("width", 120),
                DTColumnBuilder.newColumn('planCode').withTitle('出库计划').withOption("width", 120),
                DTColumnBuilder.newColumn('countOfSample').withTitle('样本量').withOption("width", 100),
                DTColumnBuilder.newColumn('delegate').withTitle('委托方').withOption("width", 250),
                DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", "auto"),
                // DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "30").notSortable().renderWith(_fnActionsHtml)

                DTColumnBuilder.newColumn('taskCode').notVisible(),
                DTColumnBuilder.newColumn('applyId').notVisible(),
                DTColumnBuilder.newColumn('planId').notVisible(),
                DTColumnBuilder.newColumn('taskId').notVisible()

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
                var status = MasterData.getStatus(data.status);
                var samplQty = ' / 100';
                samplQty = (data.countOfSample || 0) + samplQty;
                $('td:eq(2)', row).html(status);
                $('td:eq(6)', row).html(samplQty);
                $compile(angular.element(row).contents())($scope);
            }
            function _fnActionsHtml(data, type, full, meta) {
                return '<button type="button" class="btn btn-default btn-xs" ui-sref="take-over-view({id:'+ full.id +'})">' +
                    '   <i class="fa fa-eye"></i>' +
                    '</button>&nbsp;';
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
                    .search(boxCode||'')
                    .column(3)
                    .search(pos||'')
                    .draw();

                vm.filterPos = pos;
                vm.filterBoxCode = boxCode;
            };

        }

        var takeOverFlag = false;
        function _fnTakeOverModal(){
            takeOverFlag = true;
            vm.save();

        }

        function _fnCancellation() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/take-over/modal/take-over-cancellation-modal.html',
                controller: 'TakeOverCancellationModalController',
                controllerAs:'vm',
                size:'lg'
            });
            modalInstance.result.then(function (reason) {
                var invalid = {};
                invalid.invalidReason = reason;
                TakeOverService.invalidTakeOver(vm.dto.id,invalid).success(function (data) {
                   toastr.success("作废成功!");
                    $state.go("take-over-list");
                });
            });
        }

        function onError(error) {
            toastr.error(error.message);
        }
    }
    function TakeOverCancellationModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
