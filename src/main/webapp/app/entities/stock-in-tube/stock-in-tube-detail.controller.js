(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTubeDetailController', StockInTubeDetailController);

    StockInTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockInTube', 'StockInBox', 'FrozenTube'];

    function StockInTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, StockInTube, StockInBox, FrozenTube) {
        var vm = this;

        vm.stockInTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockInTubeUpdate', function(event, result) {
            vm.stockInTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
