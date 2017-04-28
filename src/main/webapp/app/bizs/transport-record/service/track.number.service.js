/**
 * Created by gaokangkang on 2017/4/28.
 * 运单号判重
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('TrackNumberService', TrackNumberService);

    TrackNumberService.$inject = ['$q','$http'];

    function TrackNumberService($q,$http) {
        return {
             getTrackNum:function(trackNumber){
                var defer = $q.defer();
                $http({
                    method:'get',
                    url:'api/tranships/isRepeat/'+trackNumber
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
