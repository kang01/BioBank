/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('requirementListController', requirementListController);

    requirementListController.$inject = ['$scope','$state'];

    function requirementListController($scope,$state) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('requirement-new');
        }
    }
})();
