/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInAddSampleModal', StockInAddSampleModal);

    StockInAddSampleModal.$inject = ['$uibModalInstance','items','toastr','SampleTypeService','ProjectService','ProjectSitesByProjectIdService','MasterData','StockInInputService','DTColumnBuilder','BioBankDataTable'];

    function StockInAddSampleModal($uibModalInstance,items,toastr,SampleTypeService,ProjectService,ProjectSitesByProjectIdService,MasterData,StockInInputService,DTColumnBuilder,BioBankDataTable) {
        var vm = this;
        //库里已有的样本
        vm.tubes = items.tubes;
        //single:单次录入 multiple 批量录入
        vm.singleMultipleFlag = items.singleMultipleFlag;

        //是否出库再回来样本
        vm.originalSampleFlag = false;
        var sampleSelectedArray = items.sampleSelectedArray || [];
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
        //冻存管状态
        vm.tubeStatusOptions = MasterData.frozenTubeStatus;

        vm.entity.status = vm.tubeStatusOptions[0].id;
        //单次录入
        if(vm.singleMultipleFlag == 'single' && sampleSelectedArray.length){
            vm.entity.status = sampleSelectedArray[0].status;
            vm.entity.sampleVolumns = sampleSelectedArray[0].sampleVolumns;
            vm.entity.memo = sampleSelectedArray[0].memo;
            vm.entity.projectSiteId = sampleSelectedArray[0].projectSiteId;
            vm.entity.sampleTypeId = sampleSelectedArray[0].sampleTypeId;
            vm.entity.sampleClassificationId = sampleSelectedArray[0].sampleClassificationId;
        }

        _.forEach(sampleSelectedArray, function(sample) {
            sample.status = vm.entity.status;
        });
        vm.sampleTypeCode = items.sampleTypeCode;

        if(vm.sampleTypeCode != "98" && vm.sampleTypeCode != "97"){
            vm.entity.sampleTypeId = items.sampleTypeId;
            vm.entity.sampleTypeCode = items.sampleTypeCode;
            vm.entity.sampleTypeName = items.sampleTypeName;
            vm.sampleTypeId = items.sampleTypeId;
            vm.sampleTypeCode = items.sampleTypeCode;
            // vm.sampleTypeName = items.sampleTypeName;
            vm.entity.sampleClassificationId = items.sampleClassificationId;
            vm.entity.sampleClassificationCode = items.sampleClassificationCode;
            vm.entity.sampleClassificationName = items.sampleClassificationName;
            vm.sampleClassificationId = items.sampleClassificationId;
            vm.sampleClassificationCode = items.sampleClassificationCode;

        }
        //从查询出来的样本中选出要入库的样本
        vm.sampleBoxSelect = _fnSampleBoxSelect;
        var tube;

        function _init() {
           // var len =  _.filter(sampleSelectedArray,{flag:"2"}).length;
           // if(len){
           //     //是否原盒的样本
           //     vm.originalSampleFlag = true;
           // }
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
                    _.forEach(sampleSelectedArray, function(sample) {
                       sample.projectSiteId = value;
                    });
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
            //樣本状态
            vm.tubeStatusConfig = {
                valueField:'id',
                labelField:'name',
                maxItems: 1,
                onChange:function(value){
                    _.forEach(sampleSelectedArray, function(sample) {
                        sample.status = value;
                    });
                }
            };

            //样本类型
            SampleTypeService.querySampleType().success(function (data) {
                vm.sampleTypeOptions = _.orderBy(data,['sampleTypeName','desc']);
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"97"});
                if((vm.sampleTypeCode == "98" || vm.sampleTypeCode == "97") && !vm.entity.sampleTypeId){
                    vm.entity.sampleTypeId = vm.sampleTypeOptions[0].id;
                    vm.entity.sampleTypeName = vm.sampleTypeOptions[0].sampleTypeName;
                    vm.entity.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
                    vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+vm.entity.sampleTypeId}).backColor;
                }else{
                    vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+vm.entity.sampleTypeId}).backColor;
                }
                _fnQueryProjectSampleClass(vm.entity.projectId,vm.entity.sampleTypeId);
            });
            vm.sampleTypeConfig = {
                valueField:'id',
                labelField:'sampleTypeName',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        _fnQueryProjectSampleClass(vm.entity.projectId,value);
                        vm.entity.sampleTypeId = value;
                        vm.entity.sampleTypeName = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeName;
                        vm.entity.sampleTypeCode = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeCode;
                        vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+value}).backColor;
                        $('table').find('.rowLight').removeClass("rowLight");
                        tube = "";
                        _.forEach(sampleSelectedArray, function(sample) {
                            sample.sampleTypeId = value;
                            sample.sampleTypeName = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeName;
                            sample.sampleTypeCode = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeCode;
                            sample.backColor = _.find(vm.sampleTypeOptions,{id:+value}).backColor;
                        });
                    }

                }
            };
            vm.queryProjectSampleClass = _fnQueryProjectSampleClass;
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    if(vm.sampleTypeCode == "98" || vm.sampleTypeCode == "97"){
                        if(vm.projectSampleTypeOptions.length){
                            vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                            vm.entity.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                            vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                            vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                            _.forEach(sampleSelectedArray, function(sample) {
                                sample.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                sample.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                                sample.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                                sample.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                            });
                        }
                    }else{
                        if(!vm.entity.sampleClassificationId && !vm.entity.backColorForClass){
                            if(vm.projectSampleTypeOptions.length){
                                vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                vm.entity.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                                vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                                vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                                _.forEach(sampleSelectedArray, function(sample) {
                                    sample.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                    sample.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                                    sample.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                                    sample.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                                });
                            }
                        }else{
                            vm.entity.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+vm.entity.sampleClassificationId}).backColor;
                            _.forEach(sampleSelectedArray, function(sample) {
                                // sample.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                                // sample.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                                // sample.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                                sample.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+vm.entity.sampleClassificationId}).backColor;
                            });
                        }
                    }
                    _queryTube();

                });
            }
            vm.projectSampleTypeConfig = {
                valueField:'sampleClassificationId',
                labelField:'sampleClassificationName',
                maxItems: 1,
                onChange:function (value) {
                    if(value){
                        vm.entity.sampleClassificationId = value;
                        vm.entity.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                        vm.entity.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                        vm.entity.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).backColor;
                        _queryTube();
                        _.forEach(sampleSelectedArray, function(sample) {
                            sample.sampleClassificationId = value;
                            sample.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                            sample.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                            sample.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).backColor;
                        });
                    }else{
                        _queryTube();
                    }

                }
            };
        }
        _init();
        //查询库存中同一项目下有的样本，盒子id是为了不验证本盒子中的样本
        vm.errorFlag = false;
        function _queryTube() {
            StockInInputService.queryTube(vm.entity.sampleCode,vm.entity.projectCode,vm.entity.frozenBoxId,vm.entity.sampleTypeId,vm.entity.sampleClassificationId).success(function (data) {
                vm.tubes = data;
            }).error(function (data) {
                toastr.error(data.message);
                vm.errorFlag = true;
            });
        }
        //从查询出来的样本中选出要入库的样本
        // vm.clickFlag = false;
        function _fnSampleBoxSelect(item,$event) {
            // vm.originalSampleFlag = true;
            tube = item;
            vm.entity.frozenTubeId = item.id;
            vm.entity.projectSiteId = item.projectSiteId;
            vm.entity.sampleTypeId = item.sampleTypeId;
            vm.entity.sampleTypeCode = item.sampleTypeCode;
            vm.entity.sampleTypeName = item.sampleTypeName;
            vm.entity.sampleClassificationId = item.sampleClassificationId;
            vm.entity.sampleClassificationCode = item.sampleClassificationCode;
            vm.entity.sampleClassificationName = item.sampleClassificationName;
            vm.entity.projectId = item.projectId;
            vm.entity.backColor = item.backColor;
            vm.entity.backColorForClass = item.backColorForClass;
            vm.entity.status = item.status;
            $($event.target).closest('table').find('.rowLight').removeClass("rowLight");
            $($event.target).closest('tr').addClass("rowLight");
            vm.queryProjectSampleClass(vm.entity.projectId,vm.entity.sampleTypeId);
            // vm.clickFlag = true;
        }

        vm.sampleInstance = {};
        vm.sampleColumns = [
            DTColumnBuilder.newColumn('frozenBoxId').withOption("width", "50").notSortable().withOption('searchable',false).withTitle('序号'),
            DTColumnBuilder.newColumn('sampleCode').withTitle('冻存管编码')
        ];
        vm.sampleOptions = BioBankDataTable.buildDTOption("BASIC", 300, 10)
            .withOption('rowCallback', rowCallback);
        function rowCallback(nRow, oData, iDisplayIndex, iDisplayIndexFull)  {
            $('td:first', nRow).html(iDisplayIndex+1);
            return nRow;
        }
        if(sampleSelectedArray.length){
            vm.sampleOptions.withOption('data',sampleSelectedArray)
        }

        function onError() {

        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            // if(vm.sampleTypeCode != "98" || vm.sampleTypeCode != "97"){
            //     if(tube){
            //         if(vm.sampleTypeId != tube.sampleTypeId){
            //             toastr.error("不同样本类型不能被选择！");
            //             return;
            //         }else{
            //             if(vm.entity.sampleClassificationId){
            //                 if(vm.sampleClassificationId != tube.sampleClassificationId){
            //                     toastr.error("不同样本分类不能被选择！");
            //                     return;
            //                 }
            //             }
            //
            //         }
            //     }
            // }
            _.forEach(sampleSelectedArray, function(sample) {
                sample.sampleVolumns = vm.entity.sampleVolumns;
                sample.memo = vm.entity.memo;
            });
            var array = [];
            if(vm.singleMultipleFlag === 'single'){
                vm.entity.oldSampleCode = items.sampleCode;
                array.push(vm.entity);
            }else{
                array = sampleSelectedArray;
            }
            $uibModalInstance.close(array);
        };
    }
})();
