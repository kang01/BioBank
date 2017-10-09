(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutHandoverBoxDetailController', StockOutHandoverBoxDetailController);

    StockOutHandoverBoxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StockOutHandoverBox', 'Equipment', 'Area', 'SupportRack', 'StockOutFrozenBox'];

    function StockOutHandoverBoxDetailController($scope, $rootScope, $stateParams, previousState, entity, StockOutHandoverBox, Equipment, Area, SupportRack, StockOutFrozenBox) {
        var vm = this;

        vm.stockOutHandoverBox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutHandoverBoxUpdate', function(event, result) {
            vm.stockOutHandoverBox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
