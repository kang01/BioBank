/**
 * Created by gaokangkang on 2017/6/25.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxInventoryController', BoxInventoryController);

    BoxInventoryController.$inject = ['$scope','$stateParams','$compile','$state','$uibModal','DTColumnBuilder','toastr','ProjectService','EquipmentAllService','AreasByEquipmentIdService','SupportacksByAreaIdService','BoxInventoryService','BioBankDataTable','BioBankSelectize','MasterData','SampleTypeService','RequirementService','FrozenBoxTypesService'];

    function BoxInventoryController($scope,$stateParams,$compile,$state,$uibModal,DTColumnBuilder,toastr,ProjectService,EquipmentAllService,AreasByEquipmentIdService,SupportacksByAreaIdService,BoxInventoryService,BioBankDataTable,BioBankSelectize,MasterData,SampleTypeService,RequirementService,FrozenBoxTypesService) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dto = {};
        vm.checked = false;
        var selectedBox;
        var modalInstance;
        vm.dto.equipmentId = $stateParams.equipmentId;
        vm.dto.areaId = $stateParams.areaId;
        vm.dto.shelvesId = $stateParams.shelvesId;
        //项目
        var projectIds = [];

        vm.dtInstance = {};
        vm.selectedInstance = {};
        vm.search = _fnSearch;
        vm.searchShow = _fnSearchShow;
        vm.selectedShow = _fnSearchShow;
        vm.movement = _fnMovement;
        vm.close = _fnClose;
        vm.empty = _fnEmpty;
        //交换、销毁
        vm.exchangeDestroy = _fnExchangeDestroy;

        //根据盒子编码查找样本
        vm.searchSample = _fnSearchSample;

        function _init() {
            //获取项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
            }
            //设备
            EquipmentAllService.query({},onEquipmentSuccess, onError);
            function onEquipmentSuccess(data) {
                vm.frozenBoxPlaceOptions = _.orderBy(data,['equipmentCode'],['asc']);
            }
            if(vm.dto.equipmentId){
                AreasByEquipmentIdService.query({id:vm.dto.equipmentId},onAreaSuccess, onError);
            }
            if(vm.dto.areaId){
                SupportacksByAreaIdService.query({id:vm.dto.areaId},onShelfSuccess, onError);
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
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:'99'});
            });
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            function onFrozenBoxTypeSuccess(data) {
                vm.frozenBoxTypeOptions = _.orderBy(data, ['id'], ['asc']);
            }
            vm.projectConfig = BioBankSelectize.buildSettings({
                valueField:'id',
                labelField:'projectName',
                searchField:'projectName',
                clearMaxItemFlag : true
            });
            vm.projectConfig.onChange = function(value){
                vm.projectIds = _.join(value, ',');
                projectIds = value;
                vm.dto.projectCodeStr = [];
                if(vm.dto.sampleTypeId){
                    _fnQueryProjectSampleClass(vm.projectIds,vm.dto.sampleTypeId);
                }
            };
            //盒子位置
            vm.frozenBoxPlaceConfig = BioBankSelectize.buildSettings({
                valueField:'id',
                labelField:'equipmentCode'
            });
            vm.frozenBoxPlaceConfig.onChange = function (value) {
                if(value){
                    AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                }else{
                    vm.frozenBoxAreaOptions = [
                        {id:"",areaCode:""}
                    ];
                    vm.dto.areaId = "";
                    vm.frozenBoxShelfOptions = [
                        {id:"",supportRackCode:""}
                    ];
                    vm.dto.shelvesId = "";
                    $scope.$apply();
                }
            };
            // vm.frozenBoxPlaceConfig = {
            //     valueField:'id',
            //     labelField:'equipmentCode',
            //     maxItems: 1,
            //     onChange:function (value) {
            //         if(value){
            //             AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
            //         }else{
            //             vm.frozenBoxAreaOptions = [
            //                 {id:"",areaCode:""}
            //             ];
            //             vm.dto.areaId = "";
            //             vm.frozenBoxShelfOptions = [
            //                 {id:"",supportRackCode:""}
            //             ];
            //             vm.dto.shelvesId = "";
            //             $scope.$apply();
            //         }
            //
            //     }
            // };
            function onAreaSuccess(data) {
                vm.frozenBoxAreaOptions = data;
                vm.frozenBoxAreaOptions.push({id:"",areaCode:""});
                if(!vm.dto.areaId){
                    vm.dto.areaId = "";
                }
                if(!vm.dto.shelvesId){
                    vm.dto.shelvesId = "";
                }


                if(vm.frozenBoxAreaOptions.length){
                    // vm.dto.areaId = vm.frozenBoxAreaOptions[0].id;
                    SupportacksByAreaIdService.query({id:vm.dto.areaId},onShelfSuccess, onError);
                }
            }
            vm.frozenBoxAreaConfig = {
                valueField:'id',
                labelField:'areaCode',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError);
                    }else{
                        vm.frozenBoxShelfOptions = [
                            {id:"",supportRackCode:""}
                        ];
                        vm.dto.shelvesId = "";
                        $scope.$apply();
                    }
                }
            };
            //架子
            function onShelfSuccess(data) {
                vm.frozenBoxShelfOptions = data;
                vm.frozenBoxShelfOptions.push({id:"",supportRackCode:""});
                if(!vm.dto.shelvesId){
                    vm.dto.shelvesId = "";
                }
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
            var selectizeObj = {
                create : true,
                persist:false,
                onInitializeFlag : true,
                clearMaxItemFlag : true
            };
            vm.boxCodeConfig = BioBankSelectize.buildSettings(selectizeObj);
            vm.boxCodeConfig.onChange = function (value) {
                    vm.dto.frozenBoxCodeStr = value;
            };

            //一维编码
            vm.boxCode1DConfig = BioBankSelectize.buildSettings(selectizeObj);
            vm.boxCode1DConfig.onChange =  function(value){
                vm.dto.frozenBoxCode1DStr = value;
            };

            //样本类型
            vm.sampleTypeConfig = BioBankSelectize.buildSettings({valueField:'id',labelField:'sampleTypeName'});
            vm.sampleTypeConfig.onChange = function(value){
                if(vm.projectIds){
                    _fnQueryProjectSampleClass(vm.projectIds,value);
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
            vm.sampleClassConfig = BioBankSelectize.buildSettings({valueField:'sampleClassificationId', labelField:'sampleClassificationName'});
            //盒子类型 17:10*10 18:8*8
            vm.boxTypeConfig = BioBankSelectize.buildSettings({valueField:'id', labelField:'frozenBoxTypeName'});
        }
        _init();
        function _fnReloadSelectedData() {
            selectedBox = [];
            vm.selectedLen = selectedBox.length;
            vm.selectedOptions.withOption('data', selectedBox);
        }
        function _fnSearchShow(status) {
            vm.status = status;
            vm.checked = true;
        }
        function _fnSearch() {
            if(projectIds.length){
                for(var i = 0; i <projectIds.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }
            vm.checked = false;
            _fnReloadSelectedData();
            vm.dtInstance.rerender();
        }
        function _fnMovement() {
            vm.checked = false;
            setTimeout(function (data) {
                $state.go('box-movement',{selectedBox:selectedBox})
            },50);
        }
        function _fnClose() {
            vm.checked = false;
        }
        function _fnEmpty() {
            vm.dto = {};
            vm.dto.projectCodeStr = [];
            projectIds = [];
            vm.projectCodeStr = [];
            vm.arrayBoxCode = [];
            vm.arrayBoxCode1D = [];
            vm.dto.spaceType = "1";
            vm.dto.compareType = "1";
            // vm.checked = false;
            // vm.dtInstance.rerender();
        }
        function _fnSearchSample(frozenBoxCode) {
            var obj = {
                frozenBoxCode:frozenBoxCode
            };
            $state.go('sample-inventory',obj);
        }
        function _fnExchangeDestroy(operateStatus) {
            // operateStatus:1.交换 2.销毁
            if(operateStatus == 1){
                //状态为已入库才能被销毁或换位
                var putInStorageData = _.filter(selectedBox,{"status":"2004"});
                if(putInStorageData.length != 2){
                    toastr.error("冻存盒未入库，不能换位!");
                    return;
                }
            }


            var ids =  _.map(selectedBox, 'id');
            modalInstance = $uibModal.open({
                templateUrl: 'app/bizs/list/modal/list-exchange-destroy-modal.html',
                controller: 'ListExchangeDestroyModalController',
                controllerAs: 'vm',
                resolve:{
                    items:function () {
                        return{
                            operateStatus:operateStatus
                        }
                    }
                }
            });
            modalInstance.result.then(function (reson) {
                var obj = reson;
                if(operateStatus == '1'){
                    obj.changeId1 = ids[0];
                    obj.changeId2 = ids[1];
                    obj.changeReason = obj.reason;
                    delete obj.reason;
                    BoxInventoryService.changePosition(obj).success(function (data) {
                        toastr.success("换位成功!");
                        _fnReloadSelectedData();
                        vm.dtInstance.rerender();
                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }else{
                    obj.destroyReason = obj.reason;
                    obj.ids = ids;
                    delete obj.reason;
                    BoxInventoryService.destroyBox(obj).success(function (data) {
                        toastr.success("销毁成功!");
                        vm.dtInstance.rerender();
                    }).error(function (data) {
                        toastr.error(data.message);
                    });
                }
            }, function () {
            });
        }

        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "110"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "60")

        ];
        vm.selected = {};
        vm.selectAll = false;
        vm.toggleAll = toggleAll;
        vm.toggleOne = toggleOne;
        function toggleAll (selectAll, selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    selectedItems[id] = selectAll;
                }
            }
        }
        selectedBox = [];
        function toggleOne (selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(selectedItems[id]) {
                        var obj = _.find(vm.BoxData,{id:+id});
                        var len = _.filter(selectedBox,{id:+id}).length;
                        if(!len){
                            selectedBox.push(obj);
                        }
                    }else{
                        var index = [];
                        if(selectedBox.length){
                            for(var i = 0; i < selectedBox.length; i++){
                                if(+id == selectedBox[i].id){
                                    index.push(i);
                                }
                            }
                            _.pullAt(selectedBox, index);
                        }
                    }
                }
            }
            vm.selectedLen = selectedBox.length;
            vm.selectedOptions.withOption('data', selectedBox);
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(!selectedItems[id]) {
                        vm.selectAll = false;
                        return;
                    }
                }
            }
            vm.selectAll = true;
        }

        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false)
            .withOption('order', [[1,'asc']])
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
                    vm.BoxData = res.data.data;
                    var error = json.error || json.sError;
                    if ( error ) {
                        jqDt._fnLog( oSettings, 0, error );
                    }
                    var count = 0;
                    _.forEach(vm.BoxData, function(value) {
                        var len  =  _.filter(selectedBox, {id:value.id}).length;
                        if(len){
                            count++;
                        }
                    });
                    if(count == vm.BoxData.length){
                        vm.selectAll = true;
                    }else{
                        vm.selectAll = false;
                        count = 0;
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
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
                $(header).find(".selectAll").bind('click',function () {
                    if(vm.selectAll){
                        for(var i = 0; i< vm.BoxData.length; i++){
                            var len = _.filter(selectedBox,{id:+vm.BoxData[i].id}).length;
                            if(!len && vm.BoxData[i].status == '2004'){
                                selectedBox.push(vm.BoxData[i])
                            }
                        }
                    }else{
                        _.forEach(vm.BoxData, function(value) {
                            var len  =  _.filter(selectedBox, {id:value.id}).length;
                            if(len){
                                _.remove(selectedBox,{id:value.id});
                            }
                        });
                    }
                    for (var id in vm.selected) {
                        if (vm.selected.hasOwnProperty(id)) {
                            vm.selected[id] = vm.selectAll;
                        }
                    }
                    vm.selectedLen = selectedBox.length;
                    vm.selectedOptions.withOption('data', selectedBox);
                });
                $compile(angular.element(header).contents())($scope);



            });
        var titleHtml = '<input  class="selectAll" type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';

        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml)
                .withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('position').withTitle('位置').withOption("width", "140"),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('冻存盒编码').withOption("width", "90").renderWith(_fnRowBoxCodeRender),
            DTColumnBuilder.newColumn('frozenBoxCode1D').withTitle('一维编码').withOption("width", "90"),
            DTColumnBuilder.newColumn('projectCode').withTitle('项目编码').withOption("width", "80"),
            DTColumnBuilder.newColumn('sampleType').withTitle('样本类型').withOption("width", "80"),
            DTColumnBuilder.newColumn('sampleClassification').withTitle('样本分类').withOption("width", "100"),
            DTColumnBuilder.newColumn('frozenBoxType').withTitle('盒类型').withOption("width", "100"),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用').withOption("width", "60"),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", 'auto'),
            DTColumnBuilder.newColumn('lockFlag').withTitle('是否锁定').withOption("width", '80'),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "60")
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            var len = _.filter(selectedBox,{id:full.id}).length;
            if(len){
                vm.selected[full.id] = true;
            }else{
                vm.selected[full.id] = false;
            }

            var html = '';
            if(full.status == '2004'){
                html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            }
            return html;
        }
        function _fnRowBoxCodeRender(data, type, full, meta) {
            var frozenBoxCode = "'"+full.frozenBoxCode+"'";
            var html = '';
            html = '<a ng-click="vm.searchSample('+frozenBoxCode+')">'+full.frozenBoxCode+'</a>';
            return html;
        }
        function createdRow(row, data, dataIndex) {
            var lock;
            if(data.lockFlag){
                lock = "是";
            }else{
                lock = "否";
            }
            var status = "";
            status = MasterData.getFrozenBoxStatus(data.status);
            $('td:eq(11)', row).html(lock);
            $('td:eq(12)', row).html(status);
            $compile(angular.element(row).contents())($scope);
        }
        function actionsHtml(data, type, full, meta) {
            // return '<button type="button" class="btn btn-xs" ui-sref="plan-edit({planId:'+ full.id +'})">' +
            //     '   <i class="fa fa-edit"></i>' +
            //     '</button>&nbsp;';
            return "";
        }

        function onError(error) {
            // BioBankBlockUi.blockUiStop();
            // toastr.error(error.data.message);
        }
    }
})();
