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
            //获取申请列表
            queryRequirementList:_queryRequirementList
        };
        function _queryRequirementList(data,oSettings) {
            return $http.post('api/res/tranships',JSON.stringify(data))
        }
        return service;
    }
})();
