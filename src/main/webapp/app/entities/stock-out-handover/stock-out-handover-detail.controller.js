(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverDetailController', StockOutHandoverDetailController);

    StockOutHandoverDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutHandover', 'StockOutTask'];

    function StockOutHandoverDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutHandover, StockOutTask) {
        var vm = this;

        vm.stockOutHandover = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutHandoverUpdate', function(event, result) {
            vm.stockOutHandover = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
