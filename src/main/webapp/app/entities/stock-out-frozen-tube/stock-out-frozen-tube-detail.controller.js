(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFrozenTubeDetailController', StockOutFrozenTubeDetailController);

    StockOutFrozenTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutFrozenTube', 'StockOutFrozenBox', 'FrozenTube'];

    function StockOutFrozenTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutFrozenTube, StockOutFrozenBox, FrozenTube) {
        var vm = this;

        vm.stockOutFrozenTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutFrozenTubeUpdate', function(event, result) {
            vm.stockOutFrozenTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
