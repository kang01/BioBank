(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutApplyProjectDetailController', StockOutApplyProjectDetailController);

    StockOutApplyProjectDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutApplyProject', 'StockOutApply', 'Project'];

    function StockOutApplyProjectDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutApplyProject, StockOutApply, Project) {
        var vm = this;

        vm.stockOutApplyProject = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutApplyProjectUpdate', function(event, result) {
            vm.stockOutApplyProject = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
