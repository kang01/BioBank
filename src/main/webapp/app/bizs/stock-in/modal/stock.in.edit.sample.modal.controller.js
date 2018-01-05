/**
 * Created by gaokangkang on 2017/6/20.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInEditSampleModal', StockInEditSampleModal);

    StockInEditSampleModal.$inject = ['$uibModalInstance','items','toastr','SampleTypeService','ProjectService','ProjectSitesByProjectIdService','MasterData','StockInInputService','DTColumnBuilder','BioBankDataTable','BioBankSelectize'];

    function StockInEditSampleModal($uibModalInstance,items,toastr,SampleTypeService,ProjectService,ProjectSitesByProjectIdService,MasterData,StockInInputService,DTColumnBuilder,BioBankDataTable,BioBankSelectize) {
        var vm = this;
        //库里已有的样本
        vm.tubes = items.tubes;
        //single:单次录入 multiple 批量录入
        vm.singleMultipleFlag = items.singleMultipleFlag;

        //是否出库再回来样本
        // vm.originalSampleFlag = false;
        var sampleSelectedArray = items.sampleSelectedArray || [];
        vm.entity = {
            sampleClassificationId:"",
            sampleClassificationName:"",
            sampleClassificationCode:""
        };
        vm.entity.projectId = items.projectId;
        vm.entity.projectCode = items.projectCode;
        vm.entity.projectSiteId = items.projectSiteId;
        vm.entity.isMixed = items.isMixed;
        vm.entity.sampleCode = items.sampleCode;

        vm.entity.sampleTypeId = items.sampleTypeId;
        vm.entity.sampleTypeCode = items.sampleTypeCode;
        vm.entity.sampleTypeName = items.sampleTypeName;


        //冻存管状态
        vm.tubeStatusOptions = MasterData.frozenTubeStatus;
        _.remove(vm.tubeStatusOptions,{id:"3005"});
        vm.entity.status = vm.tubeStatusOptions[0].id;
        //单次录入
        if(vm.singleMultipleFlag == 'single' && sampleSelectedArray.length){
            vm.entity.status = sampleSelectedArray[0].status;
            vm.entity.sampleVolumns = sampleSelectedArray[0].sampleVolumns;
            vm.entity.memo = sampleSelectedArray[0].memo;
            vm.entity.projectSiteId = sampleSelectedArray[0].projectSiteId;

            vm.entity.sampleTypeId = sampleSelectedArray[0].sampleTypeId;
            vm.entity.sampleTypeCode = sampleSelectedArray[0].sampleTypeCode;
            vm.entity.sampleTypeName = sampleSelectedArray[0].sampleTypeName;

            vm.entity.tubeRows = sampleSelectedArray[0].tubeRows;
            vm.entity.tubeColumns = sampleSelectedArray[0].tubeColumns;

            vm.entity.sampleClassificationId = sampleSelectedArray[0].sampleClassificationId;
            vm.entity.sampleClassificationCode = sampleSelectedArray[0].sampleClassificationCode;
            vm.entity.sampleClassificationName = sampleSelectedArray[0].sampleClassificationName;

            vm.entity.frozenTubeId = sampleSelectedArray[0].frozenTubeId;

        }

        // _.forEach(sampleSelectedArray, function(sample) {
        //     sample.status = vm.entity.status;
        // });

        //非混合类型
        if(!vm.entity.isMixed){
            vm.entity.sampleTypeId = items.sampleTypeId;
            vm.entity.sampleTypeCode = items.sampleTypeCode;
            vm.entity.sampleTypeName = items.sampleTypeName;

            vm.entity.sampleClassificationId = items.sampleClassificationId;
            vm.entity.sampleClassificationCode = items.sampleClassificationCode;
            vm.entity.sampleClassificationName = items.sampleClassificationName;


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
                if(vm.entity.sampleTypeCode != '99'){
                    _.remove(vm.sampleTypeOptions,{sampleTypeCode:"99"});
                }

                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"98"});
                _.remove(vm.sampleTypeOptions,{sampleTypeCode:"97"});

                // if((vm.entity.sampleTypeCode == "98" || vm.entity.sampleTypeCode == "97")){
                //     vm.entity.sampleTypeId = vm.sampleTypeOptions[0].id;
                //     vm.entity.sampleTypeName = vm.sampleTypeOptions[0].sampleTypeName;
                //     vm.entity.sampleTypeCode = vm.sampleTypeOptions[0].sampleTypeCode;
                //     vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+vm.entity.sampleTypeId}).backColor;
                // }else{
                //     if(!vm.entity.sampleTypeId){
                //         vm.entity.backColor = _.find(vm.sampleTypeOptions,{id:+vm.entity.sampleTypeId}).backColor;
                //     }
                // }
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
                        vm.entity.frontColor = _.find(vm.sampleTypeOptions,{id:+value}).frontColor;
                        $('table').find('.rowLight').removeClass("rowLight");
                        tube = "";
                        _.forEach(sampleSelectedArray, function(sample) {
                            sample.sampleTypeId = value;
                            sample.sampleTypeName = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeName;
                            sample.sampleTypeCode = _.find(vm.sampleTypeOptions,{id:+value}).sampleTypeCode;
                            sample.backColor = _.find(vm.sampleTypeOptions,{id:+value}).backColor;
                            sample.frontColor = _.find(vm.sampleTypeOptions,{id:+value}).frontColor;
                        });
                    }

                }
            };
            vm.queryProjectSampleClass = _fnQueryProjectSampleClass;
            //样本分类
            function _fnQueryProjectSampleClass(projectId,sampleTypeId) {
                SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    vm.projectSampleTypeOptions = data;
                    vm.projectSampleTypeOptions.push({sampleClassificationId:"",sampleClassificationName:""});
                    vm.entity.sampleClassificationId = "";
                    vm.entity.sampleClassificationName = "";
                    vm.entity.sampleClassificationCode = "";
                    vm.entity.backColorForClass = "";
                    vm.entity.frontColor = "";
                    // if(vm.entity.isMixed){
                    //     //混合类型
                    //     if(vm.projectSampleTypeOptions.length && vm.entity.sampleTypeCode != "99"){
                    //         vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    //         vm.entity.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                    //         vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    //         vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                    //         _.forEach(sampleSelectedArray, function(sample) {
                    //             sample.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    //             sample.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                    //             sample.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    //             sample.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                    //         });
                    //     }
                    // }else{
                    //     //非混合类型
                    //     if(!vm.entity.sampleClassificationId && !vm.entity.backColorForClass){
                    //         if(vm.projectSampleTypeOptions.length){
                    //             vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    //             vm.entity.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                    //             vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    //             vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                    //             _.forEach(sampleSelectedArray, function(sample) {
                    //                 sample.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                    //                 sample.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                    //                 sample.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                    //                 sample.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                    //             });
                    //         }
                    //     }else{
                    if(vm.entity.sampleClassificationId){
                        // zhuyu for fixed null project sample
                        var obj = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+vm.entity.sampleClassificationId})||{};
                        vm.entity.backColorForClass = obj.backColor;
                        vm.entity.frontColor = obj.frontColor;
                        //多选
                        _.forEach(sampleSelectedArray, function(sample) {
                            var obj = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+vm.entity.sampleClassificationId})||{};
                            sample.backColorForClass = obj.backColor;
                            sample.frontColor = obj.frontColor;
                        });
                    }else{
                        vm.entity.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                        vm.entity.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                        vm.entity.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                        vm.entity.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                        vm.entity.frontColor = vm.projectSampleTypeOptions[0].frontColor;
                        _.forEach(sampleSelectedArray, function(sample) {
                            sample.sampleClassificationId = vm.projectSampleTypeOptions[0].sampleClassificationId;
                            sample.sampleClassificationName = vm.projectSampleTypeOptions[0].sampleClassificationName;
                            sample.sampleClassificationCode = vm.projectSampleTypeOptions[0].sampleClassificationCode;
                            sample.backColorForClass = vm.projectSampleTypeOptions[0].backColor;
                            sample.frontColor = vm.projectSampleTypeOptions[0].frontColor;
                        });
                    }

                    //     }
                    // }
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
                        vm.entity.frontColor = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).frontColor;
                        _queryTube();
                        _.forEach(sampleSelectedArray, function(sample) {
                            sample.sampleClassificationId = value;
                            sample.sampleClassificationName = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationName;
                            sample.sampleClassificationCode = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).sampleClassificationCode;
                            sample.backColorForClass = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).backColor;
                            sample.frontColor = _.find(vm.projectSampleTypeOptions,{sampleClassificationId:+value}).frontColor;
                        });
                    }else{
                        _queryTube();
                    }

                }
            };

            //标签
            var _tagSelectize = {
                create : true,
                persist:false,
                clearMaxItemFlag : true
            };
            vm.TagConfig = BioBankSelectize.buildSettings(_tagSelectize);
            if(sampleSelectedArray.length == 1){
                vm.TagConfig.onInitialize = function (initialize) {
                    var tagSelectize = initialize;
                    var tagOption = [];
                    var tags = [];
                    var tags1 = [];
                    var tags2 = [];
                    tags1.push(sampleSelectedArray[0].tag1);
                    tags1.push(sampleSelectedArray[0].tag2);
                    tags1.push(sampleSelectedArray[0].tag3);
                    if(sampleSelectedArray[0].tag4){
                        tags2 = _.split(sampleSelectedArray[0].tag4, ',');
                    }
                    tags = _.concat(tags1, tags2);
                    _.forEach(tags,function (tag) {
                        var obj = {};
                        obj.text = tag;
                        obj.value =tag;
                        tagOption.push(obj);
                    });

                    tagSelectize.addOption(tagOption);
                    tagSelectize.setValue(tags);
                }
            }

        }
        _init();
        //查询库存中同一项目下有的样本，盒子id是为了不验证本盒子中的样本
        vm.errorFlag = false;
        function _queryTube() {
            vm.entity.frozenBoxId = items.frozenBoxId;
            StockInInputService.queryTube(vm.entity.sampleCode,vm.entity.projectCode,vm.entity.frozenBoxId,vm.entity.sampleTypeId,vm.entity.sampleClassificationId).success(function (data) {
                vm.tubes = data;
                if(vm.tubes.length == 1){
                    vm.entity = vm.tubes[0]
                }
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
            vm.entity.tubeRows = item.tubeRows;
            vm.entity.tubeColumns = item.tubeColumns;
            $($event.target).closest('table').find('.rowLight').removeClass("rowLight");
            $($event.target).closest('tr').addClass("rowLight");
            vm.queryProjectSampleClass(vm.entity.projectId,vm.entity.sampleTypeId);
            // vm.clickFlag = true;
        }

        vm.sampleInstance = {};
        vm.sampleColumns = [
            DTColumnBuilder.newColumn('sampleCode').withOption("width", "50").notSortable().withOption('searchable',false).withTitle('序号'),
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
        //标签
        function _updateTagsData() {
            var tags1 = [];
            var tags2 = [];

            _.forEach(vm.tags,function (tag,i) {
                if(i < 3){
                    tags1.push(tag);
                }else{
                    tags2.push(tag);
                }
            });
            _.forEach(tags1,function (tag,i) {
                if(tag){
                    var index = i+1;
                    vm.entity["tag"+index] = tag;
                }
            });
            if(tags2.length){
                vm.entity.tag4 = _.join(tags2,",");
            }

        }
        vm.cancel = function () {
            $uibModalInstance.dismiss('cancel');
        };
        vm.ok = function () {
            _updateTagsData();
            _.forEach(sampleSelectedArray, function(sample) {
                sample.sampleVolumns = vm.entity.sampleVolumns;
                sample.memo = vm.entity.memo;
                sample.status = vm.entity.status;
                sample.tag1 = vm.entity.tag1;
                sample.tag2 = vm.entity.tag2;
                sample.tag3 = vm.entity.tag3;
                sample.tag4 = vm.entity.tag4;
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
