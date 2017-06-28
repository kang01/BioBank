/**
 * Created by gaokangkang on 2017/6/20.
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInInputService', StockInInputService);

    StockInInputService.$inject = ['$http'];
    function StockInInputService($http) {
        var service = {
            //获取冻存管
            queryTube:_queryTube,
            //获取冻存盒
            queryStockInBox:_queryStockInBox,
            //编辑冻存盒
            queryEditStockInBox:_queryEditStockInBox,
            //新增保存入库
            saveStockIn:_fnSaveStockIn,
            //编辑保存入库
            saveEditStockIn:_fnSaveEditStockIn,
            //保存盒子
            saveStockInBox:_fnSaveStockInBox,
            //编辑保存盒子
            saveEditStockInBox:_fnSaveEditStockInBox


        };
        function _queryTube(sampleCode,projectCode,sampleTypeCode) {
            return $http.get('api/frozen-tubes/sample/'+sampleCode+'/project/'+projectCode+'/sampleType/'+sampleTypeCode);
        }
        function _fnSaveStockIn(param) {
            return $http.post('api/stock-ins',param);
        }
        function _fnSaveEditStockIn(param) {
            return $http.put('api/stock-ins',param);
        }
        function _queryStockInBox(frozenBoxCode) {
            return $http.get('api/frozen-boxes/boxCode/'+frozenBoxCode+'/forStockIn/');
        }
        function _queryEditStockInBox(frozenBoxCode) {
            return $http.get('/api/stock-in-boxes/boxCode/'+frozenBoxCode);
        }
        function _fnSaveStockInBox(stockInCode,param) {
            return $http.post('/api/stock-in-boxes/stockInCode/'+stockInCode,param);
        }
        function _fnSaveEditStockInBox(stockInBoxCode,param) {
            return $http.put('api/stock-in-boxes/stockInCode/'+stockInBoxCode,param);
        }
        return service;
    }
})();
