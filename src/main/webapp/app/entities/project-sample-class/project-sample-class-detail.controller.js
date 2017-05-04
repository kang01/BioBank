(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('ProjectSampleClassDetailController', ProjectSampleClassDetailController);

    ProjectSampleClassDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProjectSampleClass', 'Project', 'SampleType', 'SampleClassification'];

    function ProjectSampleClassDetailController($scope, $rootScope, $stateParams, previousState, entity, ProjectSampleClass, Project, SampleType, SampleClassification) {
        var vm = this;

        vm.projectSampleClass = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:projectSampleClassUpdate', function(event, result) {
            vm.projectSampleClass = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
