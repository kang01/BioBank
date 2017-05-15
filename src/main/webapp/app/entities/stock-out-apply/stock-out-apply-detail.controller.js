(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutApplyDetailController', StockOutApplyDetailController);

    StockOutApplyDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutApply', 'Delegate'];

    function StockOutApplyDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutApply, Delegate) {
        var vm = this;

        vm.stockOutApply = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutApplyUpdate', function(event, result) {
            vm.stockOutApply = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
