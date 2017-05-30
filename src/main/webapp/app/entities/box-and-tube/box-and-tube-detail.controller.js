(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('BoxAndTubeDetailController', BoxAndTubeDetailController);

    BoxAndTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'BoxAndTube', 'FrozenBox', 'FrozenTube'];

    function BoxAndTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, BoxAndTube, FrozenBox, FrozenTube) {
        var vm = this;

        vm.boxAndTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:boxAndTubeUpdate', function(event, result) {
            vm.boxAndTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
