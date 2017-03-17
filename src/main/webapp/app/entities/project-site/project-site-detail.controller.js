(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectSiteDetailController', ProjectSiteDetailController);

    ProjectSiteDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectSite', 'ProjectRelate'];

    function ProjectSiteDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectSite, ProjectRelate) {
        var vm = this;

        vm.projectSite = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:projectSiteUpdate', function(event, result) {
            vm.projectSite = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
