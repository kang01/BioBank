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
        var boxes = items.incompleteBoxes;
        var stockInFrozenTubeList = angular.copy(items.box.stockInFrozenTubeList);
        vm.box = {};
        var projectId = items.projectId;
        //是否混合类型 1：是
        vm.isMixed = items.isMixed;
        //样本类型
        var sampleTypeId = items.sampleTypeId;
        //样本分类
        var sampleTypeClassId = items.sampleTypeClassId;
        //盒类型Id
        var frozenBoxTypeId = items.frozenBoxTypeId;

        var countFlag = true;
        var initData = function () {
            //盒子类型
            FrozenBoxTypesService.query({},onFrozenBoxTypeSuccess, onError);
            _fnQuerySampleType();
        };
        initData();
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['id'], ['asc']);
                //去除混合类型
                vm.sampleTypeOptions.pop();

                if(vm.isMixed == "1"){
                    vm.box.sampleType = vm.sampleTypeOptions[0];
                    vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                }else{
                    vm.box.sampleTypeId = sampleTypeId;
                    vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id': + vm.box.sampleTypeId})[0];
                }

                _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
            });
        }
        vm.fullBoxFlag = false;
        function _fnQueryProjectSampleClasses(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.sampleTypeClassOptions = _.orderBy(data, ['sampleClassificationId'], ['asc']);
                if(vm.sampleTypeClassOptions.length){
                    vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                    vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                }

                if(countFlag){
                    //创建第一个新盒子，空管子
                    if(!stockInFrozenTubeList.length){
                        vm.createBoxflag = true;
                        _createBox();
                    }else{
                        vm.fullBoxFlag = true;
                    }
                    // countFlag = false;
                }

            });
        }

        function onFrozenBoxTypeSuccess(data) {
            vm.frozenBoxTypeOptions = data;
            if(!items.box.frozenBoxTypeId){
                vm.box.frozenBoxTypeId = vm.frozenBoxTypeOptions[0].id;
                vm.box.frozenBoxType = vm.frozenBoxTypeOptions[0];
            }
        }
        function onError(error) {
            AlertService.error(error.data.message);
        }
        vm.sampleFlag = true;
        //是否手动创建盒子

        function _createBox() {
            if(vm.createBoxflag){
                vm.box.frozenBoxCode="";
                vm.box.memo = "";
                vm.box.stockInFrozenTubeList = "";
                // vm.box = {
                //     frozenBoxCode:'',
                //     memo:'',
                //     stockInFrozenTubeList:[]
                // };
                if(stockInFrozenTubeList.length){
                    var rows = +items.box.sampleType.frozenBoxTypeRows;
                    var cols = +items.box.sampleType.frozenBoxTypeColumns;
                    var m = 0,n = 0;
                    for(var i = 0; i < stockInFrozenTubeList.length; i++){
                        if(i >= rows){
                            m++;n = 0;
                            stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m+65);
                            stockInFrozenTubeList[i].tubeColumns = n + 1;

                        }else{
                            n++;
                            stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m + 65);
                            stockInFrozenTubeList[i].tubeColumns = n;
                        }

                    }
                    vm.box.stockInFrozenTubeList = stockInFrozenTubeList;
                }
                //盒子类型
                if(frozenBoxTypeId){
                    vm.box.frozenBoxTypeId = frozenBoxTypeId;
                    vm.box.frozenBoxType = _.filter(vm.frozenBoxTypeOptions,{'id':+vm.box.frozenBoxTypeId})[0];
                }



                //不是混合类型、有分类
                if(vm.isMixed != "1" && sampleTypeClassId){
                    if(!vm.fullBoxFlag){
                        for(var i = 0; i < boxes.length; i++){
                            for(var j = 0; j < vm.sampleTypeClassOptions.length; j++){
                                if(boxes[i].sampleTypeId == vm.sampleTypeClassOptions[j].sampleClassificationId){
                                    _.pullAt(vm.sampleTypeClassOptions,j);
                                }
                            }
                        }
                        vm.box.sampleTypeId = sampleTypeId;
                        vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                        if(vm.sampleTypeClassOptions.length){
                            vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                            vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                            sampleTypeClassId = vm.box.sampleClassificationId;
                        }else{
                            vm.sampleFlag = false;
                        }
                    }else{

                        if(vm.sampleTypeClassOptions.length){
                            vm.box.sampleClassificationId = _.filter(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId})[0].sampleClassificationId;
                            vm.box.sampleClassification = _.filter(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId})[0];
                        }
                    }

                }
                //是混合、无分类
                if(vm.isMixed == "1" && !sampleTypeClassId){
                    if(!vm.fullBoxFlag){
                        for(var i = 0; i < boxes.length; i++){
                            for(var j = 0; j < vm.sampleTypeOptions.length; j++){
                                if(boxes[i].sampleTypeId == vm.sampleTypeOptions[j].id){
                                    _.pullAt(vm.sampleTypeOptions,j);
                                }
                            }
                        }
                        if(vm.sampleTypeOptions.length){
                            vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                            vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                        }else{
                            vm.sampleFlag = false;
                        }
                    }else{
                        if(vm.sampleTypeOptions.length){
                            vm.box.sampleTypeId = _.filter(vm.sampleTypeOptions,{id:items.box.sampleTypeId})[0].id;
                            vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+items.box.sampleTypeId})[0];
                        }
                    }


                }

                if(!vm.fullBoxFlag){
                    // 是混合、有分类
                    if(vm.isMixed == "1" && sampleTypeClassId){
                        for(var i = 0; i < boxes.length; i++){
                            for(var j = 0; j < vm.sampleTypeClassOptions.length; j++){
                                if(boxes[i].sampleTypeId == vm.sampleTypeClassOptions[j].sampleClassificationId){
                                    _.pullAt(vm.sampleTypeClassOptions,j);
                                }
                            }
                        }

                        vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                        vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                        countFlag = true;
                        //样本类型下的样本分类为空时，样本类型也应该不存在
                        if(!vm.sampleTypeClassOptions.length){
                            for(var k = 0; k < vm.sampleTypeOptions.length; k++){
                                if(vm.box.sampleTypeId == vm.sampleTypeOptions[k].id){
                                    _.pullAt(vm.sampleTypeOptions,k);
                                }
                            }

                            if(vm.sampleTypeOptions.length){
                                vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                                vm.box.sampleType = vm.sampleTypeOptions[0];
                                _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
                            }else{
                                vm.sampleFlag = false;
                            }
                        }else{
                            vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                            vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                            sampleTypeClassId = vm.box.sampleClassificationId;
                        }
                    }
                    //不是混合、无样本分类
                    if(vm.isMixed != "1" && !sampleTypeClassId){
                        if(boxes.length){
                            vm.sampleFlag = false;
                        }
                    }

                }else{
                    if(vm.sampleTypeClassOptions.length){
                        vm.box.sampleClassificationId = _.filter(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId})[0].sampleClassificationId;
                        vm.box.sampleClassification = _.filter(vm.sampleTypeClassOptions,{sampleClassificationId:sampleTypeClassId})[0];
                    }


                }




                // //样本类型为混合型时，同时有样本分类
                // if(vm.isMixed == "1"){
                //     vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                //     vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                // }else{
                //     vm.box.sampleTypeId = sampleTypeId;
                //     vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                // }
                // //有无样本分类
                // if(sampleTypeClassId){
                //     vm.box.sampleClassificationId = sampleTypeClassId;
                //     vm.box.sampleClassification = _.filter(vm.sampleTypeClassOptions,{'sampleClassificationId':+vm.box.sampleClassificationId})[0];
                // }else{
                //     vm.box.sampleClassificationId = "";
                //     vm.box.sampleClassification = "";
                // }




            }
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
                countFlag = false;
                _fnQueryProjectSampleClasses(projectId,value);


            }
        };
        //样本分类
        vm.sampleTypeClassConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {
                vm.box.sampleClassification = _.filter(vm.sampleTypeClassOptions,{'sampleClassificationId':+value})[0];
                vm.box.sampleClassificationId = value;
            }
        };

        vm.isBoxCodeRepeat = function () {
            vm.isRepeat = false;
        };
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
                data = _.filter(boxes, function(b){
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
                $uibModalInstance.close(vm.box);
            });
        };

        vm.yes = function () {
            vm.createBoxflag = true;
            _createBox();
        };
        vm.no = function () {
            $uibModalInstance.dismiss('cancel');
        };

    }
})();
