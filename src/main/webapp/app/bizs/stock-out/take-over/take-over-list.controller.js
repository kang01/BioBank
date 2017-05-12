/**
 * Created by gaokangkang on 2017/5/12.
 * 出库交接列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TakeOverListController', TakeOverListController);

    TakeOverListController.$inject = ['$scope','$state'];

    function TakeOverListController($scope,$state) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('take-over-new');
        }
    }
})();
