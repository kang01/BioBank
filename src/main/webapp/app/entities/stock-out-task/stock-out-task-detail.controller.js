(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutTaskDetailController', StockOutTaskDetailController);

    StockOutTaskDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutTask', 'StockOutPlan'];

    function StockOutTaskDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutTask, StockOutPlan) {
        var vm = this;

        vm.stockOutTask = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutTaskUpdate', function(event, result) {
            vm.stockOutTask = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
