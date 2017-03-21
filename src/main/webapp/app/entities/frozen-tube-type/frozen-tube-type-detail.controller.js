(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenTubeTypeDetailController', FrozenTubeTypeDetailController);

    FrozenTubeTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FrozenTubeType'];

    function FrozenTubeTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, FrozenTubeType) {
        var vm = this;

        vm.frozenTubeType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:frozenTubeTypeUpdate', function(event, result) {
            vm.frozenTubeType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
