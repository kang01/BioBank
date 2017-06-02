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
            //获取管子
            queryTubes:_queryTubes,
            //获取临时盒子
            queryTempBoxes:_queryTempBoxes,
            //保存装盒后的盒子
            saveTempBoxes:_saveTempBoxes,
            //异常
            abnormal:_fnAbnormal,
            //撤销申请
            repeal:_fnRepeal,
            //批注
            fnNote:_fnNote,
            //已出库批注
            outputNote:_fnOutputNote,
            //出库
            saveOutput:_fnSaveOutput,
            //出库列表
            queryOutputList:_queryOutputList,
            //出库详情页
            queryOutputDes:_queryOutputDes
        };
        function _queryTaskList(data,oSettings) {
            return $http.post('api/res/stock-out-tasks',JSON.stringify(data))
        }
        function _queryTaskDesc(taskId) {
            return $http.get('api/stock-out-tasks/'+taskId)
        }
        function _queryTaskBox(taskId) {
            return $http.get('api/stock-out-frozen-boxes/task/'+taskId)
        }
        function _saveTask(param) {
            return $http.put('api/stock-out-tasks/',param)
        }
        function _queryTubes(frozenBoxCode,taskId) {
            return $http.get('api/frozen-tubes/frozenBox/'+frozenBoxCode+'/task/'+taskId)
        }
        function _queryTempBoxes(taskId) {
            return $http.get('api/stock-out-frozen-boxes/temp-box/task/'+taskId)
        }
        function _saveTempBoxes(taskId,param) {
            return $http.post('api/stock-out-frozen-boxes/task/'+taskId,param)
        }
        function _fnAbnormal(param) {
            return $http.put('api/stock-out-task-frozen-tubes/abnormal',param)
        }
        function _fnRepeal(param) {
            return $http.put('api/stock-out-task-frozen-tubes/repeal',param)
        }
        function _fnNote(param) {
            return $http.put('api/stock-out-task-frozen-tubes/note',param)
        }
        function _fnOutputNote(param) {
            return $http.put('api/stock-out-frozen-boxes/note',param)
        }
        function _fnSaveOutput(taskId,frozenBoxIds,param) {
            return $http.put('api/stock-out-frozen-boxes/task/'+taskId+'/frozen-boxes/'+frozenBoxIds,param)
        }
        function _queryOutputList(taskId) {
            return $http.get('/api/stock-out-frozen-boxes/task-box/'+taskId)
        }
        function _queryOutputDes(frozenBoxIds,data) {
            return $http.post('/api/res/stock-out-box-tubes/stockOutBox/'+frozenBoxIds,JSON.stringify(data))
        }
        return service;
    }
})();
