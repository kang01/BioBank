(function() {
    'use strict';

    angular
        .module('bioBankApp')
        .controller('FrozenBoxTypeDetailController', FrozenBoxTypeDetailController);

    FrozenBoxTypeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'FrozenBoxType'];

    function FrozenBoxTypeDetailController($scope, $rootScope, $stateParams, previousState, entity, FrozenBoxType) {
        var vm = this;

        vm.frozenBoxType = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('bioBankApp:frozenBoxTypeUpdate', function(event, result) {
            vm.frozenBoxType = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
