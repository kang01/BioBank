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
            queryTubes:_queryTubes
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
        function _queryTubes(frozenBoxCode) {
            return $http.get('api/frozen-tubes/frozenBox/'+frozenBoxCode)
        }
        return service;
    }
})();
