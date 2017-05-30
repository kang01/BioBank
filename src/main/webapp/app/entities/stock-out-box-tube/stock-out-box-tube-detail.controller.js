(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutBoxTubeDetailController', StockOutBoxTubeDetailController);

    StockOutBoxTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutBoxTube', 'StockOutFrozenBox', 'FrozenTube', 'StockOutTaskFrozenTube'];

    function StockOutBoxTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutBoxTube, StockOutFrozenBox, FrozenTube, StockOutTaskFrozenTube) {
        var vm = this;

        vm.stockOutBoxTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutBoxTubeUpdate', function(event, result) {
            vm.stockOutBoxTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
