(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutRequirementDetailController', StockOutRequirementDetailController);

    StockOutRequirementDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutRequirement', 'StockOutApply', 'SampleType', 'SampleClassification', 'FrozenTubeType'];

    function StockOutRequirementDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutRequirement, StockOutApply, SampleType, SampleClassification, FrozenTubeType) {
        var vm = this;

        vm.stockOutRequirement = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutRequirementUpdate', function(event, result) {
            vm.stockOutRequirement = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
