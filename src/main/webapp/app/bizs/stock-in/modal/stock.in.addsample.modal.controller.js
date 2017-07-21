/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAddSampleModal', StockInAddSampleModal);

    StockInAddSampleModal.$inject = ['$uibModalInstance','items','toastr','SampleTypeService','ProjectService','ProjectSitesByProjectIdService','MasterData','StockInInputService'];

    function StockInAddSampleModal($uibModalInstance,items,toastr,SampleTypeService,ProjectService,ProjectSitesByProjectIdService,MasterData,StockInInputService) {
        var vm = this;
        vm.status = items.status;
        vm.tubes = items.tubes;
        vm.entity = {
            sampleClassificationId:"",
            sampleClassificationName:"",
            sampleClassificationCode:""
        };
        vm.entity.projectId = items.projectId;
        vm.entity.projectCode = items.projectCode;
        vm.entity.projectSiteId = items.projectSiteId;
        vm.entity.sampleCode = items.sampleCode;
        vm.entity.frozenBoxId = items.frozenBoxId;

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
        vm.sampleTypeCode = items.sampleTypeCode;

        if(vm.sampleTypeCode != "98" || vm.sampleTypeCode != "97"){
            vm.entity.sampleTypeId = items.sampleTypeId;
            vm.entity.sampleTypeCode = items.sampleTypeCode;
            vm.entity.sampleTypeName = items.sampleTypeName;
            vm.sampleTypeId = items.sampleTypeId;
            vm.sampleTypeCode = items.sampleTypeCode;
            vm.sampleTypeName = items.sampleTypeName;
            vm.entity.sampleClassificationId = items.sampleClassificationId;
            vm.entity.sampleClassificationCode = items.sampleClassificationCode;
            vm.entity.sampleClassificationName = items.sampleClassificationName;
            vm.sampleClassificationId = items.sampleClassificationId;
            vm.sampleClassificationCode = items.sampleClassificationCode;

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
                // if(!vm.entity.projectSiteId){
                //     if(data.length){
                //         vm.entity.projectSiteId = data[0].id;
                //     }
                // }
            }


            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','desc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"97"});
                if(vm.sampleTypeCode == "98" || vm.sampleTypeCode == "97"){
                    vm.entity.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.entity.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
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
                    vm.entity.sampleTypeCode = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeCode;
                    vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+value}).backColor;
                    $('table').find('.rowLight').removeClass("rowLight");
                    tube = "";


                    }
            };
            vm.queryProjectSampleClass = _fnQueryProjectSampleClass;
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    if(vm.sampleTypeCode == "98" || vm.sampleTypeCode == "97"){
                        if(vm.projectSampleTypeOptions.length){
                            // if(!vm.entity.sampleClassificationId){
                                vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                                vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                            // }
                            }
                    }else{
                        if(!vm.entity.sampleClassificationId && !vm.entity.backColorForClass){
                            if(vm.projectSampleTypeOptions.length){
                                vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                                vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                            }
                        }
                    }
                    StockInInputService.queryTube(vm.entity.sampleCode,vm.entity.projectCode,vm.entity.frozenBoxId,vm.entity.sampleTypeId,vm.entity.sampleClassificationId).success(function (data) {
                        vm.tubes = data;
                    });

                });
            }
            vm.projectSampleTypeConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    vm.entity.sampleClassificationId = value;
                    vm.entity.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                    vm.entity.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                    vm.entity.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).backColor;
                    StockInInputService.queryTubeBySampleClassificationId(vm.entity.sampleCode,vm.entity.projectCode,vm.entity.sampleTypeId,vm.entity.sampleClassificationId).success(function (data) {
                        vm.tubes = data;
                    });
                }
            };
        }
        _init();


        function _fnSampleBoxSelect(item,$event) {
            tube = item;
            vm.entity.id = item.id;
            vm.entity.projectSiteId = item.projectSiteId;
            vm.entity.sampleTypeId = item.sampleTypeId;
            vm.entity.sampleTypeCode = item.sampleTypeCode;
            vm.entity.sampleClassificationId = item.sampleClassificationId;
            vm.entity.sampleClassificationCode = item.sampleClassificationCode;
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
            if(vm.sampleTypeCode != "98" || vm.sampleTypeCode != "97"){
                if(tube){
                    if(vm.sampleTypeId != tube.sampleTypeId){
                        toastr.error("不同样本类型不能被选择！");
                        return;
                    }else{
                        if(vm.entity.sampleClassificationId){
                            if(vm.sampleClassificationId != tube.sampleClassificationId){
                                toastr.error("不同样本分类不能被选择！");
                                return;
                            }
                        }

                    }
                }


            }
            $uibModalInstance.close(vm.entity);
        };
    }
})();
