/**
 * Created by gaokangkang on 2017/5/12.
 * 计划中出库任务列表详情
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskStockOutModalController', TaskStockOutModalController);

    TaskStockOutModalController.$inject = ['$scope','$compile','$uibModalInstance','$uibModal','items','TaskService','DTOptionsBuilder','DTColumnBuilder','toastr','EquipmentService','AreasByEquipmentIdService','BioBankBlockUi','BioBankDataTable'];

    function TaskStockOutModalController($scope,$compile,$uibModalInstance,$uibModal,items,TaskService,DTOptionsBuilder,DTColumnBuilder,toastr,EquipmentService,AreasByEquipmentIdService,BioBankBlockUi,BioBankDataTable) {
        var vm = this;
        vm.dtInstance = {};
        vm.stockOut = {};
        var frozenBoxIds = items.frozenBoxIds;
        var taskId = items.taskId;
        vm.stockOutHeadName1 = items.stockOutHeadName1;
        vm.stockOutHeadName2 = items.stockOutHeadName2;


        //设备
        EquipmentService.query({},onEquipmentSuccess, onError);
        function onEquipmentSuccess(data) {
            vm.frozenBoxPlaceOptions = data;
        }
        vm.frozenBoxPlaceConfig = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
                AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
            }
        };
        //区域
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;
            vm.stockOut.areaId = vm.frozenBoxAreaOptions[0].id;
        }
        vm.frozenBoxAreaConfig = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                vm.stockOut.areaId = value;
            }
        };
        //样本列表
        vm.dtOptions = BioBankDataTable.buildDTOption("NORMALLY", 248, 8)
            .withOption('serverSide',true)
            .withFnServerData(function ( sSource, aoData, fnCallback, oSettings ) {
                var data = {};
                for(var i=0; aoData && i<aoData.length; ++i){
                    var oData = aoData[i];
                    data[oData.name] = oData.value;
                }
                var jqDt = this;
                if(frozenBoxIds){
                    TaskService.queryOutputDes(frozenBoxIds,data).then(function (res){
                        var json = res.data;
                        var error = json.error || json.sError;
                        if ( error ) {
                            jqDt._fnLog( oSettings, 0, error );
                        }
                        oSettings.json = json;
                        fnCallback( json );
                    }, function(res){
                        var error = res.data;
                        // console.log(res);
                        //
                        var ret = jqDt._fnCallbackFire( oSettings, null, 'xhr', [oSettings, null, oSettings.jqXHR] );

                        if ( $.inArray( true, ret ) === -1 ) {
                            if ( error === "parsererror" ) {
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
                    };
                    fnCallback( array );

                }

            })
            .withOption('createdRow', createdRow);
        vm.dtColumns = [
            DTColumnBuilder.newColumn('sampleCode').withTitle('样本编码'),
            DTColumnBuilder.newColumn('sampleTypeName').withTitle('样本类型'),
            DTColumnBuilder.newColumn('frozenBoxCode').withTitle('临时盒编码'),
            DTColumnBuilder.newColumn('sex').withTitle('性别'),
            DTColumnBuilder.newColumn('age').withTitle('年龄'),
            DTColumnBuilder.newColumn('diseaseTypeId').withTitle('标签'),
            DTColumnBuilder.newColumn('id').notVisible()
        ];
        function createdRow(row, data, dataIndex) {
            var sex = '';
            switch (data.sex){
                case 'M': sex = '男';break;
                case 'F': sex = '女';break;
                case 'null': sex = '不详';break;
            }
            $('td:eq(3)', row).html(sex);
            $compile(angular.element(row).contents())($scope);
        }



        vm.ok = function () {
            TaskService.saveOutput(taskId,frozenBoxIds,vm.stockOut).success(function (data) {
                toastr.success("出库成功!");
                $uibModalInstance.close();
            }).error(function (data) {
                toastr.error(data.message);
            });

        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


        function onError(error) {
            BioBankBlockUi.blockUiStop();
            toastr.error(error.data.message);
        }


    }
})();
