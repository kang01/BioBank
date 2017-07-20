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
        vm.checked = false;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.dto = {};
        var selectedEquipment;
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
                {value:"0001",label:"运行中"},
                {value:"0002",label:"申请移出"},
                {value:"0003",label:"申请移入"},
                {value:"0004",label:"申请换位"},
                {value:"0005",label:"已停用"}
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
            //已用
            vm.dto.spaceType = "1";
            //大于
            vm.dto.compareType = "1";
            // vm.dto.projectCodeStr = [];
        }
        _init();
        var projectIds = [];
        vm.projectConfig = {
            valueField:'id',
            labelField:'projectName',
            onChange:function(value){
                projectIds = value;
            }
        };
        //盒子位置
        vm.frozenBoxPlaceConfig = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
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
            }
        };
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;
            vm.frozenBoxAreaOptions.push({id:"",areaCode:""});
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
            // vm.dto.shelvesId = vm.frozenBoxShelfOptions[0].id;
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
        vm.close = _fnClose;
        vm.empty = _fnEmpty;
        function _fnSearchShow(status) {
            vm.status = status;
            vm.checked = true;
        }

        function _fnSearch() {
            vm.dto.projectCodeStr = [];
            if(projectIds.length){
                for(var i = 0; i <projectIds.length; i++){
                    var projectCode = _.find(vm.projectOptions,{id:+projectIds[i]}).projectCode;
                    vm.dto.projectCodeStr.push(projectCode)
                }
            }

            vm.checked = false;
            vm.dtInstance.rerender();
        }

        function _fnMovement() {
            vm.checked = false;
            setTimeout(function () {
                $state.go('equipment-movement',{selectedEquipment:selectedEquipment})
            },50);
        }
        function _fnClose() {
            vm.checked = false;
        }
        function _fnEmpty() {
            vm.dto = {};
            vm.dto.spaceType= "1";
            vm.dto.compareType= "1";
            vm.projectCodeStr = [];
            projectIds = [];
            vm.checked = false;
            vm.dtInstance.rerender();
        }

        vm.selectedOptions = BioBankDataTable.buildDTOption("BASIC", null, 10);
        vm.selectedColumns = [
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型'),
            DTColumnBuilder.newColumn('shelvesType').withTitle('架子类型')
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
        selectedEquipment = [];
        function toggleOne (selectedItems) {
            for (var id in selectedItems) {
                if (selectedItems.hasOwnProperty(id)) {
                    if(selectedItems[id]) {
                        var obj = _.find(vm.equipmentData,{id:+id});
                        var len = _.filter(selectedEquipment,{id:+id}).length;
                        if(!len){
                            selectedEquipment.push(obj);
                        }

                    }else{
                        var index = [];
                        if(selectedEquipment.length){
                            for(var i = 0; i < selectedEquipment.length; i++){
                                if(+id == selectedEquipment[i].id){
                                    index.push(i);
                                }
                            }
                            _.pullAt(selectedEquipment, index);
                        }
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
            .withOption('order', [[2,'asc']])
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
                    var count = 0;
                    _.forEach(vm.equipmentData, function(value) {
                       var len  =  _.filter(selectedEquipment, {id:value.id}).length;
                       if(len){
                           count++;
                       }
                    });
                    if(count == vm.equipmentData.length){
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
                        for(var i = 0; i< vm.equipmentData.length; i++){
                            var len = _.filter(selectedEquipment,{id:+vm.equipmentData[i].id}).length;
                            if(!len){
                                selectedEquipment.push(vm.equipmentData[i])
                            }
                        }
                    }else{
                        _.forEach(vm.equipmentData, function(value) {
                            var len  =  _.filter(selectedEquipment, {id:value.id}).length;
                            if(len){
                                _.remove(selectedEquipment,{id:value.id});
                            }
                        });
                    }
                    for (var id in vm.selected) {
                        if (vm.selected.hasOwnProperty(id)) {
                            vm.selected[id] = vm.selectAll;
                        }
                    }
                    vm.selectedLen = selectedEquipment.length;
                    vm.selectedOptions.withOption('data', selectedEquipment);
                });
                $compile(angular.element(header).contents())($scope);

            });
        var titleHtml = '<input  class="selectAll" type="checkbox" ng-model="vm.selectAll">';
        vm.dtColumns = [
            DTColumnBuilder.newColumn("").withOption("width", "30").withTitle(titleHtml)
                .withOption('searchable',false).notSortable().renderWith(_fnRowSelectorRender),
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型').withOption("width", "100"),
            DTColumnBuilder.newColumn('position').withTitle('位置').withOption("width", "140"),
            DTColumnBuilder.newColumn('shelvesType').withTitle('架子类型').withOption("width", "60"),
            DTColumnBuilder.newColumn('countOfUsed').withTitle('已用').withOption("width", "60"),
            DTColumnBuilder.newColumn('countOfRest').withTitle('剩余').withOption("width", "60"),
            DTColumnBuilder.newColumn('memo').withTitle('备注').withOption("width", "auto"),
            DTColumnBuilder.newColumn('status').withTitle('状态').withOption("width", "60"),
            DTColumnBuilder.newColumn("").withTitle('操作').withOption("width", "60")
                .withOption('searchable',false).notSortable().renderWith(actionsHtml)
        ];
        function _fnRowSelectorRender(data, type, full, meta) {
            var len = _.filter(selectedEquipment,{id:full.id}).length;
            if(len){
                vm.selected[full.id] = true;
            }else{
                vm.selected[full.id] = false;
            }
            var html = '';
            html = '<input type="checkbox" ng-model="vm.selected[' + full.id + ']" ng-click="vm.toggleOne(vm.selected)">';
            return html;
        }
        function createdRow(row, data, dataIndex) {
            var status = '';
            switch (data.status){
                case '0001': status = '运行中';break;
            }

            var countOfUsed = data.countOfUsed;
            var countOfRest = data.countOfRest;
            var total = countOfUsed+countOfRest;
            var progressStyle = "width:"+countOfUsed/total*100+"%";
            var progress = ""+countOfUsed + "/" + total;
            var html;
            html = "<div class='pos-progress'> " +
                "<div class='text'>"+progress+"</div>" +
                "<div class='Bar' style ='"+progressStyle+"'> " +

                " </div> " +
                "</div>";



            $('td:eq(7)', row).html(status);
            $('td:eq(4)', row).html(html);
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
