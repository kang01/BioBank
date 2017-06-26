/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAddSampleModal', StockInAddSampleModal);

    StockInAddSampleModal.$inject = ['$uibModalInstance','items','toastr','SampleTypeService','ProjectService','ProjectSitesByProjectIdService','MasterData'];

    function StockInAddSampleModal($uibModalInstance,items,toastr,SampleTypeService,ProjectService,ProjectSitesByProjectIdService,MasterData) {
        var vm = this;
        vm.status = items.status;
        vm.tubes = items.tubes;
        vm.entity = {
            sampleClassificationId:"",
            sampleClassificationName:""
        };
        vm.entity.projectId = items.projectId;
        vm.entity.projectSiteId = items.projectSiteId;

        vm.entity.sampleCode = items.sampleCode;
        vm.tubeStatusOptions = MasterData.frozenTubeStatus;
        vm.tubeStatusConfig = {
            valueField:'id',
            labelField:'name',
            maxItems: 1,
            onChange:function(value){
            }
        };
        vm.entity.status = vm.tubeStatusOptions[0].id;

        vm.sampleTypeName = items.sampleTypeName;
        if(vm.sampleTypeName != "98"){
            vm.entity.sampleTypeId = items.sampleTypeId;
            vm.entity.sampleTypeName = items.sampleTypeName;
            vm.entity.sampleClassificationId = items.sampleClassificationId;
            vm.entity.sampleClassificationName = items.sampleClassificationName;

        }
        // var oldTube = items.oldTube;
        vm.sampleBoxSelect = _fnSampleBoxSelect;
        var tube;

        function _init() {
            //项目
            ProjectService.query({},onProjectSuccess, onError);
            function onProjectSuccess(data) {
                vm.projectOptions = data;
                if(!vm.entity.projectId){
                    vm.entity.projectId = data[0].id;
                }
                vm.entity.projectCode = _.find(vm.projectOptions,{id:vm.entity.projectId}).projectCode;
                ProjectSitesByProjectIdService.query({id:vm.entity.projectId},onProjectSitesSuccess,onError);
            }
            //项目
            vm.projectConfig = {
                valueField:'id',
                labelField:'projectName',
                maxItems: 1,
                onChange:function(value){
                    vm.entity.projectSiteId = "";
                    ProjectSitesByProjectIdService.query({id:value},onProjectSitesSuccess,onError);
                }
            };
            //项目点
            vm.projectSitesConfig = {
                valueField:'id',
                labelField:'projectSiteName',
                maxItems: 1,
                onChange:function (value) {
                }
            };

            function onProjectSitesSuccess(data) {
                vm.projectSitesOptions = data;
                if(!vm.entity.projectSiteId){
                    if(data.length){
                        vm.entity.projectSiteId = data[0].id;
                    }
                }
            }


            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','desc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"99"});
                _.remove(vm.sampleTypeOptions,{sampleTypeName:"98"});
                if(vm.sampleTypeName == "98"){
                    vm.entity.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:vm.entity.sampleTypeId}).backColor;
                }
                _fnQueryProjectSampleClass(vm.entity.projectId,vm.entity.sampleTypeId);
            });
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    _fnQueryProjectSampleClass(vm.entity.projectId,value);
                    vm.entity.sampleTypeId = value;
                    vm.entity.sampleTypeName = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeName;
                    vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+value}).backColor;
                }
            };
            vm.queryProjectSampleClass = _fnQueryProjectSampleClass;
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    if(vm.sampleTypeName == "98"){
                        if(vm.projectSampleTypeOptions.length){
                            if(!vm.entity.sampleClassificationId){
                                vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                vm.entity.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:vm.entity.sampleClassificationId}).backColor;
                            }
                            }
                    }


                });
            }
            vm.projectSampleTypeConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    vm.entity.sampleClassificationId = value;
                    vm.entity.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                    vm.entity.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).backColor;
                }
            };


        }
        _init();


        function _fnSampleBoxSelect(item,$event) {
            tube = item;
            console.log(JSON.stringify(item));
            vm.entity.sampleTypeId = item.sampleTypeId;
            vm.entity.sampleClassificationId = item.sampleClassificationId;
            vm.entity.projectId = item.projectId;
            vm.entity.backColor = item.backColor;
            vm.entity.backColorForClass = item.backColorForClass;
            vm.entity.status = item.status;
            $($event.target).closest('table').find('.rowLight').removeClass("rowLight");
            $($event.target).closest('tr').addClass("rowLight");
            vm.queryProjectSampleClass(vm.entity.projectId,vm.entity.sampleTypeId);
        }

        function onError() {

        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            console.log(JSON.stringify(vm.entity));
            // if(oldTube.sampleTypeName != "98"){
            //     if(oldTube.sampleTypeId != tube.sampleTypeId){
            //         toastr.error("不同样本类型不能被选择！");
            //         return;
            //     }else{
            //         if(oldTube.sampleClassificationId){
            //             if(oldTube.sampleClassificationId != tube.sampleClassificationId){
            //                 toastr.error("不同样本分类不能被选择！");
            //                 return;
            //             }
            //         }
            //
            //     }
            //
            // }
            $uibModalInstance.close(vm.entity);
        };
    }
})();
