/**
 * Created by gaokangkang on 2017/9/5.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementInfoController', ProjectManagementInfoController);

    ProjectManagementInfoController.$inject = ['$scope','$stateParams'];

    function ProjectManagementInfoController($scope,$stateParams) {
        console.log($stateParams.status);
        var vm = this;

    }
})();
