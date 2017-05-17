/**
 * Created by gaokangkang on 2017/5/16.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInService', StockInService);

    StockInService.$inject = ['$http'];
    function StockInService($http) {
        var service = {
            //获取委托方
            queryDelegates:_queryDelegates,
            //获取申请列表
            queryRequirementList:_queryRequirementList,
            //附加列表
            copyRequirementList:_copyRequirementList
        };
        function _queryDelegates() {
            return $http.get('/api/delegates/all')
        }
        function _queryRequirementList(data,oSettings) {
            return $http.post('api/temp/res/stock-out-applies',JSON.stringify(data))
        }
        function _copyRequirementList(id) {
            return $http.get('api/temp/stock-out-applies/parentApply/'+id)
        }
        return service;
    }
})();
