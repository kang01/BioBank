/**
 * Created by gaokangkang on 2017/5/24.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('RequirementSampleEditModalController', RequirementSampleEditModalController);

    RequirementSampleEditModalController.$inject = ['$scope','$uibModalInstance','$compile','$uibModal','BioBankBlockUi','toastr','MasterData','items','DTOptionsBuilder','DTColumnBuilder','RequirementService','SampleTypeService'];

    function RequirementSampleEditModalController($scope,$uibModalInstance,$compile,$uibModal,BioBankBlockUi,toastr,MasterData,items,DTOptionsBuilder,DTColumnBuilder,RequirementService,SampleTypeService) {
        var vm = this;
        var projectIds = items.projectIds;
        vm.sampleRequirement = {};
        _fnQuerySampleType();
        _fuQueryFrozenTubeType();
        vm.sampleRequirement.age = "30;70";

        vm.sampleTypeConfig = {
            valueField:'id',
            labelField:'sampleTypeName',
            maxItems: 1,
            onChange:function (value) {
                if(value && projectIds){
                    _fuQuerySampleClass(projectIds,value);
                }

            }
        };
        //根据项目和样本类型获取样本分类
        function _fuQuerySampleClass(projectIds,sampleTypeId) {
            RequirementService.queryRequirementSampleClasses(projectIds,sampleTypeId).success(function (data) {
                vm.sampleClassOptions = data;
                if(!vm.sampleRequirement.sampleClassificationId){
                    if(vm.sampleClassOptions.length){
                        vm.sampleRequirement.sampleClassificationId = vm.sampleClassOptions[0].sampleClassificationId;
                        $scope.$apply();
                    }
                }


            })
        }
        //获取样本类型
        function _fnQuerySampleType() {
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data, ['id'], ['esc']);
                vm.sampleTypeOptions.unshift({id:"null",sampleTypeName:"全部"});
                vm.sampleTypeOptions.pop();
                if(!vm.sampleRequirement.sampleTypeId){
                    vm.sampleRequirement.sampleTypeId = vm.sampleTypeOptions[0].id;
                    if(vm.sampleRequirement.sampleTypeId && projectIds){
                        _fuQuerySampleClass(projectIds,vm.sampleRequirement.sampleTypeId);
                    }
                }
            });
        }
        //样本分类
        vm.sampleClassConfig = {
            valueField:'sampleClassificationId',
            labelField:'sampleClassificationName',
            maxItems: 1,
            onChange:function (value) {

            }
        };
        //获取冻存管类型
        function _fuQueryFrozenTubeType() {
            RequirementService.queryFrozenTubeType().success(function (data) {
                vm.frozenTubeTypeOptions = data;
                vm.frozenTubeTypeOptions.unshift({id:"null",frozenTubeTypeName:"全部"})
                if(!vm.sampleRequirement.frozenTubeTypeId){
                    vm.sampleRequirement.frozenTubeTypeId = vm.frozenTubeTypeOptions[0].id;
                }
            })
        }
        vm.frozenTubeTypeConfig = {
            valueField:'id',
            labelField:'frozenTubeTypeName',
            maxItems: 1,
            onChange:function (value) {
                // console.log( _.filter(vm.sampleTypeOptions,{'id':+value})[0].isMixed);
            }
        };
        //性别
        vm.sexOptions = MasterData.sexDict;
        if(!vm.sampleRequirement.sex){
            vm.sampleRequirement.sex = vm.sexOptions[2].type;
        }
        vm.sexConfig = {
            valueField:'type',
            labelField:'name',
            maxItems: 1
        };
        //疾病类型
        vm.diseaseTypeOptions = MasterData.diseaseType;
        if(!vm.sampleRequirement.diseaseTypeId){
            vm.sampleRequirement.diseaseTypeId = vm.diseaseTypeOptions[2].id;
        }
        vm.isAge = true;
        vm.isUseAge = function () {
            if(!vm.isAge){
                vm.sampleRequirement.age = "0;0";
            }else{
                vm.sampleRequirement.age = "30;70"
            }
        };

        vm.diseaseTypeConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1,
            onChange:function (value) {
            }
        };
        //保存样本需求
        vm.saveSampleRequirement = function (file) {
            BioBankBlockUi.blockUiStart();
            //是否上传附件
            if(file){
                var obj = {};
                obj.requirementName = vm.sampleRequirement.requirementName;
                var fb = new FormData();
                fb.append('stockOutRequirement', angular.toJson(obj));
                fb.append('file', file);
                RequirementService.saveSampleRequirementOfUpload(vm.requirement.id,fb).success(function (data) {
                    BioBankBlockUi.blockUiStop();
                    toastr.success("保存样本需求成功！");

                }).error(function (data) {
                    BioBankBlockUi.blockUiStop();
                })
            }else{
                vm.sampleflag = true;
                _fnSaveRequirement();
            }
        };
        //保存申请记录
        function _fnSaveRequirement() {
            delete vm.requirement.stockOutRequirement;
            BioBankBlockUi.blockUiStart();
            RequirementService.saveRequirementInfo(vm.requirement).success(function (data) {
                BioBankBlockUi.blockUiStop();
                if(!vm.sampleflag){
                    toastr.success("保存申请记录成功！");
                    return;
                }
                if(vm.sampleRequirement.id){
                    if(vm.sampleRequirement.status){
                        delete  vm.sampleRequirement.status;
                    }
                    delete  vm.sampleRequirement.samples;
                    RequirementService.saveEditSampleRequirement(vm.requirement.id,vm.sampleRequirement).success(function (data) {
                        BioBankBlockUi.blockUiStop();
                        toastr.success("保存样本需求成功！");
                        vm.sampleRequirement.requirementName = '';
                        vm.sampleRequirement.countOfSample = '';
                    }).error(function (data) {
                        BioBankBlockUi.blockUiStop();
                        toastr.success(data.message);
                    })

                }else{
                    RequirementService.saveSampleRequirement(vm.requirement.id,vm.sampleRequirement).success(function (data) {
                        BioBankBlockUi.blockUiStop();
                        toastr.success("保存样本需求成功！");
                    }).error(function (data) {
                        BioBankBlockUi.blockUiStop();
                    })
                }

            }).error(function (data) {
                BioBankBlockUi.blockUiStop();
                toastr.error("保存申请记录失败！");
            })
        }

        vm.ok = function () {
            $uibModalInstance.close();
        };
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };


    }
})();
