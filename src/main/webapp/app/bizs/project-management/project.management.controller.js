/**
 * Created by gaokangkang on 2017/8/31.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementController', ProjectManagementController);

    ProjectManagementController.$inject = ['$scope','$state'];

    function ProjectManagementController($scope,$state) {
        var vm = this;
        vm.addProjectManagement = _fnAddProjectManagement;

        function _fnAddProjectManagement() {
            $state.go("project-management-info")
        }
    }
})();
