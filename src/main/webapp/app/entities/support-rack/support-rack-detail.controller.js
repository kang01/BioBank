(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SupportRackDetailController', SupportRackDetailController);

    SupportRackDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SupportRack', 'SupportRackType', 'Area'];

    function SupportRackDetailController($scope, $rootScope, $stateParams, previousState, entity, SupportRack, SupportRackType, Area) {
        var vm = this;

        vm.supportRack = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:supportRackUpdate', function(event, result) {
            vm.supportRack = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
