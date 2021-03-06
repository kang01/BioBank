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
            queryTubeBySampleClassificationId:_queryTubeBySampleClassificationId,
            //获取冻存盒
            queryStockInBox:_queryStockInBox,
            //获取未满的库里的盒子
            queryUnfullStockInBox:_queryUnfullStockInBox,
            //编辑冻存盒
            queryEditStockInBox:_queryEditStockInBox,
            //新增保存入库
            saveStockIn:_fnSaveStockIn,
            //编辑保存入库
            // saveEditStockIn:_fnSaveEditStockIn,
            //保存盒子
            saveStockInBox:_fnSaveStockInBox,
            //编辑保存盒子
            saveEditStockInBox:_fnSaveEditStockInBox,
            //作废
            stockInCancellation:_fnStockInCancellation,
            //生成冻存盒号
            makeNewBoxCode:_fnMakeNewBoxCode


        };
        // function _queryTube(sampleCode,projectCode,sampleTypeId) {
        //     return $http.get('api/frozen-tubes/sample/'+sampleCode+'/project/'+projectCode+'/sampleType/'+sampleTypeId);
        // }
        function _queryTube(sampleCode,projectCode,frozenBoxId,sampleTypeId,sampleClassificationId) {
            if(!frozenBoxId){
                frozenBoxId = -1;
            }
            if(!sampleClassificationId){
                sampleClassificationId = -1;
            }
            return $http.get('api/frozen-tubes/sample/'+sampleCode+'/project/'+projectCode+'/frozenBox/'+frozenBoxId+'/sampleType/'+sampleTypeId+'/sampleClassification/'+sampleClassificationId);
        }
        function _queryTubeBySampleClassificationId(sampleCode,projectCode,sampleTypeId,sampleClassificationId) {
            return $http.get('api/frozen-tubes/sample/'+sampleCode+'/project/'+projectCode+'/sampleType/'+sampleTypeId+'/sampleClassification/'+sampleClassificationId);
        }
        function _fnSaveStockIn(param) {
            if(param.id){
                return $http.put('api/stock-ins',param);
            }else{
                return $http.post('api/stock-ins',param);

            }
        }
        // function _fnSaveEditStockIn(param) {
        //
        // }
        function _queryStockInBox(frozenBoxCode) {
            return $http.get('api/frozen-boxes/boxCode/'+frozenBoxCode+'/forStockIn/');
        }
        function _queryUnfullStockInBox(frozenBoxCode,projectId,stockInCode) {
            return $http.get('api/frozen-boxes/specify-unfull-frozenBox/'+frozenBoxCode+'/project/'+projectId+'/stockIn/'+stockInCode);
        }
        function _queryEditStockInBox(stockInBoxId) {
            return $http.get('api/stock-in-boxes/stockInBoxId/'+stockInBoxId);
        }
        function _fnSaveStockInBox(stockInCode,param) {
            if(param.id){
                return $http.put('api/stock-in-boxes/stockInCode/'+stockInCode,param);
            }else{
                return $http.post('api/stock-in-boxes/stockInCode/'+stockInCode,param);
            }
        }
        function _fnSaveEditStockInBox(stockInBoxCode,param) {
            return $http.put('api/stock-in-boxes/stockInCode/'+stockInBoxCode,param);
        }
        function _fnStockInCancellation(stockInCode) {
            return $http.put('api/stock-in/invalid/'+stockInCode);
        }
        function _fnMakeNewBoxCode(projectId,sampleTypeId,sampleClassId) {
            var http;
            if(sampleClassId){
                http = 'api/frozen-boxes/'+projectId+'/'+sampleTypeId+'/'+sampleClassId+'/makeNewBoxCode';
            }else{
                http = 'api/frozen-boxes/'+projectId+'/'+sampleTypeId+'/makeNewBoxCode';
            }
            return $http.get(http)
        }

        return service;
    }
})();
