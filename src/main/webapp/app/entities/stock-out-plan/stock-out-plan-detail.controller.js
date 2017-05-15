(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutPlanDetailController', StockOutPlanDetailController);

    StockOutPlanDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutPlan', 'StockOutApply'];

    function StockOutPlanDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutPlan, StockOutApply) {
        var vm = this;

        vm.stockOutPlan = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutPlanUpdate', function(event, result) {
            vm.stockOutPlan = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
