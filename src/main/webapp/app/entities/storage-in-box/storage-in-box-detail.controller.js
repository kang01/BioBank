(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('StorageInBoxDetailController', StorageInBoxDetailController);

    StorageInBoxDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'StorageInBox', 'StorageIn'];

    function StorageInBoxDetailController($scope, $rootScope, $stateParams, previousState, entity, StorageInBox, StorageIn) {
        var vm = this;

        vm.storageInBox = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:storageInBoxUpdate', function(event, result) {
            vm.storageInBox = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
