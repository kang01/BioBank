(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectRelateDetailController', ProjectRelateDetailController);

    ProjectRelateDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectRelate', 'Project', 'ProjectSite'];

    function ProjectRelateDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectRelate, Project, ProjectSite) {
        var vm = this;

        vm.projectRelate = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:projectRelateUpdate', function(event, result) {
            vm.projectRelate = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
