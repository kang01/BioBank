(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionChangeDetailController', PositionChangeDetailController);

    PositionChangeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PositionChange'];

    function PositionChangeDetailController($scope, $rootScope, $stateParams, previousState, entity, PositionChange) {
        var vm = this;

        vm.positionChange = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:positionChangeUpdate', function(event, result) {
            vm.positionChange = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
