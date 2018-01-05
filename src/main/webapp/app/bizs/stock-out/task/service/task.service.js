/**
 * Created by gaokangkang on 2017/5/12.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TaskService', TaskService);

    TaskService.$inject = ['$http'];
    function TaskService($http) {
        var service = {
            //获取任務列表
            queryTaskList:_queryTaskList,
            //任务详情
            queryTaskDesc:_queryTaskDesc,
            //获取冻存盒
            queryTaskBox:_queryTaskBox,
            //保存计划
            saveTaskBox:_saveTask,
            //作废任务
            invalidTask:_invalidTask,
            //获取管子
            queryTubes:_queryTubes,
            //获取临时盒子
            queryTempBoxes:_queryTempBoxes,
            //保存装盒后的盒子
            saveTempBoxes:_saveTempBoxes,
            //异常
            abnormal:_fnAbnormal,
            //添加tags
            addTags:_addTags,
            //撤销申请
            repeal:_fnRepeal,
            //盒子撤销
            repealBox:_repealBox,
            //批注
            fnNote:_fnNote,
            //已出库批注
            outputNote:_fnOutputNote,
            //出库
            saveOutput:_fnSaveOutput,
            //出库列表
            queryOutputList:_queryOutputList,
            //出库详情页
            queryOutputDes:_queryOutputDes,
            //样本交接
            takeOver:_takeOver,
            //开始任务计时器
            taskTimer:_fnTaskTimer
        };
        function _queryTaskList(data,oSettings) {
            return $http.post('api/res/stock-out-tasks',angular.toJson(data));
        }
        function _queryTaskDesc(taskId) {
            return $http.get('api/stock-out-tasks/'+taskId);
        }
        function _queryTaskBox(taskId) {
            return $http.get('api/stock-out-frozen-boxes/task/'+taskId);
        }
        function _saveTask(param) {
            return $http.put('api/stock-out-tasks/',param);
        }
        function _invalidTask(taskId,invalidReason) {
            var obj = {
                invalidReason:invalidReason
            };
            return $http.put('api/stock-out-tasks/invalid/'+taskId,obj);
        }
        function _queryTubes(frozenBoxCode,taskId) {
            return $http.get('api/frozen-tubes/frozenBox/'+frozenBoxCode+'/task/'+taskId);
        }
        function _queryTempBoxes(taskId) {
            return $http.get('api/stock-out-frozen-boxes/temp-box/task/'+taskId);
        }
        function _saveTempBoxes(taskId,param) {
            return $http.post('api/stock-out-frozen-boxes/task/'+taskId,param);
        }
        function _fnAbnormal(taskId,param) {
            // return $http.put('api/stock-out-task-frozen-tubes/abnormal',param);
            return $http.put('api/stock-out-task-frozen-tubes/abnormal/task/'+taskId,param);
        }
        function _addTags(taskId,param) {
            return $http.put('api/stock-out-task-frozen-tubes/tag/task/'+taskId,param);
        }
        function _fnRepeal(taskId,param) {
            return $http.put('api/stock-out-task-frozen-tubes/repeal/task/'+taskId,param);
        }
        function _repealBox(taskId,param) {
            return $http.put('api/stock-out-frozen-boxes/repealStockOutBoxs/task/'+taskId,param);
        }
        function _fnNote(taskId,param) {
            return $http.put('api/stock-out-task-frozen-tubes/note/task/'+taskId,param);
        }
        function _fnOutputNote(param) {
            return $http.put('api/stock-out-frozen-boxes/note',param);
        }
        function _fnSaveOutput(taskId,frozenBoxIds,param) {
            return $http.put('api/stock-out-frozen-boxes/task/'+taskId+'/frozen-boxes/'+frozenBoxIds,param);
        }
        function _queryOutputList(taskId) {
            return $http.get('/api/stock-out-frozen-boxes/task-box/'+taskId);
        }
        function _queryOutputDes(frozenBoxIds,data) {
            return $http.post('/api/res/stock-out-box-tubes/stockOutBox/'+frozenBoxIds,angular.toJson(data));
        }
        function _takeOver(taskId) {
            return $http.post('/api/stock-out-handovers/task/'+taskId);
        }
        function _fnTaskTimer(taskId) {
            return $http.put('/api/stock-out-tasks/'+taskId+'/begin');
        }
        return service;
    }
})();
