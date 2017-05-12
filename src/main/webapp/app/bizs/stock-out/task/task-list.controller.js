/**
 * Created by gaokangkang on 2017/5/12.
 * 出库申请列表
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TaskListController', TaskListController);

    TaskListController.$inject = ['$scope','$state'];

    function TaskListController($scope,$state) {
        var vm = this;
        vm.authorities = ['ROLE_USER', 'ROLE_ADMIN'];
        vm.add = _fnAdd;
        function _fnAdd() {
            $state.go('task-new');
        }
    }
})();
