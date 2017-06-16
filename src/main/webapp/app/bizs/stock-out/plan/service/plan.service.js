/**
 * Created by gaokangkang on 2017/5/12.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('PlanService', PlanService);

    PlanService.$inject = ['$http'];
    function PlanService($http) {
        var service = {
            //获取計劃列表
            queryPlanList:_queryPlanList,
            //查申请单号
            queryApplyNumInfo:_queryApplyNumInfo,
            //根据申请单号取计划信息及样本需求
            queryPlanSampleInfo:_queryPlanSampleInfo,
            //根据需求Ids获取冻存盒信息
            queryPlanBoxes:_queryPlanBoxes,
            //根据冻存盒取相应的管子信息
            queryPlanTubes:_queryPlanTubes,
            //保存计划
            savePlan:_savePlan,
            //编辑计划
            editPlan:_editPlan,
            //创建任务
            createTask:_createTask,
            //根据计划Id获取任务列表
            queryTaskList:_queryTaskList,
            //任务详情
            queryTaskBoxesDes:_queryTaskBoxesDes,
            //删除任务
            delTask:_delTask
        };
        function _queryPlanList(data,oSettings) {
            return $http.post('api/res/stock-out-plans',JSON.stringify(data));
        }
        function _queryApplyNumInfo(applyNumber) {

            return $http.get('api/temp/stock-out-plans/applyNumber/'+applyNumber);

        }
        function _queryPlanSampleInfo(planId) {
            // return $http.get('api/stock-out-applies/plan/'+planId);
            return $http.get('api/stock-out-applies/requirements/plan/'+planId);
        }
        function _queryPlanBoxes(sampleIds,data) {
            return $http.post('api/res/stock-out-frozen-boxes/requirement/'+sampleIds,JSON.stringify(data));
        }
        function _queryPlanTubes(requirementIds,frozenBoxId) {
            return $http.get('api/stock-out-frozen-tubes/requirements/'+requirementIds+'/frozenBox/'+frozenBoxId);
        }
        function _savePlan(applyId,param) {
            return $http.post('api/stock-out-plans/'+applyId,param);
        }
        function _editPlan(applyId,param) {
            return $http.put('api/stock-out-plans/'+applyId,param);
        }
        function _createTask(planId,boxIds) {
            return $http.post('api/stock-out-tasks/plan/'+planId+'/frozenBox/'+boxIds);;
        }
        function _queryTaskList(planId,data) {
            return $http.post('api/res/stock-out-tasks/plan/'+planId,JSON.stringify(data));
        }
        function _queryTaskBoxesDes(taskId,data) {
            return $http.post('api/res/stock-out-frozen-boxes/task/'+taskId,JSON.stringify(data));
        }
        function _delTask(taskId) {
            return $http.delete('api/stock-out-tasks/'+taskId);
        }
        return service;
    }
})();
