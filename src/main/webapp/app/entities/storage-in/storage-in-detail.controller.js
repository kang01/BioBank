(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StorageInDetailController', StorageInDetailController);

    StorageInDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StorageIn', 'Tranship'];

    function StorageInDetailController($scope, $rootScope, $stateParams, previousState, entity, StorageIn, Tranship) {
        var vm = this;

        vm.storageIn = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:storageInUpdate', function(event, result) {
            vm.storageIn = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
