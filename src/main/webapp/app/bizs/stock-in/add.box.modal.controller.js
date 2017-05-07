/**
 * Created by gaoyankang on 2017/4/9.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('AddBoxModalController', AddBoxModalController);

    AddBoxModalController.$inject = ['$uibModalInstance','$uibModal','items','AlertService','FrozenBoxTypesService','EquipmentService','AreasByEquipmentIdService','SupportacksByAreaIdService','BoxCodeIsRepeatService','SampleTypeService'];

    function AddBoxModalController($uibModalInstance,$uibModal,items,AlertService,FrozenBoxTypesService,EquipmentService,AreasByEquipmentIdService,SupportacksByAreaIdService,BoxCodeIsRepeatService,SampleTypeService) {
        var vm = this;
        vm.createBoxflag = false;
        vm.boxes = items.incompleteBoxes;
        var projectId = items.projectId;
        if(!items.box.stockInFrozenTubeList.length){
            vm.createBoxflag = true;
            _createBox();
        }
        var initData = function () {
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);//盒子类型
            // EquipmentService.query({},onEquipmentSuccess, onError);//设备
            _fnQuerySampleType();
        };
        initData();
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['id'], ['esc']);
                vm.sampleTypeOptions.pop();
                vm.box.sampleType = vm.sampleTypeOptions[0];
                vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
            });
        }
        function _fnQueryProjectSampleClasses(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.sampleTypeClassOptions = _.orderBy(data, ['id'], ['esc']);
                vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                vm.box.sampleClassification = vm.sampleTypeClassOptions[0]
            });
        }

        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
            if(!items.box.frozenBoxTypeId){
                vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
                vm.box.frozenBoxType = vm.frozenBoxTypeOptions[0];
                // vm.box.frozenBoxRows = vm.frozenBoxTypeOptions[0].frozenBoxTypeRows;
                // vm.box.frozenBoxColumns = vm.frozenBoxTypeOptions[0].frozenBoxTypeColumns
            }
        }
        // function onEquipmentSuccess(data) {
        //     vm.frozenBoxPlaceOptions = data;
        // }
        // function onAreaSuccess(data) {
        //     vm.frozenBoxAreaOptions = data;
        // }
        // function onShelfSuccess(data) {
        //     vm.frozenBoxShelfOptions = data;
        // }
        function onError(error) {
            AlertService.error(error.data.message);
        }

        function _createBox() {
            if(vm.createBoxflag){
                vm.sampleType = items.box.sampleTypeCode;
                vm.box = {
                    frozenBoxCode:'',
                    memo:'',
                    stockInFrozenTubeList:[]
                };
                if(items.box.stockInFrozenTubeList.length){
                    var m = 0,n = 0;
                    for(var i = 0; i < items.box.stockInFrozenTubeList.length; i++){
                        if(i >= +items.box.frozenBoxRows){
                            m++;n = 0;
                            items.box.stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m+65);
                            items.box.stockInFrozenTubeList[i].tubeColumns = n + 1;
                            items.box.frozenBoxRows = items.box.frozenBoxRows*2;

                        }else{
                            n++;
                            items.box.stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m + 65);
                            items.box.stockInFrozenTubeList[i].tubeColumns = n;
                        }

                    }
                    vm.box.stockInFrozenTubeList = items.box.stockInFrozenTubeList;
                }
                //盒子类型
                if(items.box.frozenBoxTypeId){
                    vm.box.frozenBoxTypeId = items.box.frozenBoxTypeId;
                }
                vm.boxTypeConfig = {
                    valueField:'id',
                    labelField:'frozenBoxTypeName',
                    maxItems: 1,
                    onChange:function(value){
                        vm.box.frozenBoxType = _.filter(vm.frozenBoxTypeOptions,{'id':+value})[0];
                        vm.box.frozenBoxTypeId = value;
                    }
                };
                vm.sampleTypeConfig = {
                    valueField:'id',
                    labelField:'sampleTypeName',
                    maxItems: 1,
                    onChange:function (value) {
                        vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+value})[0];
                        vm.box.sampleTypeId = value;
                        _fnQueryProjectSampleClasses(projectId,value);
                    }
                };
                //样本分类
                vm.sampleTypeClassConfig = {
                    valueField:'sampleClassificationId',
                    labelField:'sampleClassficationName',
                    maxItems: 1,
                    onChange:function (value) {
                    }
                };
                // //设备
                // vm.frozenBoxPlaceConfig = {
                //     valueField:'id',
                //     labelField:'equipmentCode',
                //     maxItems: 1,
                //     onChange:function (value) {
                //         AreasByEquipmentIdService.query({id:value},onAreaSuccess, onError);
                //         for(var i = 0; i < vm.frozenBoxPlaceOptions.length; i++){
                //             if(value == vm.frozenBoxPlaceOptions[i].id){
                //                 vm.box.equipmentCode = vm.frozenBoxPlaceOptions[i].equipmentCode
                //             }
                //         }
                //     }
                // };
                //区域
                // vm.frozenBoxAreaConfig = {
                //     valueField:'id',
                //     labelField:'areaCode',
                //     maxItems: 1,
                //     onChange:function (value) {
                //         for(var i = 0; i < vm.frozenBoxAreaOptions.length; i++){
                //             if(value == vm.frozenBoxAreaOptions[i].id){
                //                 vm.box.areaCode = vm.frozenBoxAreaOptions[i].areaCode
                //             }
                //         }
                //         SupportacksByAreaIdService.query({id:value},onShelfSuccess, onError)
                //
                //     }
                // };
                //架子
                // vm.frozenBoxShelfConfig = {
                //     valueField:'id',
                //     labelField:'supportRackCode',
                //     maxItems: 1,
                //     onChange:function (value) {
                //         for(var i = 0; i < vm.frozenBoxShelfOptions.length; i++){
                //             if(value == vm.frozenBoxShelfOptions[i].id){
                //                 vm.box.supportRackCode = vm.frozenBoxShelfOptions[i].areaCode
                //             }
                //         }
                //     }
                // };

                // items.sampleTypes.pop();
                // if(items.box.sampleTypeCode){
                //     vm.box.sampleType.sampleTypeCode = items.box.sampleTypeCode;
                //     vm.box.sampleTypeCode = items.box.sampleTypeCode;
                // }else{
                //     vm.box.sampleType.sampleTypeCode = items.sampleTypes[0].sampleTypeCode;
                //     vm.box.sampleType.sampleTypeName = items.sampleTypes[0].sampleTypeName;
                //     vm.box.sampleType.backColor = items.sampleTypes[0].backColor;
                //     vm.box.sampleTypeCode = items.sampleTypes[0].sampleTypeCode;
                // }
                // vm.sampleTypesOptions = items.sampleTypes;


                // for(var i =0; i < vm.sampleTypesOptions.length; i++) {
                //     if (items.box.sampleTypeCode == vm.sampleTypesOptions[i].sampleTypeCode) {
                //         vm.box.sampleType.sampleTypeName = vm.sampleTypesOptions[i].sampleTypeName;
                //         vm.box.sampleType.backColor = vm.sampleTypesOptions[i].backColor
                //     }
                // }

                vm.isBoxCodeRepeat = function () {
                    vm.isRepeat = false;
                };
            }
        }

        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            vm.isRepeat = false;
            BoxCodeIsRepeatService.getByCode(vm.box.frozenBoxCode).then(function (data) {
                vm.isRepeat = data;
                if (vm.isRepeat){
                    return;
                }
                data = _.filter(vm.boxes, function(b){
                    var box = _.filter(b.boxList, {frozenBoxCode: vm.box.frozenBoxCode});
                    return box && box.length;
                });
                vm.isRepeat = data && data.length;
                if (vm.isRepeat){
                    return;
                }

                if(vm.boxRowCol){
                    vm.box.columnsInShelf = vm.boxRowCol.charAt(0);
                    vm.box.rowsInShelf = vm.boxRowCol.charAt(vm.boxRowCol.length - 1);
                }
                vm.box.countOfSample = vm.box.stockInFrozenTubeList.length;
                // console.log(JSON.stringify(vm.box));
                $uibModalInstance.close(vm.box);
            });
        };

        vm.yes = function () {
            vm.createBoxflag = true;
            _createBox();
        };
        vm.no = function () {
            $uibModalInstance.close();
        };





    }
})();
