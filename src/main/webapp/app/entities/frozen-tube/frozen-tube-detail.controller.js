(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeDetailController', FrozenTubeDetailController);

    FrozenTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FrozenTube', 'FrozenTubeType', 'SampleType', 'Project'];

    function FrozenTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, FrozenTube, FrozenTubeType, SampleType, Project) {
        var vm = this;

        vm.frozenTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:frozenTubeUpdate', function(event, result) {
            vm.frozenTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
