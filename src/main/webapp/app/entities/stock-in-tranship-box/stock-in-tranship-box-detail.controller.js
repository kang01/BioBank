(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockInTranshipBoxDetailController', StockInTranshipBoxDetailController);

    StockInTranshipBoxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockInTranshipBox', 'TranshipBox', 'StockIn'];

    function StockInTranshipBoxDetailController($scope, $rootScope, $stateParams, previousState, entity, StockInTranshipBox, TranshipBox, StockIn) {
        var vm = this;

        vm.stockInTranshipBox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockInTranshipBoxUpdate', function(event, result) {
            vm.stockInTranshipBox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
