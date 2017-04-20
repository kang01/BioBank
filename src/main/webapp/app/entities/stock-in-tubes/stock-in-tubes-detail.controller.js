(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTubesDetailController', StockInTubesDetailController);

    StockInTubesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockInTubes', 'FrozenTube', 'FrozenBoxPosition', 'TranshipBox', 'StockInBox'];

    function StockInTubesDetailController($scope, $rootScope, $stateParams, previousState, entity, StockInTubes, FrozenTube, FrozenBoxPosition, TranshipBox, StockInBox) {
        var vm = this;

        vm.stockInTubes = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockInTubesUpdate', function(event, result) {
            vm.stockInTubes = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
