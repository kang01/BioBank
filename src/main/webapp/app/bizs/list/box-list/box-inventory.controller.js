/**
 * Created by gaokangkang on 2017/6/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxInventoryController', BoxInventoryController);

    BoxInventoryController.$inject = ['$scope','$compile','$state','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','BoxInventoryService','BioBankDataTable','MasterData','SampleTypeService','RequirementService','FrozenBoxTypesService'];

    function BoxInventoryController($scope,$compile,$state,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,BoxInventoryService,BioBankDataTable,MasterData,SampleTypeService,RequirementService,FrozenBoxTypesService) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dto = {};
        vm.checked = false;

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
            vm.statusOptions = MasterData.frozenBoxStatus;
            vm.compareTypeOption = [
                {value:"1",label:"大于"},
                {value:"3",label:"等于"},
                {value:"4",label:"小于"}
            ];
            vm.spaceTypeOption = [
                {value:"1",label:"已用"},
                {value:"2",label:"剩余"}
            ];
            vm.dto.spaceType = "1";
            vm.dto.compareType = "1";
            vm.sampleTypeOptions = [];
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','asc']);
            });
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
            }
        }
        _init();
        //项目
        var projectIds = "";
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            onChange:function(value){
                vm.projectIds = _.join(value, ',');
                projectIds = value;
                vm.dto.projectCodeStr = [];
                if(vm.dto.sampleTypeId){
                    _fnQueryProjectSampleClass(vm.projectIds,vm.dto.sampleTypeId);
                }
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
            if(vm.frozenBoxAreaOptions.length){
                vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                SupportacksByAreaIdService.query({id:vm.dto.areaId},onShelfSuccess, onError);
            }
        }
        vm.frozenBoxAreaConfig = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);

            }
        };
        //架子
        function onShelfSuccess(data) {
            vm.frozenBoxShelfOptions = data;
            vm.dto.supportRackId = vm.frozenBoxShelfOptions[0].id;
        }
        vm.frozenBoxShelfConfig = {
            valueField:'id',
            labelField:'supportRackCode',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        //设备类型
        vm.equipmentConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };
        //架子类型
        vm.shelvesConfig = {
            valueField:'value',
            labelField:'label',
            maxItems: 1,
            onChange:function(value){
            }
        };
        //盒子状态
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
        //盒子编码
        vm.boxCodeConfig = {
            create: true,
            persist:false,
            onChange: function(value){
                vm.dto.frozenBoxCodeStr = value;
            }
        };
        //样本类型
        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                if(vm.projectIds){
                    _fnQueryProjectSampleClass(vm.projectIds,value);
                }

            }
        };
        //样本分类
        function _fnQueryProjectSampleClass(projectIds,sampleTypeId) {
            RequirementService.queryRequirementSampleClasses(projectIds,sampleTypeId).success(function (data) {
                vm.sampleClassOptions = data;
                if(vm.sampleClassOptions.length){
                    vm.dto.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;
                }
            });
        }
        vm.sampleClassConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        //盒子类型 17:10*10 18:8*8
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){

            }
        };




        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
        vm.dtInstance = {};
        vm.searchShow = _fnSearchShow;
        function _fnSearchShow() {
            vm.checked = true;
        }
        vm.search = _fnSearch;
        function _fnSearch() {
            for(var i = 0; i <projectIds.length; i++){
                var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                vm.dto.projectCodeStr.push(projectCode)
            }
            vm.checked = false;
            vm.dtInstance.rerender();
        }




        // $scope.checked = !$scope.checked;
        // vm.toggle = function () {
        //     $scope.checked = !$scope.checked
        // };



        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                var searchForm = angular.toJson(vm.dto);
                BoxInventoryService.queryBoxList(data,searchForm).then(function (res){
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
            .withOption('createdRow', createdRow);


        vm.dtColumns = [
            DTColumnBuilder.newColumn('position').withTitle('位置'),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码'),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码'),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型'),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类'),
            DTColumnBuilder.newColumn('frozenBoxType').withTitle('盒类型'),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用'),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function createdRow(row, data, dataIndex) {
            var projectName = _.find(vm.projectOptions,{projectCode:data.projectCode}).projectName;
            var status = "";
            status = MasterData.getFrozenBoxStatus(data.status);
            $('td:eq(2)', row).html(projectName);
            $('td:eq(8)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            // return '<button type="button" class="btn btn-xs" ui-sref="plan-edit({planId:'+ full.id +'})">' +
            //     '   <i class="fa fa-edit"></i>' +
            //     '</button>&nbsp;';
            return "";
        }
    }
})();
