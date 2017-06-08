(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInBoxPositionDetailController', StockInBoxPositionDetailController);

    StockInBoxPositionDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockInBoxPosition', 'Equipment', 'Area', 'SupportRack', 'StockInBox'];

    function StockInBoxPositionDetailController($scope, $rootScope, $stateParams, previousState, entity, StockInBoxPosition, Equipment, Area, SupportRack, StockInBox) {
        var vm = this;

        vm.stockInBoxPosition = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockInBoxPositionUpdate', function(event, result) {
            vm.stockInBoxPosition = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
