/**
 * Created by zhuyu on 2017/3/31.
 * 入库单列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('FrozenPosService', FrozenPosService);

    FrozenPosService.$inject = ['$resource', '$http', '$q'];

    function FrozenPosService ($resource, $http, $q) {
        var service = {
            getIncompleteShelves: _getIncompleteShelves,
            getFrozenBoxesByPosition: _getFrozenBoxesByPosition,
        };

        function _getIncompleteShelves(equipmentCode, areaCode){
            var deferred = $q.defer();
            if (typeof equipmentCode === "undefined"){
                deferred.resolve(null);
                return deferred.promise;
            }

            var promise = $http.get('api/temp/frozen-pos/incomplete-shelves/' + equipmentCode + (areaCode ? '/'+areaCode : ''));

            return promise;
        }

        function _getFrozenBoxesByPosition(equipmentCode, areaCode, shelfCode){
            // /frozen-boxes/pos/{equipmentCode}/{areaCode}/{shelfCode}
            var deferred = $q.defer();
            if (typeof equipmentCode === "undefined"){
                deferred.resolve(null);
                return deferred.promise;
            }

            var promise = $http.get('api/temp/frozen-boxes/pos/' + equipmentCode + (areaCode ? '/' + areaCode + (shelfCode ? '/' + shelfCode : '') : ''));

            return promise;
        }

        return service;
    }
})();
