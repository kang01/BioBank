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
            queryPlanList:_queryPlanList
        };
        function _queryPlanList(data,oSettings) {
            return $http.post('api/res/tranships',JSON.stringify(data))
        }
        return service;
    }
})();
