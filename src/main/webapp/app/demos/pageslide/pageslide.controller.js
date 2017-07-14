/**
 * Created by zhuyu on 2017/3/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DemoPageslideController', DemoPageslideController);

    DemoPageslideController.$inject = ['$scope', '$compile', 'Principal', 'StockInService', 'ParseLinks', 'AlertService', '$state'];

    function DemoPageslideController($scope, $compile, Principal, StockInService, ParseLinks, AlertService, $state) {
        var vm = this;
        vm.checked1 = false;
        vm.checked2 = false;
        vm.toggle1 = function () {
            vm.checked1 = !vm.checked1;
        }
        vm.toggle2 = function () {
            vm.checked2= !vm.checked2;
        }
    }
})();
