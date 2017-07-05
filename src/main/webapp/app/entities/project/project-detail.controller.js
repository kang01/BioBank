(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectDetailController', ProjectDetailController);

    ProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Project', 'ProjectRelate', 'Delegate'];

    function ProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, Project, ProjectRelate, Delegate) {
        var vm = this;

        vm.project = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:projectUpdate', function(event, result) {
            vm.project = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
