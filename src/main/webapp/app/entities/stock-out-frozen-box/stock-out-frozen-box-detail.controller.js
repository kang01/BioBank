(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFrozenBoxDetailController', StockOutFrozenBoxDetailController);

    StockOutFrozenBoxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutFrozenBox', 'FrozenBox', 'StockOutBoxPosition', 'StockOutTask'];

    function StockOutFrozenBoxDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutFrozenBox, FrozenBox, StockOutBoxPosition, StockOutTask) {
        var vm = this;

        vm.stockOutFrozenBox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutFrozenBoxUpdate', function(event, result) {
            vm.stockOutFrozenBox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
