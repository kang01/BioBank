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
            copyRequirementList:_copyRequirementList,
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
            saveEditSampleRequirement:_saveEditSampleRequirement,
            //根据样本库存查询
            querySampleRequirement:_querySampleRequirement
        };
        function _queryDemo(data,oSettings) {
            return $http.post('api/res/tranships',JSON.stringify(data))
        }
        function _queryRequirementList(data,oSettings) {
            return $http.post('api/res/stock-out-applies',JSON.stringify(data))
        }
        function _copyRequirementList(id) {
            return $http.get('api/stock-out-applies/parentApply/'+id)
        }
        function _queryRequirementDesc(id) {
            return $http.get('api/stock-out-applies/'+id).then(function(res){
                return res.data;
            });
        }
        function _queryDelegates() {
            return $http.get('/api/delegates/all')
        }
        function _queryFrozenTubeType() {
            return $http.get('/api/frozen-tube-types/all')
        }
        function _queryRequirementSampleClasses(projectIds,sampleTypeId) {
            return $http.get('/api/project-sample-classes/projectIds/'+projectIds+'/sampleTypeId/'+sampleTypeId)
        }
        function _saveRequirementEmpty() {
            return $http.post('/api/stock-out-applies/new-empty')
        }
        function _saveRequirementInfo(param) {
            return $http.put('/api/stock-out-applies/update-object',param)
        }
        function _saveSampleRequirementOfUpload(applyId,param,file) {
            return $http.post('/api/stock-out-requirements/stockOutApply/'+applyId+'/upload/stockOutRequirement='+param,file)
        }
        function _saveSampleRequirement(applyId,param) {
            return $http.post('/api/stock-out-requirements/stockOutApply/'+applyId,param)
        }
        function _saveEditSampleRequirement(applyId,param) {
            return $http.put('/api/stock-out-requirements/stockOutApply/'+applyId,param)
        }
        function _querySampleRequirement(id) {
            return $http.get('/api/stock-out-requirements/'+id)
        }

        return service;
    }
})();
