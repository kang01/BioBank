/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请详情页面
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanDetailController', PlanDetailController);

    PlanDetailController.$inject = ['$scope','$compile','$uibModal','DTOptionsBuilder','DTColumnBuilder','PlanService'];

    function PlanDetailController($scope,$compile,$uibModal,DTOptionsBuilder,DTColumnBuilder,PlanService) {
        var vm = this;
        var modalInstance;
        //查看出库任务列表详情
        vm.taskDescModal = _fnTaskDescModal;
        //样本需求
       vm.list = [
            {'id': 101},
            {'id': 102},
            {'id': 103},
            {'id': 104},
            {'id': 105},
            {'id': 106},
            {'id': 107}
        ];
        //样本需求
        vm.demand = [];
        vm.checked = [];
        //全选
        vm.selectDemandAll = function () {
            if(vm.select_all) {
                vm.select_one = true;
                vm.checked = [];
                angular.forEach(vm.list, function (data, index) {
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
            if (vm.list.length === vm.checked.length) {
                vm.select_all = true;
            } else {
                vm.select_all = false;
            }
        };


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
                    PlanService.queryPlanList(data, oSettings).then(function (res){
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
                })
                .withPaginationType('full_numbers')
                .withOption('createdRow', createdRow)
                .withColumnFilter({
                    aoColumns: [{
                        type: 'text',
                        width:50,
                        iFilterLength:3
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true,
                        iFilterLength:3
                    }, {
                        type: 'Datepicker',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'text',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'Datepicker',
                        bRegex: true,
                        bSmart: true
                    }, {
                        type: 'select',
                        bRegex: false,
                        width:50,
                        values: [
                            {value:"10",label:"非常满意"},
                            {value:"9",label:"较满意"},
                            {value:"8",label:"满意"},
                            {value:"7",label:"有少量空管"},
                            {value:"6",label:"有许多空管"},
                            {value:"5",label:"有大量空管"},
                            {value:"4",label:"有少量空孔"},
                            {value:"3",label:"有少量错位"},
                            {value:"2",label:"有大量错位"},
                            {value:"1",label:"非常不满意"}
                        ]
                    }, {
                        type: 'select',
                        bRegex: true,
                        width:50,
                        values: [
                            {value:'1001',label:"进行中"},
                            {value:"1002",label:"待入库"},
                            {value:"1003",label:"已入库"},
                            {value:"1004",label:"已作废"}
                        ]
                    }]
                });
            var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

            vm.dtColumns = [
                DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml).notSortable().renderWith(_fnRowSelectorRender),
                DTColumnBuilder.newColumn('projectSiteCode').withTitle('项目点'),
                DTColumnBuilder.newColumn('projectCode').withTitle('项目编号'),
                DTColumnBuilder.newColumn('transhipDate').withTitle('转运日期'),
                DTColumnBuilder.newColumn('receiver').withTitle('接收人'),
                DTColumnBuilder.newColumn('receiveDate').withTitle('接收日期'),
                DTColumnBuilder.newColumn('sampleSatisfaction').withTitle('满意度'),
                DTColumnBuilder.newColumn('transhipState').withTitle('状态'),
                DTColumnBuilder.newColumn("").withTitle('操作').notSortable().renderWith(actionsHtml)
            ];
            function createdRow(row, data, dataIndex) {
                var transhipState = '';
                var sampleSatisfaction = '';
                switch (data.transhipState){
                    case '1001': transhipState = '进行中';break;
                    case '1002': transhipState = '待入库';break;
                    case '1003': transhipState = '已入库';break;
                    case '1004': transhipState = '已作废';break;
                }
                switch (data.sampleSatisfaction){
                    case 1: sampleSatisfaction = '非常不满意';break;
                    case 2: sampleSatisfaction = '有大量错位';break;
                    case 3: sampleSatisfaction = '有少量错位';break;
                    case 4: sampleSatisfaction = '有少量空孔';break;
                    case 5: sampleSatisfaction = '有大量空管';break;
                    case 6: sampleSatisfaction = '有许多空管';break;
                    case 7: sampleSatisfaction = '有少量空管';break;
                    case 8: sampleSatisfaction = '满意';break;
                    case 9: sampleSatisfaction = '较满意';break;
                    case 10: sampleSatisfaction = '非常满意';break;
                }
                $('td:eq(5)', row).html(sampleSatisfaction);
                $('td:eq(6)', row).html(transhipState);
                $compile(angular.element(row).contents())($scope);
            }
            function actionsHtml(data, type, full, meta) {
                return '<button type="button" class="btn btn-warning" ui-sref="transport-record-edit({id:'+ full.id +'})">' +
                    '   <i class="fa fa-edit"></i>' +
                    '</button>&nbsp;'
            }
            function _fnRowSelectorRender(data, type, full, meta) {
                vm.selected[full.projectCode] = false;
                var html = '';
                html = '<input type="checkbox" ng-model="vm.selected[\'' + full.projectCode + '\']" ng-click="vm.toggleOne(vm.selected)">';
                return html;
            }
        }

    }
})();
