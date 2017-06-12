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
        // console.log(JSON.stringify(boxes));
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
        //无样本分类，不是混合类型，盒子只能手动创建1个
        if(vm.isMixed == "0" && !sampleTypeClassId){

        }
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
                    vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id': + vm.box.sampleTypeId})[0]
                }

                _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
            });
        }
        function _fnQueryProjectSampleClasses(projectId,sampleTypeId) {
            SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                vm.sampleTypeClassOptions = _.orderBy(data, ['sampleClassificationId'], ['asc']);
                if(sampleTypeClassId){
                    for(var i = 0; i < boxes.length; i++){
                        for(var j = 0; j < vm.sampleTypeClassOptions.length; j++){
                            if(boxes[i].sampleTypeId == vm.sampleTypeClassOptions[j].sampleClassificationId){
                                _.pullAt(vm.sampleTypeClassOptions,j);
                            }
                        }
                    }
                    //样本类型下的样本分类为空时，样本类型也应该不存在
                    if(!vm.sampleTypeClassOptions.length){
                        for(var m = 0; m < vm.sampleTypeOptions.length; m++){
                            if(sampleTypeId == vm.sampleTypeOptions[m].id){
                                _.pullAt(vm.sampleTypeOptions,m);


                            }
                        }
                        if(vm.sampleTypeOptions.length){
                            vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                            _fnQueryProjectSampleClasses(projectId,vm.box.sampleTypeId);
                        }
                    }
                }


                if(vm.sampleTypeClassOptions.length){
                    vm.box.sampleClassificationId = vm.sampleTypeClassOptions[0].sampleClassificationId;
                    vm.box.sampleClassification = vm.sampleTypeClassOptions[0];
                    sampleTypeClassId = vm.box.sampleClassificationId;
                }
                if(countFlag){
                    //创建第一个新盒子，空管子
                    if(!items.box.stockInFrozenTubeList.length){
                        vm.createBoxflag = true;
                        _createBox();
                    }
                    countFlag = false;
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

        function _createBox() {
            if(vm.createBoxflag){
                vm.box = {
                    frozenBoxCode:'',
                    memo:'',
                    stockInFrozenTubeList:[]
                };
                if(items.box.stockInFrozenTubeList.length){
                    var rows = +items.box.sampleType.frozenBoxTypeRows;
                    var cols = +items.box.sampleType.frozenBoxTypeColumns;
                    var m = 0,n = 0;
                    for(var i = 0; i < items.box.stockInFrozenTubeList.length; i++){
                        if(i >= rows){
                            m++;n = 0;
                            items.box.stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m+65);
                            items.box.stockInFrozenTubeList[i].tubeColumns = n + 1;

                        }else{
                            n++;
                            items.box.stockInFrozenTubeList[i].tubeRows = String.fromCharCode(m + 65);
                            items.box.stockInFrozenTubeList[i].tubeColumns = n;
                        }

                    }
                    vm.box.stockInFrozenTubeList = items.box.stockInFrozenTubeList;
                }
                //盒子类型
                if(frozenBoxTypeId){
                    vm.box.frozenBoxTypeId = frozenBoxTypeId;
                    vm.box.frozenBoxType = _.filter(vm.frozenBoxTypeOptions,{'id':+vm.box.frozenBoxTypeId})[0];
                }
                //样本类型为混合型时，同时有样本分类
                if(vm.isMixed == "1"){
                    vm.box.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                }else{
                    vm.box.sampleTypeId = sampleTypeId;
                    vm.box.sampleType = _.filter(vm.sampleTypeOptions,{'id':+vm.box.sampleTypeId})[0];
                }
                //有无样本分类
                if(sampleTypeClassId){
                    vm.box.sampleClassificationId = sampleTypeClassId;
                    vm.box.sampleClassification = _.filter(vm.sampleTypeClassOptions,{'sampleClassificationId':+vm.box.sampleClassificationId})[0];
                }else{
                    vm.box.sampleClassificationId = "";
                    vm.box.sampleClassification = "";
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
                        // if(sampleTypeClassId){
                            _fnQueryProjectSampleClasses(projectId,value);
                        // }

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
                // console.log(JSON.stringify(vm.box))
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
