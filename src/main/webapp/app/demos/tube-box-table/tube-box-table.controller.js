/**
 * Created by zhuyu on 2017/3/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DemoTubeBoxTableController', DemoTubeBoxTableController);

    DemoTubeBoxTableController.$inject = ['$scope', '$compile', 'Principal', 'StockInService', 'ParseLinks', 'AlertService', '$state', 'StockInInputService'];

    function DemoTubeBoxTableController($scope, $compile, Principal, StockInService, ParseLinks, AlertService, $state, StockInInputService) {
        var vm = this;
        vm.checked1 = false;
        vm.checked2 = false;
        vm.toggle1 = function () {
            vm.checked1 = !vm.checked1;
        }
        vm.toggle2 = function () {
            vm.checked2 = !vm.checked2;
        }

        vm.box = {};
        vm.tubes = [];
        StockInInputService.queryEditStockInBox(53783).success(function (data) {
            console.log(data);
            vm.box = data;
            vm.tubes = data.frozenTubeDTOS;
            vm.tubes[0].status = "3001";
            vm.tubes[1].status = "3002";
            vm.tubes[2].status = "3003";
            vm.tubes[3].status = "3004";
            vm.tubes[4].memo = "abcdefghijklmnopqrstuvwxyz\nabcdefghijklmnopqrstuvwxyz";
            vm.tubes[5].flag = 2;
            vm.tubes[6].flag = 2;

            vm.htInstance.api.loadData(data, data.frozenTubeDTOS);

            // if ($scope.$digest()){
            //     $scope.$apply();
            // }
        });

        vm.select = function(){
            vm.htInstance.api.selectRangeCell([[0,1],[0,3],[0,5]]);
        };
        vm.selectAll = function(){
            vm.htInstance.api.selectAll();
        };
        vm.setValueEditable = function(){
            vm.htInstance.api.updateSettings({
                isCellValueEditable: true,
                isCellStatusEditable: false,
            });
        };
        vm.setStatusEditable = function(){
            vm.htInstance.api.updateSettings({
                isCellValueEditable: false,
                isCellStatusEditable: true,
            });
        };

        vm.getGridData = function(){
            var data = vm.htInstance.api.getGridData();
            console.log(data);
        };

        vm.exchange = function(){
            vm.htInstance.api.exchangeSelectedTubePosition();
        }
    }
})();
