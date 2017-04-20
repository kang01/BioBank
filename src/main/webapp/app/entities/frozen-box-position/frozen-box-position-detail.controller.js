(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxPositionDetailController', FrozenBoxPositionDetailController);

    FrozenBoxPositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FrozenBoxPosition', 'Equipment', 'Area', 'SupportRack', 'FrozenBox'];

    function FrozenBoxPositionDetailController($scope, $rootScope, $stateParams, previousState, entity, FrozenBoxPosition, Equipment, Area, SupportRack, FrozenBox) {
        var vm = this;

        vm.frozenBoxPosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:frozenBoxPositionUpdate', function(event, result) {
            vm.frozenBoxPosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
