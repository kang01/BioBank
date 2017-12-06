/**
 * Created by gaokangkang on 2017/12/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ImportBoxModalController', ImportBoxModalController);
    ImportBoxModalController.$inject = ['$scope','$q','toastr','$timeout','BioBankDataTable','DTColumnBuilder','$uibModalInstance','$uibModal','items','TranshipBoxService','blockUI','blockUIConfig','AreasByEquipmentIdService','EquipmentAllService','SampleTypeService','TransportRecordService'];

    function ImportBoxModalController($scope,$q,toastr,$timeout,BioBankDataTable,DTColumnBuilder,$uibModalInstance,$uibModal,items,TranshipBoxService,blockUI,blockUIConfig,AreasByEquipmentIdService,EquipmentAllService,SampleTypeService,TransportRecordService) {

        var vm = this;
        //设备
        vm.frozenBoxPlaceOptions = items.frozenBoxPlaceOptions;

        _fnBoxTable();
        _fnBoxInfo();

        //盒子信息
        function _fnBoxInfo() {
            _fnTempPos();
            //暂存位置
            function _fnTempPos(){
                //设备
                vm.frozenBoxPlaceConfigTemp = {
                    valueField:'id',
                    labelField:'equipmentCode',
                    maxItems: 1,
                    onChange:function (value) {
                        if(value){
                            AreasByEquipmentIdService.query({id:value},onAreaTempSuccess, onError);
                        }else{
                            vm.frozenBoxHoldAreaOptions = [
                                {id:"",areaCode:""}
                            ];
                            vm.frozenBox.areaId = "";
                            $scope.$apply();
                        }
                    }
                };
                //区域
                vm.frozenBoxAreaConfigTemp = {
                    valueField:'id',
                    labelField:'areaCode',
                    maxItems: 1,
                    onChange:function (value) {
                        vm.frozenBox.areaId  = value;
                    }
                };

                function onAreaTempSuccess(data) {
                    vm.frozenBoxHoldAreaOptions = data;
                    vm.frozenBox.areaId = vm.frozenBoxHoldAreaOptions[0].id;
                }
            }
        }
        function _fnBoxTable() {
            var columns = [
                {name:"0",width:"auto"},
                {name:"1",width:"auto"},
                {name:"2",width:"80"},
                {name:"3",width:"80"},
                {name:"4",width:"80"},
                {name:"5",width:"80"},
                {name:"6",width:"80"}
            ];
            vm.dtOptions = BioBankDataTable.buildDTOption("BASIC,SEARCHING","260");
            vm.dtColumns = BioBankDataTable.buildDTColumn(columns);
        }

        function onError(data) {}

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
        };


    }

})();
