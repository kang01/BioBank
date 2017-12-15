/**
 * Created by gaokangkang on 2017/12/5.
 * 归还
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('GiveBackService', GiveBackService);

    GiveBackService.$inject = ['$http'];
    function GiveBackService($http) {
        var service = {
            //获取归还的盒子列表
            queryGiveBackTable:_queryGiveBackTable,
            //获取出库申请单信息
            queryApplyInfo:_queryApplyInfo,
            //保存一空归还信息
            saveGiveBackEmpty:_saveGiveBackEmpty,
            //获取归还信息
            queryGiveBackInfo:_queryGiveBackInfo,
            //修改保存归还记录
            saveGiveBackRecord:_saveGiveBackRecord,
            //获取归还冻存盒列表
            queryGiveBackBox:_queryGiveBackBox,
            //获取批量的冻存盒列表
            queryBatchGiveBackBox:_queryBatchGiveBackBox
        };
        function _queryGiveBackTable(data,oSettings) {
            return $http.post('api/res/return-back',JSON.stringify(data));
        }
        function _queryApplyInfo(applyCode) {
            return $http.get('api/stock-out-applies/applyCode/'+applyCode);
        }
        function _saveGiveBackEmpty(giveBackInfo) {
            return $http.post('api/return-back/new-empty/stockOutApply/'+giveBackInfo.applyId+'/project/'+giveBackInfo.projectId,{});
        }
        function _queryGiveBackInfo(giveBackId) {
            return $http.get('api/return-back/id/'+giveBackId);
        }
        function _saveGiveBackRecord(data) {
            return $http.put('api/return-back/update-object',data);
        }
        function _queryGiveBackBox(giveBackCode) {
            return $http.get('api/tranship-boxes/transhipCode/'+giveBackCode);
        }
        function _queryBatchGiveBackBox(applyCode,frozenBoxCodeStr,_deferred) {
            var opt =null;
            if(_deferred){
                opt = {timeout: _deferred.promise};
            }
            return $http.get('api/return-boxes/stockOutApply/'+applyCode+'/frozenBoxCode/'+frozenBoxCodeStr,opt);
        }
        return service;
    }
})();
