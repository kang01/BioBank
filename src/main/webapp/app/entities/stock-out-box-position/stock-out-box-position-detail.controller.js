(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutBoxPositionDetailController', StockOutBoxPositionDetailController);

    StockOutBoxPositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutBoxPosition', 'Equipment', 'Area', 'SupportRack', 'FrozenBox'];

    function StockOutBoxPositionDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutBoxPosition, Equipment, Area, SupportRack, FrozenBox) {
        var vm = this;

        vm.stockOutBoxPosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutBoxPositionUpdate', function(event, result) {
            vm.stockOutBoxPosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
