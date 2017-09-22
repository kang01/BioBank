/**
 * Created by gaokangkang on 2017/9/12.
 */
(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectManagementSitesController', ProjectManagementSitesController);

    ProjectManagementSitesController.$inject = ['$scope','$stateParams'];

    function ProjectManagementSitesController($scope,$stateParams) {
        console.log($stateParams.status);
        var vm = this;
    }
})();
