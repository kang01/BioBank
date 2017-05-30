(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutTaskFrozenTubeDetailController', StockOutTaskFrozenTubeDetailController);

    StockOutTaskFrozenTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutTaskFrozenTube', 'StockOutTask', 'StockOutPlanFrozenTube'];

    function StockOutTaskFrozenTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutTaskFrozenTube, StockOutTask, StockOutPlanFrozenTube) {
        var vm = this;

        vm.stockOutTaskFrozenTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutTaskFrozenTubeUpdate', function(event, result) {
            vm.stockOutTaskFrozenTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
