/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanDetailController', PlanDetailController)
        .controller('PlanDelModalController', PlanDelModalController)

    PlanDetailController.$inject = ['$scope','$compile','$stateParams','$uibModal','DTOptionsBuilder','DTColumnBuilder','PlanService','RequirementService'];
    PlanDelModalController.$inject = ['$uibModalInstance'];
    function PlanDetailController($scope,$compile,$stateParams,$uibModal,DTOptionsBuilder,DTColumnBuilder,PlanService,RequirementService) {
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
        //创建任务
        vm.createTask = _fnCreateTask;


        if($stateParams.planId){
            vm.planId = $stateParams.planId;
            PlanService.queryPlanSampleInfo(vm.planId).success(function (data) {
                vm.plan = data;
            }).then(function () {

            })
        }
        

        // vm.applyNumberOptions = [];
        // vm.applyNumberConfig = {
        //     create: false,
        //     valueField:'id',
        //     labelField:'applyNumber',
        //     maxItems: 1,
        //     searchField: ['applyNumber'],
        //     onChange:function (value) {
        //         //根据申请单号查找计划信息及样本需求
        //         PlanService.queryPlanSampleInfo(value).success(function (data) {
        //             vm.plan = data;
        //         })
        //     },
        //     load: function(query, callback) {
        //         //查询申请单号
        //         if (query) {
        //             PlanService.queryApplyNumInfo(query).success(function (data) {
        //                 callback(data);
        //             });
        //         }
        //     }
        // };


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
                    arrayId.push(id)
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
                vm.strBoxIds = _.join(arrayId,",");
            };

            vm.toggleOne = function (selectedItems) {
                // console.log(JSON.stringify(selectedItems))
                var arrayId = [];
                for (var id in selectedItems) {
                    if(selectedItems[id]){
                        arrayId.push(id)
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
            vm.dtOptions = DTOptionsBuilder.newOptions()
                .withOption('processing',true)
                .withOption('serverSide',true)
                .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                    var data = {};
                    for(var i=0; aoData && i<aoData.length; ++i){
                        var oData = aoData[i];
                        data[oData.name] = oData.value;
                    }
                    var jqDt = this;
                    if(vm.sampleIds){
                        PlanService.queryPlanBoxes(vm.sampleIds,data).then(function (res){
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
                    }else{
                        var array = {
                            draw : 1,
                            recordsTotal : 100,
                            recordsFiltered : 10,
                            data: [ ],
                            error : ""
                        }
                        fnCallback( array );

                    }

                })
                .withPaginationType('full_numbers')
                .withOption('rowCallback', rowCallback)
                .withOption('createdRow', function(row, data, dataIndex) {
                    // Recompiling so we can bind Angular directive to the DT
                    $compile(angular.element(row).contents())($scope);
                })
                .withOption('headerCallback', function(header) {
                    if (!vm.headerCompiled) {
                        // Use this headerCompiled field to only compile header once
                        vm.headerCompiled = true;
                        $compile(angular.element(header).contents())($scope);
                    }

                });

            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

            vm.dtColumns = [
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码'),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
                DTColumnBuilder.newColumn('position').withTitle('冻存位置'),
                DTColumnBuilder.newColumn('countOfSample').withTitle('出库样本数量'),
                DTColumnBuilder.newColumn('id').notVisible()
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
                    $scope.$apply(function () {
                        rowClickHandler(tr,oData);
                    })
                });
                return nRow;
            }
            function rowClickHandler(tr,oData) {
                $(tr).closest('table').find('.rowLight').removeClass("rowLight");
                $(tr).addClass('rowLight');
                PlanService.queryPlanTubes(1,oData.id).success(function (data) {
                    vm.tubeOptions.withOption('data', data);
                    vm.tubeInstance.rerender();
                })
            }

            //管子列表
            vm.tubeOptions = DTOptionsBuilder.newOptions()
                .withPaginationType('full_numbers');
            vm.tubeColumns = [
                DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
                DTColumnBuilder.newColumn('status').withTitle('状态'),
                DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
                DTColumnBuilder.newColumn('sex').withTitle('性别'),
                DTColumnBuilder.newColumn('age').withTitle('年龄'),
                DTColumnBuilder.newColumn('sampleUsedTimes').withTitle('使用次数'),
                DTColumnBuilder.newColumn('memo').withTitle('批注'),
                DTColumnBuilder.newColumn('id').notVisible()
            ];
        }
        function _fnCreateTask() {
            PlanService.createTask(vm.planId,vm.strBoxIds).success(function (data) {
                vm.taskInstance.rerender();
            })
        }
        //任务列表
        vm.taskOptions = DTOptionsBuilder.newOptions()
            .withOption('processing',true)
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
            .withPaginationType('full_numbers')
            .withOption('createdRow', createdRow);


        vm.taskColumns = [
            DTColumnBuilder.newColumn('stockOutTaskCode').withTitle('任务编码'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn('countOfFrozenBox').withTitle('冻存盒数'),
            DTColumnBuilder.newColumn('countOfSample').withTitle('样本数'),
            DTColumnBuilder.newColumn('createDate').withTitle('创建日期'),
            DTColumnBuilder.newColumn('stockOutDate').withTitle('出库日期'),
            DTColumnBuilder.newColumn('operators').withTitle('操作员'),
            DTColumnBuilder.newColumn('memo').withTitle('备注'),
            DTColumnBuilder.newColumn('id').notVisible(),
            DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
        ];

        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '1501': status = '待出库';break;
                case '1502': status = '已出库';break;
            }
            $('td:eq(1)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return  '<a  ng-click="vm.taskDescModal('+full.id+')">查看</a>&nbsp;' +
                    '<a ng-click="vm.taskDelModal('+full.id+')">删除</a>&nbsp;'
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
                })
            }else {
                vm.select_one = false;
                vm.checked = [];
                vm.demand = [];
            }
            vm.sampleIds = vm.checked.join(",");
            _queryPlanBoxes()
        };
        vm.selectDemandOne = function () {
            angular.forEach(vm.demand , function (data, id) {
                var index = vm.checked.indexOf(id);
                if(data && index === -1) {
                    vm.checked.push(id);
                } else if (!data && index !== -1){
                    vm.checked.splice(index, 1);
                };
            });
            if (vm.plan.stockOutRequirement.length === vm.checked.length) {
                vm.select_all = true;
            } else {
                vm.select_all = false;
            }

            // console.log(JSON.stringify(vm.checked))
            vm.sampleIds = vm.checked.join(",");
            _queryPlanBoxes()
        };
        //获取冻存盒列表
        function _queryPlanBoxes() {
            setTimeout(function () {
                vm.dtInstance.rerender();
            },100)

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
                        }
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
                PlanService.delTask(taskId).success(function (data) {
                    vm.dtInstance.rerender();
                    vm.taskInstance.rerender();
                });
            });

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
