(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipDetailController', TranshipDetailController);

    TranshipDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Tranship', 'Project', 'ProjectSite'];

    function TranshipDetailController($scope, $rootScope, $stateParams, previousState, entity, Tranship, Project, ProjectSite) {
        var vm = this;

        vm.tranship = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:transhipUpdate', function(event, result) {
            vm.tranship = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
