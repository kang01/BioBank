/**
 * Created by gaokangkang on 2017/6/30.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('EquipmentMovementController', EquipmentMovementController);

    EquipmentMovementController.$inject = ['$scope','$compile','$state','$stateParams','DTColumnBuilder','ProjectService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','EquipmentInventoryService','BioBankDataTable'];

    function EquipmentMovementController($scope,$compile,$state,$stateParams,DTColumnBuilder,ProjectService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,EquipmentInventoryService,BioBankDataTable) {
        var vm = this;
        vm.dtInstance = {};
        vm.dto = {};
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        var selectedEquipment = $stateParams.selectedEquipment;
        function _init() {

        }
        _init();





        vm.selectedOptions = BioBankDataTable.buildDTOption("NORMALLY", null, 10)
            .withOption('searching', false);


        vm.selectedColumns = [
            DTColumnBuilder.newColumn('equipmentType').withTitle('设备类型'),
            // DTColumnBuilder.newColumn('equipmentCode').withTitle('设备'),
            // DTColumnBuilder.newColumn('areaCode').withTitle('区域'),
            // DTColumnBuilder.newColumn('shelvesCode').withTitle('架子'),
            DTColumnBuilder.newColumn('shelvesType').withTitle('架子类型'),
            // DTColumnBuilder.newColumn('countOfUsed').withTitle('已用'),
            // DTColumnBuilder.newColumn('countOfRest').withTitle('剩余'),
            // DTColumnBuilder.newColumn('status').withTitle('状态'),
            // DTColumnBuilder.newColumn("").withTitle('操作').withOption('searchable',false).notSortable().renderWith(actionsHtml),
            // DTColumnBuilder.newColumn('id').notVisible()
        ];
        vm.selectedOptions.withOption('data', selectedEquipment);
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
            DTColumnBuilder.newColumn('equipmentCode').withTitle('设备'),
            DTColumnBuilder.newColumn('areaCode').withTitle('区域'),
            DTColumnBuilder.newColumn('shelvesCode').withTitle('架子'),
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
            switch (data.status){
                case '0001': status = '运行中';break;
            }
            $('td:eq(7)', row).html(status);
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
