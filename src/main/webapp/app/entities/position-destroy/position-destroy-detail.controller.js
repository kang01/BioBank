(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('PositionDestroyDetailController', PositionDestroyDetailController);

    PositionDestroyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'PositionDestroy'];

    function PositionDestroyDetailController($scope, $rootScope, $stateParams, previousState, entity, PositionDestroy) {
        var vm = this;

        vm.positionDestroy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:positionDestroyUpdate', function(event, result) {
            vm.positionDestroy = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
