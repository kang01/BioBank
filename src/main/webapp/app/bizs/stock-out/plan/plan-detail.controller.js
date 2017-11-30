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
        vm.checkedPlanFlag = false;

        //查看出库任务列表详情
        vm.taskDescModal = _fnTaskDescModal;
        //删除任务
        vm.taskDelModal = _fnTaskDelModal;
        //开始任务
        vm.startTask = _fnStartTask;
        //创建任务
        vm.createTask = _fnCreateTask;
        vm.close = _fnClose;

        var applyId;
        if($stateParams.planId){
            vm.planId = $stateParams.planId;
            PlanService.queryPlanSampleInfo(vm.planId).success(function (data) {
                vm.plan = data;
                applyId = data.id;
                angular.forEach(vm.plan.stockOutRequirement, function (data, index) {
                    vm.checkedApply.push(data.id);
                    vm.demand[data.id] = true;
                    vm.sampleIds = vm.checkedApply.join(",");
                    _queryPlanBoxes();
                    // _fnLoadBox();
                });
                vm.select_all = true;
            }).error(function () {

            });
            var obj = {
                draw:1,
                length:-1
            };

        }

        _initStockOutBoxesTable();
        //选择的计划的盒子列表
        var selectedSample;
        function _initStockOutBoxesTable() {
            vm.selected = {};
            vm.selectAll = false;

            // 处理盒子选中状态
            vm.toggleAll = function (selectAll, selectedItems) {
                selectedItems = vm.selected;
                selectAll = vm.selectAll;
                var boxIdArray = [];
                for (var id in selectedItems) {
                    boxIdArray.push(id);
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
                vm.strBoxIds = _.join(boxIdArray,",");
                //
                if(!selectAll){
                    //全部不选中
                    vm.strBoxIds = "";
                    _.forEach(vm.boxData, function(value) {
                        var len  =  _.filter(selectedSample, {id:value.id}).length;
                        if(len){
                            _.remove(selectedSample,{id:value.id});
                        }
                    });
                }else{
                    for(var i = 0; i< vm.boxData.length; i++){
                        var len = _.filter(selectedSample,{id:+vm.boxData[i].id}).length;
                        if(!len){
                            selectedSample.push(vm.boxData[i])
                        }
                    }
                }
                vm.selectedLen = selectedSample.length;
                vm.selectedOptions.withOption('data', selectedSample);
            };
            var clickFlag = false;
            //选择的计划的盒子列表
            selectedSample = [];
            vm.toggleOne = function (selectedItems) {
                clickFlag = true;
                var boxIdArray = [];
                for (var id in selectedItems) {
                    if(selectedItems[id]){
                        boxIdArray.push(id);
                        var obj = _.find(vm.boxData,{id:+id});
                        var len = _.filter(selectedSample,{id:+id}).length;
                        if(!len){
                            selectedSample.push(obj);
                        }
                    }else{
                        var index = [];
                        if(selectedSample.length){
                            for(var i = 0; i < selectedSample.length; i++){
                                if(+id == selectedSample[i].id){
                                    index.push(i);
                                }
                            }
                            _.pullAt(selectedSample, index);
                        }
                    }
                }
                vm.selectedLen = selectedSample.length;
                vm.selectedOptions.withOption('data', selectedSample);
                vm.strBoxIds = _.join(boxIdArray,",");
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
            vm.selectedLen = 0;
            // vm.boxData = [];
            // vm.dtOptions.withOption('data',vm.boxData);
            vm.dtOptions = BioBankDataTable.buildDTOption("SORTING,SEARCHING", 300, 5)
                .withOption('order', [[2,'asc']])
                .withButtons([
                    {
                        text: '创建任务',
                        className: 'btn btn-primary btn-sm ml-10',
                        key: '1',
                        action: _fnCreateTask
                    },{
                        text: '勾选',
                        className: 'btn btn-primary btn-sm ml-10',
                        key: '1',
                        action: _fnSelectTask
                    },
                    {
                        text: '<i class="fa fa-shopping-cart"></i><div class="circle1" ng-if="vm.selectedLen">{{vm.selectedLen}}</div>',
                        className: 'btn btn-primary btn-sm ml-10 position-relative',
                        key: '2',
                        action: _fnViewPlanDetail
                    }
                ])
                .withOption('rowCallback', rowCallback)
                .withOption('createdRow', function(row, data, iDisplayIndex) {
                    $compile(angular.element(row).contents())($scope);
                })
                .withOption('headerCallback', function(header) {
                    var circle1 = $(".circle1").parent()[0];

                    $compile(angular.element(header).contents())($scope);
                    $compile(angular.element(circle1).contents())($scope);
                });

            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

            vm.dtColumns = [
                // DTColumnBuilder.newColumn('id').notVisible(),
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode1D').withOption("width", "30").notSortable().withOption('searchable',false).withTitle('#'),
                DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('冻存盒编码').withOption("width", "90").renderWith(_fnRowRender),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "80"),
                DTColumnBuilder.newColumn('position').withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('出库样本数量').withOption("width", "120")
            ];


            function _fnRowSelectorRender(data, type, full, meta) {
                var len = _.filter(selectedSample,{id:full.id}).length;
                if(len){
                    vm.selected[full.id] = true;
                }else{
                    vm.selected[full.id] = false;
                }
                var html = '';
                html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
                return html;
            }
            function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
                $('td:nth-child(2)', nRow).html(iDisplayIndex + 1);
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
                    frozenBoxCode = "1D:"+full.frozenBoxCode1D +"&nbsp;<br>" + "2D:"+full.frozenBoxCode;
                }else{
                    frozenBoxCode = "2D:"+full.frozenBoxCode;
                }

                return frozenBoxCode;
            }
            //获取管子信息
            function _loadTubes(boxId) {
                if(vm.sampleIds){
                    PlanService.queryPlanTubes(vm.sampleIds,boxId).success(function (data) {
                        vm.tubeData = data;
                        vm.tubeOptions.withOption('data', data);
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




            vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
            vm.selectedColumns = [
                DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('样本编码').withOption("width", "130").renderWith(_fnRowRender),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60")
                // DTColumnBuilder.newColumn('status').withTitle('状态'),

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
        vm.checkedApply = [];
        //全选
        vm.selectDemandAll = function () {

            if(vm.select_all) {
                vm.select_one = true;
                vm.checkedApply = [];
                angular.forEach(vm.plan.stockOutRequirement, function (data, index) {
                    vm.checkedApply.push(data.id);
                    vm.demand[data.id] = true;
                });
            }else {
                vm.select_one = false;
                vm.checkedApply = [];
                vm.demand = [];
                vm.selectedLen = 0;
                vm.selectedOptions.withOption('data', []);
                vm.selectAll = false;
            }
            vm.sampleIds = vm.checkedApply.join(",");
            _queryPlanBoxes();
        };
        vm.selectDemandOne = function () {
            angular.forEach(vm.demand , function (data, id) {
                var index = vm.checkedApply.indexOf(id);
                if(data && index === -1) {
                    vm.checkedApply.push(id);
                } else if (!data && index !== -1){
                    vm.checkedApply.splice(index, 1);
                }
            });
            if (vm.plan.stockOutRequirement.length === vm.checkedApply.length) {
                vm.select_all = true;
            } else {
                vm.select_all = false;
            }

            // console.log(JSON.stringify(vm.checkedApply))
            vm.sampleIds = vm.checkedApply.join(",");
            _queryPlanBoxes();
        };
        //获取冻存盒列表
        function _queryPlanBoxes() {
            vm.strBoxIds = "";
            setTimeout(function () {
                if(vm.sampleIds){
                    PlanService.queryPlanBoxes(vm.sampleIds,obj).success(function (res){
                        vm.boxData = res.data;
                        vm.dtOptions.withOption('data',vm.boxData);
                    });
                }else{
                    vm.dtOptions.withOption('data',[]);
                    vm.dtInstance.rerender();

                }

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
                    vm.checkedPlanFlag = false;
                    vm.selectedLen = 0;
                    vm.selectedOptions.withOption('data', []);
                    vm.selectAll = false;
                });
            }else{
                toastr.error("请勾选申请出库的冻存盒!");
            }

        }
        //自定义勾选
        function _fnSelectTask() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/plan/modal/plan-task-select-modal.html',
                controller: 'PlanTaskSelectModalController',
                controllerAs:'vm',
                size:'md',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {

                        };
                    }
                }
            });

            modalInstance.result.then(function (paginationIndexArray) {
                vm.boxData = _.orderBy(vm.boxData, ['frozenBoxCode1D'], ['asc']);
                var boxIdArray = [];
                var rgExp = /\d{1,3}-\d{1,3}/;
                _.forEach(paginationIndexArray,function (pagination) {
                    var index = pagination.indexOf("-");
                    //检查输入的内容中是否包含-
                    if(index != -1){
                        if(rgExp.test(pagination)){
                            //匹配成功后，判断第一个数要小于第二个数，不然就输入错误
                            var num1 = _.toNumber(pagination.substr(0,index));
                            var num2 = _.toNumber(pagination.substring(index+1));
                            if(num2 > vm.boxData.length){
                                num2 = vm.boxData.length;
                                vm.selectAll = true;
                            }
                            for(var i = num1; i < num2+1; i++){
                                _fnSelectBoxData(i);

                            }
                        }
                    }else{
                        var i = _.toNumber(pagination);
                        _fnSelectBoxData(i);
                    }

                });
                //勾选冻存盒列表
                function _fnSelectBoxData(index) {
                    var len = _.filter(selectedSample,{id:vm.boxData[index-1].id}).length;
                    if(!len){
                        //要勾选的数据
                        selectedSample.push(vm.boxData[index-1]);
                        //盒子ID
                        boxIdArray.push(vm.boxData[index-1].id);
                    }
                }
                vm.dtInstance.rerender();
                vm.selectedLen = selectedSample.length;
                vm.selectedOptions.withOption('data', selectedSample);
                vm.strBoxIds = _.join(boxIdArray,",");

            });
        }
        //查看选中的计划详情
        function _fnViewPlanDetail() {
            vm.checkedPlanFlag = true;
            $scope.$apply();
        }
        //关闭
        function _fnClose() {
            vm.checkedPlanFlag = false;
        }
        //查看
        function _fnTaskDescModal(taskId) {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/plan/modal/plan-task-desc-modal.html',
                controller: 'PlanTaskDescModalController',
                controllerAs:'vm',
                size:'lg',
                backdrop:'static',
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
