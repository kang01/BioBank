/**
 * Created by gaoyankang on 2017/4/4.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('BioBankBlockUi',['blockUI','$timeout',function (blockUI,$timeout) {
            var blockUiMessage = "处理中……";
            var factory = {};
            factory.blockUiStart = function () {
                blockUI.start(blockUiMessage);
            };
            factory.blockUiStop = function () {
                $timeout(function() {
                    blockUI.stop();
                }, 1000);
            };
            return factory;
        }])
        .factory('BioBankDataTable',['DTOptionsBuilder','DTColumnBuilder','$timeout','$compile',function (DTOptionsBuilder, DTColumnBuilder,$timeout,$compile) {
            var service = {};
            service.buildDTOption = function (type, scrollY, pageLength, dom, scope,orderIndex) {
                var options = DTOptionsBuilder.newOptions()
                    .withOption('processing',true);

                var types = type.split(',');
                _.each(types, function(t){
                    switch (t){
                        case "ORDINARY":
                            options.withPaginationType('full_numbers');
                            break;
                        case "NORMALLY":
                            if (window.innerHeight >= 850){
                                pageLength += pageLength;
                            }
                            options.withPaginationType('full_numbers');
                            break;
                        case "NO-PAGING":
                            options.withOption('paging', false);
                            break;
                        case "SEARCHING":
                            options.withOption('searching', true);
                            break;
                        case "BASIC":
                            options.withOption('info', false)
                                .withOption('paging', false)
                                .withOption('sorting', false)
                                .withOption('searching', false);
                            break;
                        case "SORTING":
                            options.withOption('sorting', [])
                                .withOption('paging', false)
                                .withOption('info', false)
                                .withOption('searching', false)
                                .withPaginationType('full_numbers');
                            break;
                    }
                });


                if (scrollY){
                    options.withScroller()
                        .withOption('scrollY', +scrollY);
                }
                if (pageLength){
                    options.withOption('lengthChange', false)
                        .withOption('pageLength', +pageLength);
                }

                if (dom != null){
                    options.withDOM(dom);
                }else{
                    dom = "<'row mt-0'<'col-xs-6 text-left pl-25' f> <'col-xs-6 text-right mb-5' TB> r> t <'row mt-0'<'col-xs-4'i> <'col-xs-8'p>>";
                    options.withButtons([]).withDOM(dom);
                }

                if (scope){
                    // 执行Header内容的Compile
                    options.isHeaderCompiled = false;
                    options.withOption('headerCallback', function(header) {
                        if (!options.isHeaderCompiled) {
                            // Use this headerCompiled field to only compile header once
                            // options.isHeaderCompiled = true;
                            var theader = angular.element(header);
                            // 检查DOM是否已经$compile过了
                            if (theader.find(".ng-scope").length == 0){
                                $compile(theader.contents())(scope);
                            }
                        }
                    });
                }
                if (orderIndex){
                    options.withOption('order', [[orderIndex, 'asc' ]])
                }


                return options;
            };
            service.buildDTColumn = function (columnDatas) {
                var columns = [];
                var obj;
                _.forEach(columnDatas,function (column) {

                    obj = DTColumnBuilder.newColumn(column.name);

                    if(column.title){
                        obj.withTitle(column.title);
                    }
                    if(column.width){
                        obj.withOption("width", column.width);
                    }
                    if(column.notSortable){
                        obj.notSortable();
                    }
                    if(column.renderWith){
                        obj.renderWith(column.renderWith)
                    }

                    columns.push(obj);
                });

                return columns;
            };
            return service;
        }])
        .factory('MasterData',['SampleTypeService',function(SampleTypeService){
            var _sexDict= [{type:'M',name:'男'},{type:'F',name:'女'},{type:'null',name:'不详'}];
            //疾病类型
            var _diseaseType = [
                {
                    id:'1',
                    name:"AMI"
                },{
                    id:'2',
                    name:"PCI"
                },{
                    id:'null',
                    name:"不祥"
                }
            ];
            //转运状态
            var _transportStatus = [
                {id:"1001",name:"进行中"},
                {id:"1002",name:"待入库"},
                {id:"1003",name:"已入库"},
                {id:"1005",name:"接收完成"},
                {id:"1090",name:"已作废"}
            ];
            //申请状态
            var _requirementStatus = [
                {id:"1101",name:"进行中"},
                {id:"1102",name:"待批准"},
                {id:"1103",name:"已批准"},
                {id:"1190",name:"已作废"}
            ];
            //计划状态
            var _planStatus = [
                {id:"1401",name:"进行中"},
                {id:"1402",name:"已完成"},
                {id:"1403",name:"已撤销"},
                {id:"1490",name:"已作废"}
            ];
            //任务状态
            var _taskStatus = [
                {id:"1601",name:"待出库"},
                {id:"1602",name:"进行中"},
                {id:"1603",name:"已出库"},
                {id:"1604",name:"异常出库"},
                {id:"1605",name:"已撤销"},
                {id:"1690",name:"已作废"}
            ];
            //交接状态
            var _takeOverStatus = [
                {id:"2101",name:"进行中"},
                {id:"2102",name:"已交接"}
            ];
            // 出库冻存盒状态
            var _stockOutTaskBoxStatus = [
                {id:"1701",name:"待出库"},
                {id:"1702",name:"已出库"},
                {id:"1703",name:"已交接"}
            ];
            // 冻存盒状态
            var _frozenBoxStatus = [
                //2001：新建，2002：待入库，2003：已分装，2004：已入库，2090：已作废，2006：已上架,2008:待出库
                {value:"2001",label:"新建"},
                {value:"2002",label:"待入库"},
                {value:"2003",label:"已分装"},
                {value:"2004",label:"已入库"},
                {value:"2006",label:"已上架"},
                {value:"2008",label:"待出库"},
                {value:"2009",label:"已出库"},
                {value:"2010",label:"已交接"},
                {value:"2011",label:"接收完成"},
                {value:"2012",label:"已销毁"},
                {value:"2013",label:"归还中"},
                {value:"2090",label:"已作废"},
                {value:"2101",label:"交接进行中"},
                {value:"2102",label:"交接完成"},
                {value:"2190",label:"交接已作废"}
            ];
            // 冻存管状态
            var _frozenTubeStatus = [
                //冻存管状态：3001：正常，3002：空管，3003：空孔；3004：异常
                {id:"3001",name:"正常"},
                {id:"3002",name:"空管"},
                {id:"3003",name:"空孔"},
                {id:"3004",name:"异常"},
                {id:"3005",name:"销毁"}
            ];
            var allStatus = null;
            function _getStatus(statusCode){
                if (!allStatus){
                    allStatus = _.union(
                        _transportStatus,
                        _requirementStatus,
                        _taskStatus,
                        _takeOverStatus,
                        _stockOutTaskBoxStatus,
                        _frozenBoxStatus,
                        _frozenTubeStatus,
                        _planStatus
                    );
                }

                return (_.find(allStatus, {id:statusCode+""})||{}).name;
            }
            function _getFrozenBoxStatus(statusCode) {
                return (_.find(_frozenBoxStatus,{value:statusCode+""})|| {}).label;
            }
            function _getSex(sex) {
                return (_.find(_sexDict,{type:sex+""})|| {}).name;
            }

            var masterData = {
                sexDict :_sexDict,
                diseaseType :_diseaseType,
                transportStatus :_transportStatus,
                requirementStatus :_requirementStatus,
                taskStatus :_taskStatus,
                takeOverStatus :_takeOverStatus,
                stockOutTaskBoxStatus: _stockOutTaskBoxStatus,
                frozenBoxStatus: _frozenBoxStatus,
                frozenTubeStatus: _frozenTubeStatus,
                getStatus: _getStatus,
                getFrozenBoxStatus: _getFrozenBoxStatus,
                getSex: _getSex
            };
            return masterData;
        }])
        .factory('SampleService', function () {
            var factory = {};
            //改变管子样本类型 1:分类 2:类型
            factory.changeSampleType = function (sampleTypeId,td,sampleTypeOptions,status) {
                if(status == 1){
                    for(var i = 0; i < sampleTypeOptions.length; i++){
                        if(sampleTypeId == sampleTypeOptions[i].sampleClassificationId){
                            td.style.backgroundColor = sampleTypeOptions[i].backColor;
                        }
                    }
                }else{
                    for(var i = 0; i < sampleTypeOptions.length; i++){
                        if(sampleTypeId == sampleTypeOptions[i].id){
                            td.style.backgroundColor = sampleTypeOptions[i].backColor;
                        }
                    }
                }

            };
            return factory;
        })
        .factory('BioBankSelectize',function () {
            var service = {};
            service.buildSettings = function (data){
                // true对于使用create方式添加的option会添加到select标签下面，再次点击会在下拉中出现。
                // False，create方式添加的option只是显示在选框中，再次点击不会出现在下拉框中。
                var options = {
                    create : false,
                    maxItems:1,
                    delimiterEx:"[ ,]"
                };
                options = _.assignIn(options, data);

                if(data.clearMaxItemFlag){
                    delete options.maxItems
                }
                options.onInitialize = function (selectizeInstance) {
                    options.selectizeInstance = selectizeInstance;
                    var delimiterEscaped = selectizeInstance.settings.delimiterEx; //selectizeInstance.settings.delimiter.replace(/[-\/\\^$*+?.()|[\]{}]/g, '\\$&');
                    selectizeInstance.settings.splitOn = new RegExp('\\s*' + delimiterEscaped + '+\\s*');
                };
                return options;
            };
            return service
        })
        .factory('MasterMethod',['SampleTypeService','ProjectService','toastr','RequirementService',
            function (SampleTypeService,ProjectService,toastr,RequirementService) {
            //RNA：大橘盒 DNA：96孔板
            function _getBoxTypeCode(sampleTypeCode){
                var boxTypeCode = null;
                switch (sampleTypeCode){
                    case "RNA":
                        boxTypeCode = "DJH";
                        break;
                    case "DNA":
                        boxTypeCode = "96KB";
                        break;
                    default:
                        boxTypeCode = "DCH";
                        break;
                }

                return boxTypeCode;
            }
            function _changeBoxType(boxTypeOptions,sampleTypeCode) {
               var boxType = _.filter(boxTypeOptions, {frozenBoxTypeCode:_getBoxTypeCode(sampleTypeCode)})[0];
                return boxType;
            }
            //样本类型
            function _querySampleType() {
                return SampleTypeService.querySampleType().success(function (data) {
                    return data
                });
            }
            //样本分类
            function _querySampleClass(projectId,sampleTypeId) {
                if(!sampleTypeId || !projectId){
                    return
                }
                return SampleTypeService.queryProjectSampleClasses(projectId,sampleTypeId).success(function (data) {
                    data.push({sampleClassificationId:null,sampleClassificationName:null});
                    return data;
                })
            }
            //获取所有的项目
            function _queryProject() {
                return ProjectService.query({},function (data) {
                    return data
                }, onError).$promise;
            }
            //获取检测类型
            function _queryCheckType() {
                return RequirementService.queryCheckTypes().success(function (data) {
                    return data;
                });
            }
            //获取委托方
            function _queryDelegates() {
                return RequirementService.queryDelegates().success(function (data) {
                    return data;
                });
            }
            function onError(res) {
                toastr.error(res.data.message);
            }
            var service = {
                changeBoxType :_changeBoxType,
                querySampleType:_querySampleType,
                querySampleClass:_querySampleClass,
                queryProject:_queryProject,
                queryCheckType:_queryCheckType,
                queryDelegates:_queryDelegates
            };
            return service
        }]);
})();
