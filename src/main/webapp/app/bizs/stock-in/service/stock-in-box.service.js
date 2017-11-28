/**
 * Created by zhuyu on 2017/3/31.
 * 入库单列表
 */
(function () {
    'use strict';

    angular
        .module('bioBankApp')
        .factory('StockInBoxService', StockInBoxService);

    StockInBoxService.$inject = ['$resource', '$http'];

    function StockInBoxService ($resource, $http) {
        var service = $resource('api/stock-in-box/:id', {}, {
            'query': {method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'save': { method:'POST' },
            'update': { method:'PUT' },
            'delete':{ method:'DELETE'}
        });

        service.getJqDataTableValues = function(data){
            var ajaxUrl = 'api/res/stock-in-boxes/stock-in/' + data.stockInCode;

            var req = {
                method: 'POST',
                url: ajaxUrl,
                headers: {
                    'Content-Type': 'application/json'
                },
                data: JSON.stringify(data)
            };

            return $http(req);
        };
        //不分页的盒子集合
        service.getBoxesNoPage = function (stockInCode) {
            return $http.get('api/stock-in-boxes/stock-in/' + stockInCode);
        };
        service.getStockInBoxByCodes = function(codes){
            var url = "api/stock-in-boxes/boxCodes/" + codes.join(',');
            return $http.get(url);
        };

        service.saveBoxPosition = function(stockInCode, boxCode, position){
            var url = "api/stock-in-boxes/stock-in/"+stockInCode+"/box/"+boxCode+"/moved";
            return $http.put(url, position);
        };

        return service;
    }
})();
