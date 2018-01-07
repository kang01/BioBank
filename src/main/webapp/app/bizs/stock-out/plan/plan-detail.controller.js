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

    PlanDetailController.$inject = ['$scope','$compile','$state','$stateParams','toastr','$uibModal','DTOptionsBuilder','DTColumnBuilder','PlanService','BioBankBlockUi','BioBankDataTable','MasterData','$timeout'];
    PlanDelModalController.$inject = ['$uibModalInstance'];
    function PlanDetailController($scope,$compile,$state,$stateParams,toastr,$uibModal,DTOptionsBuilder,DTColumnBuilder,PlanService,BioBankBlockUi,BioBankDataTable,MasterData,$timeout) {
        var vm = this;
        var modalInstance;
        vm.boxData = [];
        vm.dtInstance = {};
        vm.tubeInstance = {};
        vm.taskInstance = {};
        vm.plan = {};
        //选中的冻存盒右侧列表
        vm.checkedPlanFlag = false;
        //样本需求
        vm.demand = [];
        //需求的全选
        vm.select_demand_all = true;
        //计划ID
        var _planId = $stateParams.planId;
        //需求的id
        var _requirementIds = [];
        var _requirementIdStr;
        //选择的计划的盒子列表
        var _selectedBox = [];
        //选中的冻存盒数量
        vm.selectedLen = 0;
        var _boxId;



        //查看出库任务列表详情
        vm.taskDescModal = _fnTaskDescModal;
        //删除任务
        vm.taskDelModal = _fnTaskDelModal;
        //开始任务
        vm.startTask = _fnStartTask;
        //创建任务
        vm.createTask = _fnCreateTask;
        //获取样本
        vm.loadTubes = _loadTubes;
        //关闭右侧弹出框
        vm.close = _fnClose;

        //获取出库计划的信息
        _queryPlanInfo();
        //冻存盒列表、样本列表、出库任务列表
        _initStockOutPlanTables();


        //全选
        vm.selectDemandAll = function (selectAll, selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
            if(selectAll){
                vm.dtOptions.withOption('data',vm.boxData);
            }else{
                vm.selectAll = false;
                vm.dtOptions.withOption('data',[]);
                _clearSelectedBox();
            }
        };
        vm.selectDemandOne = function (selectedItems) {
            var requirementIds = [];
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        requirementIds.push(id);
                    }
                }
            }
            // 前端过滤根据需求获取冻存盒的数据
            _changeBoxData(requirementIds);
            vm.selectAll = false;
            vm.toggleAll(vm.selectAll,vm.selected);
            _clearSelectedBox();

            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        vm.select_demand_all = false;
                        return;
                    }
                }
            }
            vm.select_demand_all = true;

        };

        //获取出库计划的信息
        function _queryPlanInfo(){
            if(_planId){
                PlanService.queryPlanSampleInfo(_planId).success(function (data) {
                    vm.plan = data;
                    angular.forEach(vm.plan.stockOutRequirement, function (data, index) {
                        vm.demand[data.id] = true;
                        _requirementIds.push(data.id);
                    });
                    _queryPlanBoxes(_requirementIds);

                }).error(function () {

                });
            }
        }
        //冻存盒列表、样本列表、出库任务列表
        function _initStockOutPlanTables() {
            vm.selected = {};
            vm.selectAll = false;

            vm.selectedTube = {};
            vm.selectAllTube = false;
            vm.toggleAllTube = _toggleAllTube;
            vm.toggleOneTube = _toggleOneTube;
            // 处理盒子选中状态
            vm.toggleAll = function (selectAll, selectedItems) {
                selectedItems = vm.selected;
                selectAll = vm.selectAll;
                // var _boxIdArray = [];
                for (var id in selectedItems) {
                    // _boxIdArray.push(id);
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
                // vm.strBoxIds = _.join(_boxIdArray,",");
                //
                if(!selectAll){
                    //全部不选中
                    vm.strBoxIds = "";
                    _.forEach(vm.boxData, function(value) {
                        var len  =  _.filter(_selectedBox, {id:value.id}).length;
                        if(len){
                            _.remove(_selectedBox,{id:value.id});
                        }
                    });
                }else{
                    for(var i = 0; i< vm.boxData.length; i++){
                        var len = _.filter(_selectedBox,{id:+vm.boxData[i].id}).length;
                        if(!len){
                            _selectedBox.push(vm.boxData[i])
                        }
                    }
                }
                vm.selectedLen = _selectedBox.length;
                vm.selectedOptions.withOption('data', _selectedBox);
                var boxIdArray = _.map(_selectedBox, 'id');
                vm.strBoxIds = _.join(boxIdArray,",");
            };
            vm.toggleOne = function (selectedItems) {
                // var boxIdArray = [];
                for (var id in selectedItems) {
                    if(selectedItems[id]){
                        // boxIdArray.push(id);
                        var obj = _.find(vm.boxData,{id:+id});
                        var len = _.filter(_selectedBox,{id:+id}).length;
                        if(!len){
                            _selectedBox.push(obj);
                        }
                    }else{
                        var index = [];
                        if(_selectedBox.length){
                            for(var i = 0; i < _selectedBox.length; i++){
                                if(+id == _selectedBox[i].id){
                                    index.push(i);
                                }
                            }
                            _.pullAt(_selectedBox, index);
                        }
                    }
                }
                vm.selectedLen = _selectedBox.length;
                vm.selectedOptions.withOption('data', _selectedBox);
                var boxIdArray = _.map(_selectedBox, 'id');
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
                    },{
                        text: '撤销',
                        className: 'btn btn-primary btn-sm ml-10',
                        key: '1',
                        action: _onRepealBoxHandler
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
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode1D').withOption("width", "30").notSortable().withOption('searchable',false).withTitle('#'),
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
                $('td:nth-child(2)', nRow).html(iDisplayIndex + 1);
                return nRow;
            }
            function _fnRowRender(data, type, full, meta) {
                var frozenBoxCode = '';
                if(full.frozenBoxCode1D){
                    // frozenBoxCode = "1D:"+full.frozenBoxCode1D +"&nbsp;<br>" + "2D:"+full.frozenBoxCode;
                    frozenBoxCode = "<a>1D:"+full.frozenBoxCode1D+"&nbsp;<br>2D:"+full.frozenBoxCode+"</a>"
                }else{
                    frozenBoxCode = "<a ng-click='vm.loadTubes("+full.id+")'>2D:"+full.frozenBoxCode+"</a>";
                }

                return frozenBoxCode;
            }
            var tubeTitleHtmT = '<input type="checkbox" ng-model="vm.selectAllTube" ng-click="vm.toggleAllTube(vm.selectAllTube,vm.selectedTube)">';
            //管子列表
            vm.tubeOptions = BioBankDataTable.buildDTOption("BASIC", 300,5)
                .withOption('createdRow', function(row, data, dataIndex) {
                    var status = MasterData.getStatus(data.status);
                    $('td:eq(2)', row).html(status);
                    $compile(angular.element(row).contents())($scope);
                })
                .withOption('headerCallback', function(header) {
                    $compile(angular.element(header).contents())($scope);
                })
                .withButtons([
                    {
                        text: '撤销',
                        className: 'btn btn-primary btn-sm ml-10',
                        key: '1',
                        action: _onRepealTubeHandler
                    }
                ]);
            vm.tubeColumns = [
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(tubeTitleHtmT).withOption('searchable',false).notSortable().renderWith(_tubeRowSelectorRender),
                DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码').withOption("width", "100"),
                DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "50"),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60"),
                DTColumnBuilder.newColumn('sampleClassificationName').withTitle('样本分类').withOption("width", "120"),
                DTColumnBuilder.newColumn('memo').withTitle('批注')
            ];
            function _tubeRowSelectorRender(data, type, full, meta) {
                vm.selectedTube[full.id] = false;
                return '<input type="checkbox" ng-model="vm.selectedTube[' + full.id + ']" ng-click="vm.toggleOneTube(vm.selectedTube)">';
            }
            function _toggleAllTube(selectAll, selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            }
            function _toggleOneTube(selectedItems) {
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        if(!selectedItems[id]) {
                            vm.selectAllTube = false;
                            return;
                        }
                    }
                }
                vm.selectAllTube = true;
            }
            vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
            vm.selectedColumns = [
                DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('冻存盒编码').withOption("width", "130").renderWith(_fnRowRender),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型').withOption("width", "60")

            ];

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
                    PlanService.queryTaskList(_planId,data).then(function (res){
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
                var status = MasterData.getStatus(data.status);
                $('td:eq(1)', row).html(status);
                $compile(angular.element(row).contents())($scope);
            }
            function actionsHtml(data, type, full, meta) {
                return  '<button  class="btn btn-xs" ng-click="vm.taskDescModal('+full.id+')"><i class="fa fa-eye"></i></button>&nbsp;' +
                    '<button class="btn btn-xs" ng-if="'+full.status+'==1601" ng-click="vm.taskDelModal('+full.id+')"><i class="fa  fa-trash-o"></i></button>&nbsp;'+
                    '<button class="btn btn-xs" ng-if="'+full.status+'==1601" ng-click="vm.startTask('+full.id+')"><i class="fa fa-play"></i></button>'
            }
        }
        //前端过滤根据需求获取冻存盒的数据
        function _changeBoxData(requirementIds) {
            var boxData = angular.copy(vm.boxData);
            _.forEach(requirementIds,function (id) {
                _.remove(boxData,{stockOutRequirementId:+id});
            });
            vm.dtOptions.withOption("data",boxData);
        }
        //清除选中的盒子
        function _clearSelectedBox() {
            _selectedBox = [];
            vm.selectedLen = 0;
            vm.selectedOptions.withOption('data', []);
        }
        //获取冻存盒列表
        function _queryPlanBoxes() {
            var obj = {
                draw:1,
                length:-1
            };
            _requirementIdStr = _requirementIds.join(",");
            PlanService.queryPlanBoxes(_requirementIdStr,obj).success(function (res){
                vm.boxData = res.data;
                vm.dtOptions.withOption('data',vm.boxData);
            });
            // vm.strBoxIds = "";
            // setTimeout(function () {
            //     if(_requirementIdStr){
            //
            //     }else{
            //         vm.dtOptions.withOption('data',[]);
            //     }
            //     // vm.tubeOptions.withOption('data', []);
            // },100);
        }
        //获取管子信息
        function _loadTubes(boxId) {
            _boxId = boxId;
            if(_requirementIdStr){
                PlanService.queryPlanTubes(_requirementIdStr,boxId).success(function (data) {
                    vm.tubeData = data;
                    vm.tubeOptions.withOption('data', data);
                    vm.selectAllTube = false;
                });
            }

        }
        //创建任务
        function _fnCreateTask() {
            if(vm.strBoxIds){
                BioBankBlockUi.blockUiStart();
                PlanService.createTask(_planId,vm.strBoxIds).success(function (data) {
                    BioBankBlockUi.blockUiStop();
                    toastr.success("创建任务成功!");
                    _queryPlanBoxes();
                    vm.strBoxIds = "";
                    vm.taskInstance.rerender();
                    vm.checkedPlanFlag = false;
                    vm.selectAll = false;
                    _clearSelectedBox();
                });
            }else{
                toastr.error("请勾选申请出库的冻存盒!");
            }

        }
        //自定义勾选
        function _fnSelectTask() {
            vm.boxData = vm.dtInstance.DataTable.rows().data();
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

            modalInstance.result.then(function (indexArray) {
                _selectedBox = [];
                vm.selectedLen = 0;
                vm.selectedOptions.withOption('data', _selectedBox);
                _.forEach(vm.boxData,function (box) {
                    vm.selected[box.id] = false;
                });

                _.forEach(indexArray,function (index) {
                    if(vm.boxData.length >= index){
                        _fnSelectBoxData(index);
                    }
                });

            });

        }
        //撤销计划 status:1:盒子 2:样本
        function _onRepealBoxHandler() {
            var boxIdArray = _.compact(_.split(vm.strBoxIds, ','));
            if(!boxIdArray.length){
                toastr.error("请选择撤销的冻存盒！");
                return;
            }
            _repealPlan(1);
        }
        var _tubeIds = [];
        function _onRepealTubeHandler() {
            _tubeIds = [];
            for(var id in vm.selectedTube){
                if (vm.selectedTube.hasOwnProperty(id)) {
                    if(vm.selectedTube[id]) {
                        _tubeIds.push(id);
                    }
                }
            }
            if(!_tubeIds.length){
                toastr.error("请选择撤销的冻存样本！");
                return;
            }
            _repealPlan(2);
        }
        function _repealPlan(status) {

            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/common/prompt-content-modal.html',
                controller: 'PromptContentModalController',
                controllerAs:'vm',
                size:'md',
                backdrop:'static',
                resolve: {
                    items: function () {
                        return {
                            status:"1"
                        };
                    }
                }
            });

            modalInstance.result.then(function (repealReason) {
                if(status == 1){
                    _repealBox(repealReason);
                }else{
                    _repealTube(repealReason);
                }


            });
        }
        function _repealBox(repealReason) {
            var repealArray = [];
            var boxIdArray = _.split(vm.strBoxIds, ',');
            _.forEach(vm.boxData,function (box) {
                _.forEach(boxIdArray,function (boxId) {
                    if(box.id == boxId){
                        var obj = {};
                        obj.id = box.id;
                        obj.repealReason = repealReason;
                        obj.stockOutRequirementId = box.stockOutRequirementId;
                        repealArray.push(obj);
                    }
                })
            });
            PlanService.repealBox(_planId,repealArray).success(function (data) {
                _.forEach(boxIdArray,function (boxId) {
                    _.remove(vm.boxData,{id:+boxId});
                });
                toastr.success("撤销成功！");
                vm.dtOptions.withOption('data',vm.boxData);
                vm.tubeOptions.withOption('data', []);
                _clearData();
            });
        }
        function _repealTube(repealReason) {
            var repealArray = [];
            _.forEach(vm.tubeData,function (tube) {
                _.forEach(_tubeIds,function (tubeId) {
                    if(tube.id == tubeId){
                        var obj = {};
                        obj.id = tube.id;
                        obj.repealReason = repealReason;
                        repealArray.push(obj);
                    }
                })
            });
            PlanService.repealTube(_planId,repealArray).success(function (data) {
                _.forEach(_tubeIds,function (tubeId) {
                    _.remove(vm.tubeData,{id:+tubeId});
                });
                toastr.success("撤销成功！");
                vm.tubeOptions.withOption('data', vm.tubeData);
                _tubeIds = [];
                vm.selectAllTube = false;
                // _queryPlanBoxes();
                _.forEach(vm.boxData,function (box) {
                    if(box.id == _boxId){
                        box.countOfSample -= repealArray.length;
                    }
                    if(box.countOfSample == 0){
                        _.remove(vm.boxData,{id:box.id});
                    }
                });
                vm.dtOptions.withOption('data',vm.boxData);
            });
        }
        function _clearData(){
            //id串
            vm.strBoxIds = "";
            //右侧导航栏
            vm.checkedPlanFlag = false;
            //全选
            vm.selectAll = false;
            //清空购物车里的数据
            _clearSelectedBox();
        }
        //选中冻存盒列表
        function _fnSelectBoxData(index) {

            var len = _.filter(_selectedBox,{id:vm.boxData[index-1].id}).length;
            if(!len){
                //要勾选的数据
                _selectedBox.push(vm.boxData[index-1]);
            }

            vm.selectedLen = _selectedBox.length;
            _.forEach(_selectedBox,function (box) {
                vm.selected[box.id] = true;
            });
            if(vm.selectedLen == vm.boxData.length){
                vm.selectAll = true;
            }else{
                vm.selectAll = false;
            }
            vm.selectedOptions.withOption('data', _selectedBox);
            //开始任务需要的id串
            var boxIdArray = _.map(_selectedBox, 'id');
            vm.strBoxIds = _.join(boxIdArray,",");

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
