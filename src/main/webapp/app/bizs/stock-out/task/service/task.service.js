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
            queryTaskList:_queryTaskList
        };
        function _queryTaskList(data,oSettings) {
            return $http.post('api/res/tranships',JSON.stringify(data))
        }
        return service;
    }
})();
