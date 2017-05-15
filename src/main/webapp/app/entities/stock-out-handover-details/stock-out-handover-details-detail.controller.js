(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverDetailsDetailController', StockOutHandoverDetailsDetailController);

    StockOutHandoverDetailsDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutHandoverDetails', 'StockOutHandover', 'StockOutFrozenTube'];

    function StockOutHandoverDetailsDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutHandoverDetails, StockOutHandover, StockOutFrozenTube) {
        var vm = this;

        vm.stockOutHandoverDetails = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutHandoverDetailsUpdate', function(event, result) {
            vm.stockOutHandoverDetails = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
