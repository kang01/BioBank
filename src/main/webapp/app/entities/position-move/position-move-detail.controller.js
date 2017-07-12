(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionMoveDetailController', PositionMoveDetailController);

    PositionMoveDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PositionMove'];

    function PositionMoveDetailController($scope, $rootScope, $stateParams, previousState, entity, PositionMove) {
        var vm = this;

        vm.positionMove = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:positionMoveUpdate', function(event, result) {
            vm.positionMove = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
