(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('TranshipStockInDetailController', TranshipStockInDetailController);

    TranshipStockInDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'TranshipStockIn', 'Tranship', 'StockIn'];

    function TranshipStockInDetailController($scope, $rootScope, $stateParams, previousState, entity, TranshipStockIn, Tranship, StockIn) {
        var vm = this;

        vm.transhipStockIn = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:transhipStockInUpdate', function(event, result) {
            vm.transhipStockIn = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
