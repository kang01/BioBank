(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutReqFrozenTubeDetailController', StockOutReqFrozenTubeDetailController);

    StockOutReqFrozenTubeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutReqFrozenTube', 'FrozenBox', 'FrozenTube', 'StockOutRequirement'];

    function StockOutReqFrozenTubeDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutReqFrozenTube, FrozenBox, FrozenTube, StockOutRequirement) {
        var vm = this;

        vm.stockOutReqFrozenTube = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutReqFrozenTubeUpdate', function(event, result) {
            vm.stockOutReqFrozenTube = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
