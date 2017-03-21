(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('SupportRackTypeDetailController', SupportRackTypeDetailController);

    SupportRackTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SupportRackType'];

    function SupportRackTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, SupportRackType) {
        var vm = this;

        vm.supportRackType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:supportRackTypeUpdate', function(event, result) {
            vm.supportRackType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
