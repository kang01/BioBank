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
            queryTaskDesc:_queryTaskDesc
        };
        function _queryTaskList(data,oSettings) {
            return $http.post('api/res/stock-out-tasks',JSON.stringify(data))
        }
        function _queryTaskDesc(taskId) {
            return $http.get('api/stock-out-tasks/'+taskId)
        }
        return service;
    }
})();
