/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanDetailController', PlanDetailController)
        .controller('PlanDelModalController', PlanDelModalController);

    PlanDetailController.$inject = ['$scope','$compile','$state','$stateParams','toastr','$uibModal','DTOptionsBuilder','DTColumnBuilder','PlanService','BioBankBlockUi','BioBankDataTable'];
    PlanDelModalController.$inject = ['$uibModalInstance'];
    function PlanDetailController($scope,$compile,$state,$stateParams,toastr,$uibModal,DTOptionsBuilder,DTColumnBuilder,PlanService,BioBankBlockUi,BioBankDataTable) {
        var vm = this;
        var modalInstance;
        vm.dtInstance = {};
        vm.tubeInstance = {};
        vm.taskInstance = {};
        vm.plan = {};


        //查看出库任务列表详情
        vm.taskDescModal = _fnTaskDescModal;
        //删除任务
        vm.taskDelModal = _fnTaskDelModal;
        //开始任务
        vm.startTask = _fnStartTask;
        //创建任务
        vm.createTask = _fnCreateTask;

        var applyId;
        if($stateParams.planId){
            vm.planId = $stateParams.planId;
            PlanService.queryPlanSampleInfo(vm.planId).success(function (data) {
                vm.plan = data;
                applyId = data.id;
                angular.forEach(vm.plan.stockOutRequirement, function (data, index) {
                    vm.checked.push(data.id);
                    vm.demand[data.id] = true;
                    vm.sampleIds = vm.checked.join(",");
                    _queryPlanBoxes();
                    PlanService.queryPlanBoxes(vm.sampleIds,obj).success(function (res){
                        vm.boxData = res.data;
                        vm.dtOptions.withOption('data',vm.boxData);
                    });
                });
                vm.select_all = true;
            }).then(function () {

            });
            var obj = {
                draw:1,
                length:-1
            };

        }

        _initStockOutBoxesTable();
        function _initStockOutBoxesTable() {
            vm.selected = {};
            vm.selectAll = false;
            // 处理盒子选中状态
            vm.toggleAll = function (selectAll, selectedItems) {
                selectedItems = vm.selected;
                selectAll = vm.selectAll;
                var arrayId = [];
                for (var id in selectedItems) {
                    arrayId.push(id);
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
                vm.strBoxIds = _.join(arrayId,",");
                if(!selectAll){
                    vm.strBoxIds = "";
                }
            };
            var clickFlag = false;
            vm.toggleOne = function (selectedItems) {
                clickFlag = true;
                // console.log(JSON.stringify(selectedItems))
                var arrayId = [];
                for (var id in selectedItems) {
                    if(selectedItems[id]){
                        arrayId.push(id);
                    }
                }
                vm.strBoxIds = _.join(arrayId,",");
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAll = false;
                            return;
                        }
                    }
                }
                vm.selectAll = true;


            };
            //盒子列表
            var boxId;
            // vm.boxData = [];
            // vm.dtOptions.withOption('data',vm.boxData);
            vm.dtOptions = BioBankDataTable.buildDTOption("SORTING", 300, 5)
                .withOption('order', [[1,'asc']])
                .withButtons([
                    {
                        text: '创建任务',
                        className: 'btn btn-primary btn-sm ml-10',
                        key: '1',
                        action: _fnCreateTask
                    }
                ])
                .withOption('rowCallback', rowCallback)
                .withOption('createdRow', function(row, data, dataIndex) {
                    $compile(angular.element(row).contents())($scope);
                })
                .withOption('headerCallback', function(header) {
                    $compile(angular.element(header).contents())($scope);
                });

            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

            vm.dtColumns = [
                // DTColumnBuilder.newColumn('id').notVisible(),
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('冻存盒编码').withOption("width", "90").renderWith(_fnRowRender),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "80"),
                DTColumnBuilder.newColumn('position').withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('出库样本数量').withOption("width", "120")
            ];


            function _fnRowSelectorRender(data, type, full, meta) {
                vm.selected[full.id] = false;
                var html = '';
                html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
                return html;
            }
            function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
                $('td', nRow).unbind('click');
                $(nRow).bind('click', function() {
                    var tr = this;
                    if(!clickFlag){
                        rowClickHandler(tr,oData);
                    }
                    clickFlag = false;
                });
                if(boxId == oData.id){
                    $(nRow).addClass('rowLight');
                }
                return nRow;
            }
            function rowClickHandler(tr,oData) {
                $(tr).closest('table').find('.rowLight').removeClass("rowLight");
                $(tr).addClass('rowLight');
                _loadTubes(oData.id);
            }
            function _fnRowRender(data, type, full, meta) {
                var frozenBoxCode = '';
                if(full.frozenBoxCode1D){
                    frozenBoxCode = "1D:"+full.frozenBoxCode1D +"<br>" + "2D:"+full.frozenBoxCode;
                }else{
                    frozenBoxCode = "2D:"+full.frozenBoxCode;
                }

                return frozenBoxCode;
            }
            function _loadTubes(boxId) {
                if(vm.sampleIds){
                    PlanService.queryPlanTubes(vm.sampleIds,boxId).success(function (data) {
                        vm.tubeData = data;
                        vm.tubeOptions.withOption('data', data);
                        // vm.tubeInstance.rerender();
                    });
                }

            }
            //管子列表
            vm.tubeOptions = BioBankDataTable.buildDTOption("BASIC", 300)
                .withOption('createdRow', function(row, data, dataIndex) {
                    var status = '';
                    switch (data.status){
                        case '3001': status = '正常';break;
                        case '3002': status = '空管';break;
                        case '3003': status = '空孔';break;
                        case '3004': status = '异常';break;
                    }
                    $('td:eq(1)', row).html(status);
                    $compile(angular.element(row).contents())($scope);
                });
            vm.tubeColumns = [
                DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码').withOption("width", "100"),
                DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60"),
                DTColumnBuilder.newColumn('sampleClassificationName').withTitle('样本分类').withOption("width", "90"),
                // DTColumnBuilder.newColumn('sex').withTitle('性别'),
                // DTColumnBuilder.newColumn('age').withTitle('年龄'),
                // DTColumnBuilder.newColumn('sampleUsedTimes').withTitle('使用次数'),
                DTColumnBuilder.newColumn('memo').withTitle('批注'),
                DTColumnBuilder.newColumn('id').notVisible()
            ];
        }

        //任务列表
        vm.taskOptions = BioBankDataTable.buildDTOption("NORMALLY", 170, 5)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                PlanService.queryTaskList(vm.planId,data).then(function (res){
                        vm.selectAll = false;
                        vm.selected = {};
                        var json = res.data;
                        var error = json.error || json.sError;
                        if ( error ) {
                            jqDt._fnLog( oSettings, 0, error );
                        }
                        oSettings.json = json;
                        fnCallback( json );
                    }).catch(function(res){
                        // console.log(res);
                        //
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
            })
            .withOption('createdRow', createdRow);


        vm.taskColumns = [
            DTColumnBuilder.newColumn('stockOutTaskCode').withTitle('任务编码'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn('countOfFrozenBox').withTitle('冻存盒数').notSortable(),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本数'),
            DTColumnBuilder.newColumn('createDate').withTitle('创建日期'),
            DTColumnBuilder.newColumn('stockOutDate').withTitle('出库日期'),
            DTColumnBuilder.newColumn('operators').withTitle('操作员'),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];

        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '1601': status = '待出库';break;
                case '1602': status = '进行中';break;
                case '1603': status = '已出库';break;
                case '1604': status = '异常出库';break;
                case '1690': status = '已作废';break;
            }
            $('td:eq(1)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return  '<button  class="btn btn-xs" ng-click="vm.taskDescModal('+full.id+')"><i class="fa fa-eye"></i></button>&nbsp;' +
                    '<button class="btn btn-xs" ng-if="'+full.status+'==1601" ng-click="vm.taskDelModal('+full.id+')"><i class="fa  fa-trash-o"></i></button>&nbsp;'+
                    '<button class="btn btn-xs" ng-if="'+full.status+'==1601" ng-click="vm.startTask('+full.id+')"><i class="fa fa-play"></i></button>'
        }
        //样本需求
        vm.demand = [];
        vm.checked = [];
        //全选
        vm.selectDemandAll = function () {

            if(vm.select_all) {
                vm.select_one = true;
                vm.checked = [];
                angular.forEach(vm.plan.stockOutRequirement, function (data, index) {
                    vm.checked.push(data.id);
                    vm.demand[data.id] = true;
                });
            }else {
                vm.select_one = false;
                vm.checked = [];
                vm.demand = [];
            }
            vm.sampleIds = vm.checked.join(",");
            _queryPlanBoxes();
        };
        vm.selectDemandOne = function () {
            angular.forEach(vm.demand , function (data, id) {
                var index = vm.checked.indexOf(id);
                if(data && index === -1) {
                    vm.checked.push(id);
                } else if (!data && index !== -1){
                    vm.checked.splice(index, 1);
                }
            });
            if (vm.plan.stockOutRequirement.length === vm.checked.length) {
                vm.select_all = true;
            } else {
                vm.select_all = false;
            }

            // console.log(JSON.stringify(vm.checked))
            vm.sampleIds = vm.checked.join(",");
            _queryPlanBoxes();
        };
        //获取冻存盒列表
        function _queryPlanBoxes() {
            vm.strBoxIds = "";
            setTimeout(function () {
                vm.dtInstance.rerender();
                vm.tubeOptions.withOption('data', []);
                vm.tubeInstance.rerender();
            },100);

        }

        //创建任务
        function _fnCreateTask() {
            if(vm.strBoxIds){
                BioBankBlockUi.blockUiStart();
                PlanService.createTask(vm.planId,vm.strBoxIds).success(function (data) {
                    BioBankBlockUi.blockUiStop();
                    toastr.success("创建任务成功!");
                    _queryPlanBoxes();
                    vm.strBoxIds = "";
                    vm.taskInstance.rerender();
                });
            }else{
                toastr.error("请勾选申请出库的冻存盒!");
            }

        }
        //查看
        function _fnTaskDescModal(taskId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/plan/modal/plan-task-desc-modal.html',
                controller: 'PlanTaskDescModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                            taskId:taskId
                        };
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }
        //删除
        function _fnTaskDelModal(taskId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/plan/modal/plan-task-del-modal.html',
                controller: 'PlanDelModalController',
                controllerAs:'vm'
            });

            modalInstance.result.then(function (data) {
                BioBankBlockUi.blockUiStart();
                PlanService.delTask(taskId).success(function (data) {
                    BioBankBlockUi.blockUiStop();
                    toastr.success("删除任务成功!");
                    _queryPlanBoxes();
                    vm.taskInstance.rerender();
                });
            });

        }
        //开始任务
        function _fnStartTask(id) {
            $state.go('task-edit',{taskId:id});
        }

    }

    function PlanDelModalController($uibModalInstance) {
        var vm = this;
        vm.ok = function () {
            $uibModalInstance.close(true);
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
    }
})();
