/**
 * Created by gaoyankang on 2017/4/9.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AddBoxModalController', AddBoxModalController);

    AddBoxModalController.$inject = ['$uibModalInstance','$uibModal','items','AlertService','FrozenBoxTypesService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService'];

    function AddBoxModalController($uibModalInstance,$uibModal,items,AlertService,FrozenBoxTypesService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService) {
        var vm = this;
        vm.box = {
            frozenBoxRows:"",
            frozenBoxColumns:"",
            frozenBoxCode:'',
            isSplit:0,
            sampleTypeCode:"",
            sampleType:{},
            stockInFrozenTubeList:[]
        };
        var loadAll = function () {
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);//盒子类型
            EquipmentService.query({},onEquipmentSuccess, onError);//设备
        };
        loadAll();
        //盒子类型
        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
        }
        vm.box.frozenBoxTypeId = items.box.frozenBoxTypeId;
        if(items.box.frozenBoxTypeId == 17){
            vm.box.frozenBoxRows = 10;
            vm.box.frozenBoxColumns = 10;
        }
        if(items.box.frozenBoxTypeId == 18){
            vm.box.frozenBoxRows = 8;
            vm.box.frozenBoxColumns = 8;
        }
        vm.boxTypeConfig = {
            valueField:'id',
            labelField:'frozenBoxTypeName',
            maxItems: 1,
            onChange:function(value){
                if(value == 17){
                    vm.box.frozenBoxRows = 10;
                    vm.box.frozenBoxColumns = 10;
                }else{
                    vm.box.frozenBoxRows = 8;
                    vm.box.frozenBoxColumns = 8;
                }
            }
        };
        //设备
        function onEquipmentSuccess(data) {
            vm.frozenBoxPlaceOptions = data;
        }
        vm.frozenBoxPlaceConfig = {
            valueField:'id',
            labelField:'equipmentCode',
            maxItems: 1,
            onChange:function (value) {
                AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                for(var i = 0; i < vm.frozenBoxPlaceOptions.length; i++){
                    if(value == vm.frozenBoxPlaceOptions[i].id){
                        vm.box.equipmentCode = vm.frozenBoxPlaceOptions[i].equipmentCode
                    }
                }
            }
        };
        //区域
        function onAreaSuccess(data) {
            vm.frozenBoxAreaOptions = data;

        }
        vm.frozenBoxAreaConfig = {
            valueField:'id',
            labelField:'areaCode',
            maxItems: 1,
            onChange:function (value) {
                for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                    if(value == vm.frozenBoxAreaOptions[i].id){
                        vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode
                    }
                }
                SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError)

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
                        vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode
                    }
                }
            }
        };
        function onError(error) {
            AlertService.error(error.data.message);
        }

        vm.sampleTypesOptions = items.sampleTypes;
        vm.box.sampleType.sampleTypeCode = items.box.sampleTypeCode;
        for(var i =0; i < vm.sampleTypesOptions.length; i++) {
            if (items.box.sampleTypeCode == vm.sampleTypesOptions[i].sampleTypeCode) {
                vm.box.sampleType.sampleTypeName = vm.sampleTypesOptions[i].sampleTypeName;
                vm.box.sampleType.backColor = vm.sampleTypesOptions[i].backColor
            }
        }
        vm.sampleTypesConfig = {
            valueField:'sampleTypeCode',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                vm.box.sampleTypeCode = value;
                for(var i =0; i < vm.sampleTypesOptions.length; i++){
                    if(value == vm.sampleTypesOptions[i].sampleTypeCode){
                        vm.box.sampleType.sampleTypeName = vm.sampleTypesOptions[i].sampleTypeName;
                        vm.box.sampleType.backColor = vm.sampleTypesOptions[i].backColor
                    }
                }
            }
        };





        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            if(vm.boxRowCol){
                vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                vm.box.rowsInShelf = vm.boxRowCol.charAt(vm.boxRowCol.length - 1);
            }
            vm.box.countOfSample = 0;
            $uibModalInstance.close(vm.box);
        };
    }
})();
