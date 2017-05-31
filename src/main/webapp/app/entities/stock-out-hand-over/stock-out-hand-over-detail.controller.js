(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandOverDetailController', StockOutHandOverDetailController);

    StockOutHandOverDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutHandOver', 'StockOutTask', 'StockOutApply', 'StockOutPlan'];

    function StockOutHandOverDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutHandOver, StockOutTask, StockOutApply, StockOutPlan) {
        var vm = this;

        vm.stockOutHandOver = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutHandOverUpdate', function(event, result) {
            vm.stockOutHandOver = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
