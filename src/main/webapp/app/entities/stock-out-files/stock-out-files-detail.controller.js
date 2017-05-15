(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StockOutFilesDetailController', StockOutFilesDetailController);

    StockOutFilesDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'DataUtils', 'entity', 'StockOutFiles'];

    function StockOutFilesDetailController($scope, $rootScope, $stateParams, previousState, DataUtils, entity, StockOutFiles) {
        var vm = this;

        vm.stockOutFiles = entity;
        vm.previousState = previousState.name;
        vm.byteSize = DataUtils.byteSize;
        vm.openFile = DataUtils.openFile;

        var unsubscribe = $rootScope.$on('bioBankApp:stockOutFilesUpdate', function(event, result) {
            vm.stockOutFiles = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
