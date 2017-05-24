/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanDetailController', PlanDetailController);

    PlanDetailController.$inject = ['$scope','$compile','$stateParams','$uibModal','DTOptionsBuilder','DTColumnBuilder','PlanService','RequirementService'];

    function PlanDetailController($scope,$compile,$stateParams,$uibModal,DTOptionsBuilder,DTColumnBuilder,PlanService,RequirementService) {
        var vm = this;
        var modalInstance;
        vm.dtInstance = {};
        vm.tubeInstance = {};
        vm.plan = {};
        vm.task = {};
        //查看出库任务列表详情
        vm.taskDescModal = _fnTaskDescModal;
        if($stateParams.planId){
            vm.planId = $stateParams.planId;
            PlanService.queryPlanSampleInfo(vm.planId).then(function (data) {

            }).then(function () {

            })
        }

        vm.applyNumberOptions = [];
        vm.applyNumberConfig = {
            create: false,
            valueField:'id',
            labelField:'applyNumber',
            maxItems: 1,
            searchField: ['applyNumber'],
            onChange:function (value) {
                //根据申请单号查找计划信息及样本需求
                PlanService.queryPlanSampleInfo(value).success(function (data) {
                    vm.plan = data;
                })
            },
            load: function(query, callback) {
                //查询申请单号
                if (query) {
                    PlanService.queryApplyNumInfo(query).success(function (data) {
                        callback(data);
                    });
                }
            }
        };


        _initStockOutBoxesTable();
        function _initStockOutBoxesTable() {
            vm.selected = {};
            vm.selectAll = false;
            // 处理盒子选中状态
            vm.toggleAll = function (selectAll, selectedItems) {
                selectedItems = vm.selected;
                selectAll = vm.selectAll;
                for (var id in selectedItems) {
                    if (selectedItems.hasOwnProperty(id)) {
                        selectedItems[id] = selectAll;
                    }
                }
            };
            vm.toggleOne = function (selectedItems) {
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
                    }

                })
                .withPaginationType('full_numbers')
                .withOption('rowCallback', rowCallback);

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
                vm.selected[full.frozenBoxCode] = false;
                var html = '';
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.frozenBoxCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
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
                    console.log(data)
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
        //样本需求
        vm.demand = [];
        vm.checked = [];
        //全选
        vm.selectDemandAll = function () {
            if(vm.select_all) {
                vm.select_one = true;
                vm.checked = [];
                angular.forEach(vm.plan.requirements, function (data, index) {
                    vm.checked.push(data.id);
                    vm.demand[data.id] = true;
                })
            }else {
                vm.select_one = false;
                vm.checked = [];
                vm.demand = [];
            }
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
            if (vm.plan.requirements.length === vm.checked.length) {
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
            console.log(vm.dtInstance)
            setTimeout(function () {
                vm.dtInstance.rerender();
            },100)

        }


        function _fnTaskDescModal() {
            modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'app/bizs/stock-out/plan/modal/plan-task-desc-modal.html',
                controller: 'PlanTaskDescModalController',
                controllerAs:'vm',
                size:'lg',
                resolve: {
                    items: function () {
                        return {
                        }
                    }
                }
            });

            modalInstance.result.then(function (data) {

            });
        }

    }
})();
