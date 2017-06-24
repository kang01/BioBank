/**
 * Created by gaokangkang on 2017/6/23.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentInventoryController', EquipmentInventoryController);

    EquipmentInventoryController.$inject = ['$scope','$compile','$state','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','PlanService','BioBankDataTable'];

    function EquipmentInventoryController($scope,$compile,$state,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,PlanService,BioBankDataTable) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dto = {};
        vm.dto.address = "北京";


        function _init() {
            //获取项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备
            EquipmentService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = data;
            }
            vm.equipmentOptions = [
                {value:"1",label:"冰箱"},
                {value:"2",label:"液氮罐"}
            ];
            vm.shelvesOptions = [
                {value:"1",label:"4*6"},
                {value:"2",label:"6*4"}
            ];
            vm.statusOptions = [
                {value:"1",label:"运行中"},
                {value:"2",label:"申请移出"},
                {value:"3",label:"申请移入"},
                {value:"4",label:"申请换位"},
                {value:"5",label:"已停用"}
            ];
            vm.compareTypeOption = [
                {value:"1",label:"大于"},
                {value:"3",label:"等于"},
                {value:"4",label:"小于"}
            ];
            vm.spaceTypeOption = [
                {value:"1",label:"已用"},
                {value:"2",label:"剩余"}
            ]
        }
        _init();
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            onChange:function(value){
                vm.projectIds = _.join(value, ',');
                $scope.$apply();
            }
        };
        //盒子位置
        vm.frozenBoxPlaceConfig = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
                AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
            }
        };
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;
            vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
        }
        vm.frozenBoxAreaConfig = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                    if(value == vm.frozenBoxAreaOptions[i].id){
                        vm.dto.areaCode = vm.frozenBoxAreaOptions[i].areaCode;
                    }
                }
                SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);

            }
        };
        //架子
        function onShelfSuccess(data) {
            vm.frozenBoxShelfOptions = data;
        }
        vm.frozenBoxShelfConfig = {
            valueField:'id',
            labelField:'supportRackCode',
            maxItems: 1,
            onChange:function (value) {
                for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                    if(value == vm.frozenBoxShelfOptions[i].id){
                        vm.dto.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode;
                    }
                }
            }
        };
        vm.equipmentConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };
        vm.shelvesConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };
        vm.statusConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };
        vm.compareTypeConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };
        vm.spaceTypeConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };

        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }





        $scope.checked = !$scope.checked;
        vm.toggle = function () {
            $scope.checked = !$scope.checked
        };



        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                PlanService.queryPlanList(data, oSettings).then(function (res){
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
            .withOption('createdRow', createdRow)
            .withColumnFilter({
                aoColumns: [{
                    type: 'text',
                    width:50,
                    iFilterLength:3
                },{
                    type: 'text',
                    width:50,
                    iFilterLength:3
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true,
                    iFilterLength:3
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: true,
                    bSmart: true
                }, {
                    type: 'text',
                    bRegex: false
                }, {
                    type: 'select',
                    bRegex: true,
                    width:50,
                    values: [
                        {value:'1401',label:"进行中"},
                        {value:"1402",label:"已完成"},
                        {value:"1403",label:"已作废"}
                    ]
                },null]
            });

        vm.dtColumns = [
            DTColumnBuilder.newColumn('applyNumber').withTitle('设备类型'),
            DTColumnBuilder.newColumn('stockOutPlanCode').withTitle('设备'),
            DTColumnBuilder.newColumn('planDate').withTitle('区域'),
            DTColumnBuilder.newColumn('purposeOfSample').withTitle('架子'),
            DTColumnBuilder.newColumn('countOfStockOutPlanSample').withTitle('架子类型'),
            DTColumnBuilder.newColumn('countOfStockOutTask').withTitle('已用'),
            DTColumnBuilder.newColumn('status').withTitle('剩余'),
            DTColumnBuilder.newColumn('status').withTitle('项目编码'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function createdRow(row, data, dataIndex) {
            var planStatus = '';
            switch (data.status){
                case '1401': planStatus = '进行中';break;
                case '1402': planStatus = '已完成';break;
                case '1403': planStatus = '已作废';break;
            }
            $('td:eq(6)', row).html(planStatus);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            return '<button type="button" class="btn btn-xs" ui-sref="plan-edit({planId:'+ full.id +'})">' +
                '   <i class="fa fa-edit"></i>' +
                '</button>&nbsp;';
        }
    }
})();
