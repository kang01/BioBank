/**
 * Created by gaokangkang on 2017/5/12.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('RequirementService', RequirementService);

    RequirementService.$inject = ['$http'];
    function RequirementService($http) {
        var service = {
            queryDemo:_queryDemo,

            //获取申请列表
            queryRequirementList:_queryRequirementList,
            //附加列表
            copyRequirementList:_copyRequirementList,
            //获取委托方
            queryDelegates:_queryDelegates,
            //冻存管类型
            queryFrozenTubeType:_queryFrozenTubeType,
            //根据项目和样本类型获取样本分类
            queryRequirementSampleClasses:_queryRequirementSampleClasses,
            //保存申请空对象
            saveRequirement:_saveRequirement
        };
        function _queryDemo(data,oSettings) {
            return $http.post('api/res/tranships',JSON.stringify(data))
        }
        function _queryRequirementList(data,oSettings) {
            return $http.post('api/temp/res/stock-out-applies',JSON.stringify(data))
        }
        function _copyRequirementList(id) {
            return $http.get('api/temp/stock-out-applies/parentApply/'+id)
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
        function _saveRequirement(requirement) {
            return $http.post('/api/stock-out-applies/new-empty',requirement)
        }

        return service;
    }
})();
