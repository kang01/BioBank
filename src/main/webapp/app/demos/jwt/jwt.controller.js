/**
 * Created by zhuyu on 2017/3/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('DemoJWTController', DemoJWTController);

    DemoJWTController.$inject = ['$scope', '$compile', 'Principal', '$state', '$http'];

    function DemoJWTController($scope, $compile, Principal, $state, $http) {
        var vm = this;
        vm.products = [];

        vm.run = function() {
            $http.get("http://localhost:8081/api/products").then(function(res){
                vm.products = res.data;
            });
        };
    }
})();
