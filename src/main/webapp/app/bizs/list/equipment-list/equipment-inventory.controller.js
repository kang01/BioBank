/**
 * Created by gaokangkang on 2017/6/23.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentInventoryController', EquipmentInventoryController);

    EquipmentInventoryController.$inject = ['$scope','$compile','$state','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable'];

    function EquipmentInventoryController($scope,$compile,$state,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dto = {};
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
            EquipmentInventoryService.querySupportRackTypes().success(function (data) {
                vm.shelvesOptions = data;
            });

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
            ];
            vm.dto.spaceType = "1";
            vm.dto.compareType = "1";
        }
        _init();
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            onChange:function(value){
                vm.dto.projectCodeStr = [];
                for(var i = 0; i <value.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+value[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
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
                // for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                //     if(value == vm.frozenBoxShelfOptions[i].id){
                //         vm.dto.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode;
                //     }
                // }
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
            valueField:'id',
            labelField:'supportRackTypeCode',
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


        vm.dtInstance = {};
        vm.searchShow = _fnSearchShow;
        vm.search = _fnSearch;
        vm.selectedShow = _fnSearchShow;
        vm.movement = _fnMovement;
        function _fnSearchShow(status) {
            vm.status = status;
            vm.checked = true;
        }

        function _fnSearch() {
            vm.checked = false;
            vm.dtInstance.rerender();
        }

        function _fnMovement() {
            var obj = {};
            obj.selectedEquipment = selectedEquipment;
            $state.go('equipment-movement',obj)
        }


        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);


        vm.selectedColumns = [
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型'),
            // DTColumnBuilder.newColumn('equipmentCode').withTitle('设备'),
            // DTColumnBuilder.newColumn('areaCode').withTitle('区域'),
            // DTColumnBuilder.newColumn('shelvesCode').withTitle('架子'),
            DTColumnBuilder.newColumn('shelvesType').withTitle('架子类型')
            // DTColumnBuilder.newColumn('countOfUsed').withTitle('已用'),
            // DTColumnBuilder.newColumn('countOfRest').withTitle('剩余'),
            // DTColumnBuilder.newColumn('status').withTitle('状态'),
            // DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
            // DTColumnBuilder.newColumn('id').notVisible()
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
        var selectedEquipment;
        function toggleOne (selectedItems) {
            var selectedEquipment = [];
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(selectedItems[id]) {
                        var obj = _.find(vm.equipmentData,{id:+id});
                        selectedEquipment.push(obj);
                    }
                }
            }
            vm.selectedLen = selectedEquipment.length;
            vm.selectedOptions.withOption('data', selectedEquipment);
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
                EquipmentInventoryService.queryEquipmentList(data,searchForm).then(function (res){
                    var json = res.data;
                    vm.equipmentData = res.data.data;
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
            .withOption('headerCallback', function(header) {
                $compile(angular.element(header).contents())($scope);
            });
        var titleHtml = '<input type="checkbox" ng-model="vm.selectAll" ng-click="vm.toggleAll()">';
        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml)
                .withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型'),
            DTColumnBuilder.newColumn('equipmentCode').withTitle('位置'),
            // DTColumnBuilder.newColumn('areaCode').withTitle('区域'),
            // DTColumnBuilder.newColumn('shelvesCode').withTitle('架子'),
            DTColumnBuilder.newColumn('shelvesType').withTitle('架子类型'),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用'),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余'),
            DTColumnBuilder.newColumn('status').withTitle('状态'),
            DTColumnBuilder.newColumn("").withTitle('操作')
                .withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            vm.selected[full.id] = false;
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }
        function createdRow(row, data, dataIndex) {
            var status = '';
            var position = data.equipmentCode+"."+data.areaCode+"."+data.shelvesCode;
            switch (data.status){
                case '0001': status = '运行中';break;
            }
            $('td:eq(6)', row).html(status);
            $('td:eq(2)', row).html(position);
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
