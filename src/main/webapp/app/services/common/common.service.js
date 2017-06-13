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
        .factory('BioBankDataTable',['DTOptionsBuilder','DTColumnBuilder','$timeout',function (DTOptionsBuilder, DTColumnBuilder,$timeout) {
            var service = {};
            service.buildDTOption = function (type, scrollY, pageLength, dom) {
                var options = DTOptionsBuilder.newOptions()
                    .withOption('processing',true);

                var types = type.split(',');
                _.each(types, function(t){
                    switch (t){
                        case "NORMALLY":
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

                if (dom){
                    options.withDOM(dom);
                }

                return options;
            };

            return service;
        }])
        .factory('MasterData',function () {
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
            //申请状态
            var _requirementStatus = [
                {id:"1101",name:"进行中"},
                {id:"1102",name:"待批准"},
                {id:"1103",name:"已批准"},
                {id:"1105",name:"已作废"}
            ];
            //任务状态
            var _taskStatus = [
                {id:"1601",name:"待出库"},
                {id:"1602",name:"进行中"},
                {id:"1603",name:"已出库"},
                {id:"1604",name:"异常出库"},
                {id:"1605",name:"已作废"}
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
                //2001：新建，2002：待入库，2003：已分装，2004：已入库，2005：已作废，2006：已上架,2008:待出库
                {id:"2001",name:"新建"},
                {id:"2002",name:"待入库"},
                {id:"2003",name:"已分装"},
                {id:"2004",name:"已入库"},
                {id:"2005",name:"已作废"},
                {id:"2006",name:"已上架"},
                // {id:"2007",name:"已上架"},
                {id:"2008",name:"待出库"},
            ];
            // 冻存管状态
            var _frozenTubeStatus = [
                //冻存管状态：3001：正常，3002：空管，3003：空孔；3004：异常
                {id:"3001",name:"正常"},
                {id:"3002",name:"空管"},
                {id:"3003",name:"空孔"},
                {id:"3004",name:"异常"}
            ];

            var allStatus = null;
            function _getStatus(statusCode){
                if (!allStatus){
                    allStatus = _.union(
                        _requirementStatus,
                        _taskStatus,
                        _takeOverStatus,
                        _stockOutTaskBoxStatus,
                        _frozenBoxStatus,
                        _frozenTubeStatus
                    );
                }

                return (_.find(allStatus, {id:statusCode+""})||{}).name;
            }

            var masterData = {
                sexDict :_sexDict,
                diseaseType :_diseaseType,
                requirementStatus :_requirementStatus,
                taskStatus :_taskStatus,
                takeOverStatus :_takeOverStatus,
                stockOutTaskBoxStatus: _stockOutTaskBoxStatus,
                frozenBoxStatus: _frozenBoxStatus,
                frozenTubeStatus: _frozenTubeStatus,
                getStatus: _getStatus,
            };
            return masterData;
        })
        .factory('SampleService', function () {
            var factory = {};
            //改变管子样本类型 1:分类
            factory.changeSampleType = function (sampleId,td,sampleTypeOptions,status) {
                if(status == 1){
                    for(var i = 0; i < sampleTypeOptions.length; i++){
                        if(sampleId == sampleTypeOptions[i].sampleClassificationId){
                            td.style.backgroundColor = sampleTypeOptions[i].backColor;
                        }
                    }
                }else{
                    for(var i = 0; i < sampleTypeOptions.length; i++){
                        if(sampleId == sampleTypeOptions[i].id){
                            td.style.backgroundColor = sampleTypeOptions[i].backColor;
                        }
                    }                }

            };
            return factory;
        });
})();
