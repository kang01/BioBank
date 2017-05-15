(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutRequiredSampleDetailController', StockOutRequiredSampleDetailController);

    StockOutRequiredSampleDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutRequiredSample', 'StockOutRequirement'];

    function StockOutRequiredSampleDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutRequiredSample, StockOutRequirement) {
        var vm = this;

        vm.stockOutRequiredSample = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutRequiredSampleUpdate', function(event, result) {
            vm.stockOutRequiredSample = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
