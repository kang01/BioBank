/**
 * Created by gaokangkang on 2017/4/16.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('BoxCodeIsRepeatService',BoxCodeIsRepeatService);
    BoxCodeIsRepeatService.$inject = ['$q','$http'];

    function BoxCodeIsRepeatService($q,$http) {
        return {
            getByCode:function(code){
                var defer = $q.defer();
                $http({
                    method:'get',
                    url:'api/frozen-boxes/isRepeat/'+code
                }).success(function (data,status,headers,config) {
                    defer.resolve(data);
                }).error(function (data,status,headers,config) {
                    defer.reject(data);
                });
                return defer.promise
            }
        }
    }
})();
