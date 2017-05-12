/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PlanListController', PlanListController);

    PlanListController.$inject = ['$scope','$state'];

    function PlanListController($scope,$state) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('plan-new');
        }
    }
})();
