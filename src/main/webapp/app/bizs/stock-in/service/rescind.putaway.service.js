/**
 * Created by gaokangkang on 2017/5/8.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('RescindPutAwayService', RescindPutAwayService);

    RescindPutAwayService.$inject = ['$http'];

    function RescindPutAwayService ($http) {
        var service = {
            rescindPutAway: _rescindPutAway
        };
        function _rescindPutAway(stockInCode,boxCode) {
            return $http.put('api/stock-in-boxes/stock-in/'+stockInCode+'/box/'+boxCode+'/moveDown')
        }
        return service;
    }
})();
