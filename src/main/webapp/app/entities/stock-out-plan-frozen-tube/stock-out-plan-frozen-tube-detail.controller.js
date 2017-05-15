(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutPlanFrozenTubeDetailController', StockOutPlanFrozenTubeDetailController);

    StockOutPlanFrozenTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutPlanFrozenTube', 'StockOutPlan', 'FrozenBox', 'FrozenTube'];

    function StockOutPlanFrozenTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutPlanFrozenTube, StockOutPlan, FrozenBox, FrozenTube) {
        var vm = this;

        vm.stockOutPlanFrozenTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutPlanFrozenTubeUpdate', function(event, result) {
            vm.stockOutPlanFrozenTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
