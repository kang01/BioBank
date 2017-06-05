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
            //需求状态
            var _requirementStatus = [
                {id:"1101",name:"进行中"},
                {id:"1102",name:"待批准"},
                {id:"1103",name:"已批准"},
                {id:"1104",name:"已作废"}
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
                {id:"1703",name:"已交接"},
            ];

            var allStatus = null
            function _getStatus(statusCode){
                if (!allStatus){
                    allStatus = _.union(
                        _requirementStatus,
                        _taskStatus,
                        _takeOverStatus,
                        _stockOutTaskBoxStatus
                    );
                }

                return (_.find(allStatus, {id:statusCode})||{}).name;
            }

            var masterData = {
                sexDict :_sexDict,
                diseaseType :_diseaseType,
                requirementStatus :_requirementStatus,
                taskStatus :_taskStatus,
                takeOverStatus :_takeOverStatus,
                stockOutTaskBoxStatus: _stockOutTaskBoxStatus,
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
