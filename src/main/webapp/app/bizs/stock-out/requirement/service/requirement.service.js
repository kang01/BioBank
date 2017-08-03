/**
 * Created by gaokangkang on 2017/5/12.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('RequirementService', RequirementService);

    RequirementService.$inject = ['$http','$q'];
    function RequirementService($http,$q) {
        var service = {
            queryDemo:_queryDemo,
            //获取申请列表
            queryRequirementList:_queryRequirementList,
            //附加列表
            queryCopyRequirementList:_queryCopyRequirementList,
            //根据ID获取详情
            queryRequirementDesc:_queryRequirementDesc,
            //获取委托方
            queryDelegates:_queryDelegates,
            //冻存管类型
            queryFrozenTubeType:_queryFrozenTubeType,
            //根据项目和样本类型获取样本分类
            queryRequirementSampleClasses:_queryRequirementSampleClasses,
            //保存申请空对象
            saveRequirementEmpty:_saveRequirementEmpty,
            //保存申请对象
            saveRequirementInfo:_saveRequirementInfo,
            //保存样本需求(带附件)
            saveSampleRequirementOfUpload:_saveSampleRequirementOfUpload,
            //保存样本需求
            saveSampleRequirement:_saveSampleRequirement,
            //修改保存样本需求
            saveEditSampleRequirement:_saveEditSampleRequirement,
            //根据样本库存查询
            querySampleRequirement:_querySampleRequirement,
            //核对
            checkSampleRequirement:_checkSampleRequirement,
            //批量核对
            checkSampleRequirementList:_checkSampleRequirementList,
            //删除
            delSampleRequirement:_delSampleRequirement,
            //核对详情
            descSampleRequirement:_descSampleRequirement,
            //指定样本详情
            appointDescSampleRequirement:_appointDescSampleRequirement,
            //详情列表
            descSampleList:_descSampleList,
            //复原
            revertSampleRequirement:_revertSampleRequirement,
            //批准
            approveSampleRequirement:_approveSampleRequirement,
            //附加
            addApplyRequirement:_addApplyRequirement,
            //项目更改样本列表复原
            sampleRevert:_sampleRevert,
            //保存计划
            savePlan:_savePlan,
            //作废
            invalidPlan:_invalidPlan

        };
        function _queryDemo(data,oSettings) {
            return $http.post('api/res/tranships',JSON.stringify(data));
        }
        function _queryRequirementList(data,oSettings) {
            return $http.post('api/res/stock-out-applies',JSON.stringify(data));
        }
        function _queryCopyRequirementList(id) {
            return $http.get('api/stock-out-applies/parentApply/'+id);
        }
        function _queryRequirementDesc(id) {
            return $http.get('api/stock-out-applies/'+id).then(function(res){
                return res.data;
            });
        }
        function _queryDelegates() {
            return $http.get('api/delegates/all');
        }
        function _queryFrozenTubeType() {
            return $http.get('api/frozen-tube-types/all');
        }
        function _queryRequirementSampleClasses(projectIds,sampleTypeId) {
            return $http.get('api/project-sample-classes/projectIds/'+projectIds+'/sampleTypeId/'+sampleTypeId);
        }
        function _saveRequirementEmpty() {
            return $http.post('api/stock-out-applies/new-empty');
        }
        function _saveRequirementInfo(param) {
            return $http.put('api/stock-out-applies/update-object',param);
        }
        function _saveSampleRequirementOfUpload(applyId,file) {
            var req = {
                method: 'POST',
                url: 'api/stock-out-requirements/stockOutApply/'+applyId+'/upload',
                headers: {
                    'Content-Type': undefined
                },
                data: file
            };
            return $http(req);
        }
        function _saveSampleRequirement(applyId,param) {
            return $http.post('api/stock-out-requirements/stockOutApply/'+applyId,param);
        }
        function _saveEditSampleRequirement(applyId,param) {
            return $http.put('api/stock-out-requirements/stockOutApply/'+applyId,param);
        }
        function _querySampleRequirement(id) {
            return $http.get('api/stock-out-requirements/'+id);
        }
        function _checkSampleRequirement(id) {
            return $http.post('api/stock-out-requirements/'+id+'/check');
        }
        function _checkSampleRequirementList(ids) {
            return $http.post('api/stock-out-requirements/check/'+ids);
        }
        function _delSampleRequirement(id) {
            return $http.delete('api/stock-out-requirements/'+id);
        }
        function _descSampleRequirement(id) {
            return $http.get('api/stock-out-requirements/getCheckDetail/'+id);
        }
        function _descSampleList(id,data) {
            return $http.post('api/res/stock-out-requirements/getCheckDetail/'+id,angular.toJson(data));
        }
        //指定样本详情
        function _appointDescSampleRequirement(id,data) {
            return $http.post('api/res/stock-out-required-samples/stockOutRequirement/'+id,angular.toJson(data));
        }
        function _revertSampleRequirement(id) {
            return $http.put('api/stock-out-requirements/revert/'+id);
        }
        function _approveSampleRequirement(applyId,param) {
            return $http.put('api/stock-out-applies/approve/'+applyId,param);
        }
        function _addApplyRequirement(applyId) {
            return $http.post('api/stock-out-applies/additionalApply/'+applyId);
        }
        function _sampleRevert(applyId) {
            return $http.put('/api/stock-out-applies/revert/'+applyId);
        }
        function _savePlan(applyId) {
            return $http.post('/api/stock-out-plans/'+applyId);
        }
        function _invalidPlan(applyId,param) {
            return $http.put('/api/stock-out-applies/'+applyId+'/invalid',param);
        }

        return service;
    }
})();
